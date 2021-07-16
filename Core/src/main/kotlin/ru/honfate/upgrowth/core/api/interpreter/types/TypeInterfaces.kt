package ru.honfate.upgrowth.core.api.interpreter.types

import kotlin.reflect.KClass

interface Type {
    val typeName: String
    val typeInherits: Type?
    val typeGenerics: Array<Type>
    val typeValueClass: KClass<*>

    fun setValue(value: Any)
    fun getValue(): Any?
}

interface Functional: Type {
    val functionalReturnType: Type
    fun eval(args: Array<Type>): Type
}

interface Iterable: Type {
    val size: Int
    val iterator: Iterator<*>
}