package ru.honfate.upgrowth.core.model.types.functional

import ru.honfate.upgrowth.core.model.Callable

class Function(callable: Callable) : FunctionalValue(callable) {
    override val typeName: String
        get() = functionalArgTypes.joinToString(", ", "(", ") -> $functionalReturnType")
                { it.typeName }
}