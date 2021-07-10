package ru.honfate.upgrowth.core.model

import ru.honfate.upgrowth.core.model.blocks.Block
import ru.honfate.upgrowth.core.model.blocks.toContext
import ru.honfate.upgrowth.core.model.exception.RuntimeErrorException
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue

data class Specification(val name: String, val type: Type)

class Callable(val args: Array<Specification>, val body: Block)
{
    fun match(argsGiven: Array<Type>) = args.map{ it.type }.toTypedArray().contentEquals(argsGiven)

    fun run(core: Core, argsGiven: Array<TypedValue>): TypedValue {
        if (!match(argsGiven.toList().toTypedArray()))
            throw RuntimeErrorException("Function arguments does not match specification")
        return body.run(
            core,
            argsGiven
                .mapIndexed {i, value -> Variable(value, args[i].name, value)}
                .toContext()
        )
    }
}