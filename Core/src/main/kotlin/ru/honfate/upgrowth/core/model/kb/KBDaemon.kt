package ru.honfate.upgrowth.core.model.kb

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.blocks.emptyContext
import ru.honfate.upgrowth.core.model.exception.KBOperationError
import ru.honfate.upgrowth.core.model.kb.matcher.Matcher
import ru.honfate.upgrowth.core.model.kb.matcher.StaticRelationTemplate
import ru.honfate.upgrowth.core.model.types.basic.StaticRelationValue

class KBDaemon(
    private val productions: List<Production>,
    private val core: Core
) {

    data class Fact(val relation: StaticRelationValue, val deepness: Int, val deleteMatcher: Matcher?)

    private val factBucket: MutableSet<Fact> =
        emptySet<Fact>().toMutableSet()

    private val matchersIndex: Map<Matcher.EventType, MutableSet<Fact>> =
        Matcher.EventType.values().associate { it to emptySet<Fact>().toMutableSet() }

    // Отображение "имя отношения -> список продукций, где оно встречается"
    private val productionIndex: Map<String, List<Production>> = productions.flatMap { production ->
        production.staticProducers.map {it.name to production}
    }   .groupBy({ it.first }, {it.second})
        .mapValues {entry -> entry.value.distinct().sortedBy { it.number } }

    // Счетчик вложенности текущего вызова
    private var nestedCallsCounter: Int = 0

    private lateinit var targetTemplate: StaticRelationTemplate
    private var target: StaticRelationValue? = null

    private fun findProduction(): Production? {
        val rawRelations = facts
        val relationNames = rawRelations.map { it.name }.toSet()
        return productionIndex
            .filter { it.key in relationNames }  // Отбираем только те продукции, где встречаются факты из базы
            .flatMap { it.value }.toSet()        // Множество всех продукции, уже без ключей
            .filter {production ->               // Отбираем только те продукции, чье множество статических продуцентов
                relationNames.containsAll(production.staticProducers.map {it.name}) // является подмножеством фактов
            }.sortedBy { it.number }             // Сортируем по номеру продукции
            .firstOrNull { it.matches(rawRelations, core) }
    }

    private fun activate(production: Production): List<StaticRelationValue> {
        matchersIndex[Matcher.EventType.ProductionActivated]!!
            .filter { it.deleteMatcher != null && it.deleteMatcher.wasActivated(production) }
            .forEach(::rawDelete)

        production.dynamicConsumers
            .forEach { it.run(core, production.context) }

        return production.dynamicConsumers
            .map {it.run(core, production.context) as StaticRelationValue}
    }

    private fun rawPut(relation: StaticRelationValue, deleteMatcher: Matcher?) {
        matchersIndex[Matcher.EventType.FactPut]!!
            .filter { it.deleteMatcher != null && it.deleteMatcher.wasPut(relation) }
            .forEach(::rawDelete)

        if (nestedCallsCounter > 0 && this::targetTemplate.isInitialized) {
            if (targetTemplate.matches(relation, emptyContext()))
                target = relation
        }

        factBucket.add(Fact(relation, nestedCallsCounter, deleteMatcher))
    }

    private fun rawDelete(fact: Fact) {
        if (fact.deleteMatcher != null) {
            matchersIndex[fact.deleteMatcher.type]!!.remove(fact)
        }
        matchersIndex[Matcher.EventType.FactDeleted]!!
            .filter { it.deleteMatcher != null && it.deleteMatcher.wasDeleted(fact.relation) }
            .forEach(::rawDelete)
        factBucket.remove(fact)
    }

    //API

    val facts: Set<StaticRelationValue>
    get() = factBucket.map{it.relation}.toSet()

    fun put(relation: StaticRelationValue, deleteMatcher: Matcher? = null) {
        if (relation !in factBucket.map {it.relation})
            rawPut(relation, deleteMatcher)
    }

    fun delete(relation: StaticRelationValue) {
        val fact = factBucket.find { it.relation == relation }
        when {
            fact == null -> throw KBOperationError("There is no such fact: $relation")
            fact.deepness < nestedCallsCounter ->
                throw KBOperationError("It is enable to delete fact $relation from outer scope")
            else -> rawDelete(fact)
        }
    }

    fun reset() {
        nestedCallsCounter = 0
        factBucket.clear()
        matchersIndex.forEach { (_, facts) -> facts.clear() }
    }

    // Вложенный вызов, который запускает экспертную систему, пока не появится отношение,
    // удовлятворяющее шаблону. Если система остановливает, то возвращается null,
    // иначе возвращается факт, который удовлетворяет шаблону
    fun implies(from: StaticRelationValue, to: StaticRelationTemplate): StaticRelationValue? {
        nestedCallsCounter++
        val temp = if (this::targetTemplate.isInitialized) targetTemplate else null
        targetTemplate = to
        put(from, null)
        var production = findProduction()

        while (production != null) {
            activate(production).forEach { put(it) }
            if (target == null) production = findProduction()
            else break
        }

        if (temp != null)
            targetTemplate = temp

        factBucket.filter { it.deepness == nestedCallsCounter }
            .forEach { rawDelete(it) }

        nestedCallsCounter--
        return target
    }
}