package ru.honfate.upgrowth.core.model.types.functional

import ru.honfate.upgrowth.core.model.Callable
import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.types.basic.EmptyValue

class Script(callable: Callable) : FunctionalValue(callable) {
    override val functionalReturnType: Type
        get() = EmptyValue()

    override val typeName: String
        get() = functionalArgTypes.joinToString(", ", "/", "/") { it.typeName }

    override fun run(core: Core, args: Array<TypedValue>): TypedValue {
        super.run(core, args)
        return EmptyValue()
    }
}