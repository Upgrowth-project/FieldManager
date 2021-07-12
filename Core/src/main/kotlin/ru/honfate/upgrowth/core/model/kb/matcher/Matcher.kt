package ru.honfate.upgrowth.core.model.kb.matcher

import ru.honfate.upgrowth.core.model.blocks.emptyContext
import ru.honfate.upgrowth.core.model.exception.MatcherException
import ru.honfate.upgrowth.core.model.kb.Production
import ru.honfate.upgrowth.core.model.types.basic.StaticRelationValue
import kotlin.Any

class Matcher(val type: EventType, val target: Any) {
    init {
        when (type) {
            EventType.FactPut -> {
                if (target !is StaticRelationTemplate)
                    throw MatcherException("Expected a static relation template")
            }
            EventType.FactDeleted -> {
                if (target !is StaticRelationTemplate)
                    throw MatcherException("Expected a static relation template")
            }
            EventType.ProductionActivated -> {
                if (target !is Production)
                    throw MatcherException("Expected a production")
            }
        }
    }

    enum class EventType {
        FactPut, FactDeleted, ProductionActivated
    }

    fun wasPut(relation: StaticRelationValue): Boolean = type == EventType.FactPut &&
            (target as StaticRelationTemplate).matches(relation, emptyContext())

    fun wasDeleted(relation: StaticRelationValue): Boolean = type == EventType.FactDeleted &&
            (target as StaticRelationTemplate).matches(relation, emptyContext())

    fun wasActivated(production: Production): Boolean = type == EventType.ProductionActivated &&
            (target as Production).number == production.number
}

fun whenPut(template: StaticRelationTemplate): Matcher = Matcher(Matcher.EventType.FactPut, template)
fun whenDeleted(template: StaticRelationTemplate): Matcher = Matcher(Matcher.EventType.FactDeleted, template)
fun whenActivated(production: Production): Matcher = Matcher(Matcher.EventType.ProductionActivated, production)