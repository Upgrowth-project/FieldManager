package ru.honfate.upgrowth.core.model.types

import kotlin.reflect.KClass

interface Type {
    val typeName: String
    val typeInherits: Type?
    val typeGenerics: Array<Type>
    val typeValueClass: KClass<*>

    fun buildTypedValue(data: Any?): TypedValue
}

interface TypedValue: Type {
    var data: Any?
}

interface Functional: Type {
    val functionalReturnType: Type
    fun eval(args: Array<TypedValue>): Type
}

interface Iterable: TypedValue {
    val size: Int
    val iterator: Iterator<*>
}

