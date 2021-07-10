package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Callable
import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.exception.CompilationErrorException
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue

class CallBlock(private val callable: Callable,
                private val args: ListBlock): Block {

    init {
        if (!callable.match(args.types))
            throw CompilationErrorException("Number of arguments does not match call specification")
    }

    override val returnType: Type
        get() = callable.body.returnType
    override val additionalContext: Context
        get() = emptyContext()

    override fun run(core: Core, arguments: Context): TypedValue {
        args.run(core, arguments)
        return callable.run(core, args.values)
    }
}