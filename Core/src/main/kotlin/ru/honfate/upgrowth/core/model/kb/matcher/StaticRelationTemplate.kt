package ru.honfate.upgrowth.core.model.kb.matcher

import ru.honfate.upgrowth.core.model.blocks.Context
import ru.honfate.upgrowth.core.model.types.basic.StaticRelationValue

class StaticRelationTemplate(
    val name: String,
    val operands: Array<TemplateValue>,
    val value: Boolean = true
) {
    fun matches(relation: StaticRelationValue, context: Context): Boolean {
        return relation.name == name &&
                relation.operands.size == operands.size &&
                operands.mapIndexed {i, template ->
                    template.matches(relation.operands[i], context)
                }.reduce { b1, b2 -> b1 && b2}
    }
}