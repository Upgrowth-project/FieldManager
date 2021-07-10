package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.types.basic.EmptyValue
import ru.honfate.upgrowth.core.model.types.TypedValue

class PreConditionCycleBlock(private val conditionBlock: BooleanConditionBlock,
                             private val body: Block) : CycleBlock(conditionBlock, body) {
    override fun run(core: Core, arguments: Context): TypedValue {
        val bodyContext = (arguments + conditionBlock.additionalContext).toMutableMap()
        while(conditionBlock.isTrue(core, arguments)) {
            body.run(core, bodyContext)
        }
        return EmptyValue()
    }
}