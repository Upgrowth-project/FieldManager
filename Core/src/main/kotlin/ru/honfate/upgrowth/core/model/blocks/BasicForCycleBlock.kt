package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.types.EmptyValue
import ru.honfate.upgrowth.core.model.types.TypedValue

class BasicForCycleBlock(private val initBlock: Block,
                         private val conditionBlock: ConditionBlock,
                         private val incrementBlock: Block,
                         private val body: Block) : CycleBlock(conditionBlock, body) {

    override fun run(core: Core, arguments: Context): TypedValue {
        val conditionContext = arguments + initBlock.additionalContext
        val bodyContext = conditionContext + conditionBlock.additionalContext + incrementBlock.additionalContext
        initBlock.run(core, arguments)
        while (conditionBlock.isTrue(core, conditionContext)) {
            body.run(core, bodyContext)
            incrementBlock.run(core, bodyContext)
        }
        return EmptyValue()
    }
}