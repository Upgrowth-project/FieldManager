package ru.honfate.upgrowth.core.model.types.functional

import ru.honfate.upgrowth.core.model.Callable
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue


interface FunctionalType: Type {
    val functionalReturnType: Type
    val functionalArgTypes: Array<Type>
    val functionalIsGenerified: Boolean
}

interface FunctionalValue: FunctionalType, TypedValue {
    val callable: Callable
}