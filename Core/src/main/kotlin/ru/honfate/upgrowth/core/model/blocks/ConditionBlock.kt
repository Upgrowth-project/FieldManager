package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.exception.RuntimeErrorException
import ru.honfate.upgrowth.core.model.types.basic.NaturalValue
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue

interface ConditionBlock: Block {
    override val returnType: Type
        get() = NaturalValue.build<Boolean>()
    fun isTrue(core: Core, arguments: Context): Boolean

    @Deprecated("Condition blocks are not supposed to be ran with 'run' method")
    override fun run(core: Core, arguments: Context): TypedValue =
        throw RuntimeErrorException("Condition blocks are not supposed to be ran with 'run' method")
}