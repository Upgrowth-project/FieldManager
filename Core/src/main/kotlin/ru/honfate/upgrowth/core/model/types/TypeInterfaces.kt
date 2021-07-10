package ru.honfate.upgrowth.core.model.types

import kotlin.reflect.KClass

interface Type {
    val typeName: String
    val typeInherits: Type?
    val typeGenerics: Array<Pair<String, Type>>
    val typeValueClass: KClass<*>

    fun buildTypedValue(data: Any? = null): TypedValue
    fun typeEquals(other: Type): Boolean
}

interface TypedValue: Type {
    var data: Any?

    override fun equals(other: Any?): Boolean
}
