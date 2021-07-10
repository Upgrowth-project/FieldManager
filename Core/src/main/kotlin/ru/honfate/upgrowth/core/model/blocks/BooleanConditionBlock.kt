package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.exception.TypeMismatchException
import ru.honfate.upgrowth.core.model.types.basic.NaturalValue

class BooleanConditionBlock(private val block: Block): ConditionBlock {

    init {
        NaturalValue.build<Boolean>().also {
            if (block.returnType != it) throw TypeMismatchException(it, block.returnType)
        }
    }

    override fun isTrue(core: Core, arguments: Context): Boolean {
        return block.run(core, arguments).data as Boolean
    }

    override val additionalContext: Context
        get() = block.additionalContext
}