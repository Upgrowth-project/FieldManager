package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.types.EmptyValue
import ru.honfate.upgrowth.core.model.types.TypedValue

class PostConditionCycleBlock(private val conditionBlock: BooleanConditionBlock,
                              private val body: Block) : CycleBlock(conditionBlock, body) {
    override fun run(core: Core, arguments: Context): TypedValue {
        do {
            body.run(core, arguments)
        } while (conditionBlock.isTrue(core, arguments))
        return EmptyValue()
    }
}