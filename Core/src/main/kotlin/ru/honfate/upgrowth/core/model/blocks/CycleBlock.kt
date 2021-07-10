package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.types.basic.EmptyValue
import ru.honfate.upgrowth.core.model.types.Type

abstract class CycleBlock(conditionBlock: ConditionBlock, body: Block): Block  {
    override val returnType: Type
        get() = EmptyValue()
    override val additionalContext: Context
        get() = emptyContext()
}