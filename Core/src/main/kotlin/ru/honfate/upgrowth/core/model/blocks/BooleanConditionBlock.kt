package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.types.TypedValue

class BooleanConditionBlock(private val block: Block): ConditionBlock {

    init {
        TODO("Make sure that return type is boolean when boolean type will be implemented")
    }

    override fun isTrue(core: Core, arguments: Context): Boolean {
        return block.run(core, arguments).data as Boolean
    }

    override val additionalContext: Context
        get() = block.additionalContext
}