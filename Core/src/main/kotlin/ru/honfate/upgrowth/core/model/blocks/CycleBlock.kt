package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.types.EmptyValue
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue

abstract class CycleBlock(conditionBlock: ConditionBlock, body: Block): Block  {
    override val returnType: Type
        get() = EmptyValue()
    override val additionalContext: Context
        get() = emptyContext()
}