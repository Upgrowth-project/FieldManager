package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.exception.RuntimeErrorException
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue

interface ConditionBlock: Block {
    override val returnType: Type
        get() = TODO("Make boolean when boolean type will be implemented")
    fun isTrue(core: Core, arguments: Context): Boolean

    @Deprecated("Condition blocks are not supposed to be ran with 'run' method")
    override fun run(core: Core, arguments: Context): TypedValue =
        throw RuntimeErrorException("Condition blocks are not supposed to be ran with 'run' method")
}