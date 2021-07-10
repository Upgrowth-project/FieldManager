package ru.honfate.upgrowth.core.model.types.functional

import ru.honfate.upgrowth.core.model.Callable
import ru.honfate.upgrowth.core.model.exception.TypeMismatchException
import ru.honfate.upgrowth.core.model.kb.matcher.Matcher
import ru.honfate.upgrowth.core.model.types.basic.BooleanValue

class Relation(callable: Callable, val destructionMatcher: Matcher?) : FunctionalValue(callable) {
    init {
        if (functionalReturnType != BooleanValue())
            throw TypeMismatchException("Relation body should have Boolean return value")
    }

    override val typeName: String
        get() = functionalArgTypes.joinToString(" x ", "(", ")") { it.typeName }
}