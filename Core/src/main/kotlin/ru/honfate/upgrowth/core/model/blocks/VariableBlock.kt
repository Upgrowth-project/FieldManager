package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.Variable
import ru.honfate.upgrowth.core.model.exception.RuntimeErrorException
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue

class VariableBlock(private val variable: Variable): Block {
    override val returnType: Type
        get() = variable.type
    override val additionalContext: Context
        get() = emptyContext()

    override fun run(core: Core, arguments: Context): TypedValue {
        return arguments[variable.name]?.typedValue ?:
            throw RuntimeErrorException("Unknown variable: ${variable.name}")
    }
}