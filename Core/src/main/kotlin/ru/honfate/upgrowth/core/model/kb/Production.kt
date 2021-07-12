package ru.honfate.upgrowth.core.model.kb

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.blocks.Block
import ru.honfate.upgrowth.core.model.blocks.Context
import ru.honfate.upgrowth.core.model.blocks.emptyContext
import ru.honfate.upgrowth.core.model.exception.CompilationErrorException
import ru.honfate.upgrowth.core.model.kb.matcher.StaticRelationTemplate
import ru.honfate.upgrowth.core.model.types.basic.StaticRelationValue
import ru.honfate.upgrowth.core.model.types.functional.Relation

class Production(val number: Int,
                 val staticProducers: List<StaticRelationTemplate>,
                 val dynamicProducers: List<Block>,
                 val staticConsumers: List<Block>,
                 val dynamicConsumers: List<Block>) {
    init {
        if (staticProducers.isEmpty())
            throw CompilationErrorException("Production should have static producers")
        if (staticConsumers.size + dynamicConsumers.size == 0)
            throw CompilationErrorException("Production should have consumers")
    }

    val context: Context = emptyContext()

    private fun matchesDynamicProducers(context: Context, core: Core): Boolean {
        return dynamicProducers
            .map { (it.run(core, context) as StaticRelationValue).value }
            .reduce {b1, b2 -> b1 && b2}
    }

    private fun matchesRecursion(list: List<StaticRelationTemplate>,
                                 factsIndex: Map<String, List<StaticRelationValue>>,
                                 context: Context,
                                 core: Core,
                                 isTop: Boolean = false): Boolean {
        if (list.isEmpty()) return true
        val template = list.first()
        lateinit var localContext: Context
        for (fact in factsIndex[template.name]!!) {
            localContext = context.toMutableMap()
            if (template.matches(fact, localContext)) {
                val lowerLevelValue = matchesRecursion(list.subList(1, list.size), factsIndex, localContext, core)
                if (!isTop) {
                    if (lowerLevelValue) {
                        context.putAll(localContext)
                        return true
                    }
                    else continue
                }
                else  {
                    if (lowerLevelValue && matchesDynamicProducers(localContext, core)) {
                        context.putAll(localContext)
                        return true
                    }
                    else continue
                }
            }
        }
        return false
    }

    fun matches(relations: Iterable<StaticRelationValue>, core: Core): Boolean {
        val staticProducerIndex = staticProducers.groupBy { it.name }
        val factsIndex = relations.filter { it.name in staticProducerIndex.keys }
            .groupBy { it.name }
        return matchesRecursion(staticProducers, factsIndex, context, core, true)
    }
}