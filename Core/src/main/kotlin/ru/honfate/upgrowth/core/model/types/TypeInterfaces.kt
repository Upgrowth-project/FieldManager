package ru.honfate.upgrowth.core.model.types

import kotlin.reflect.KClass

interface Type {
    val typeName: String
    val typeInherits: Type?
    val typeGenerics: Map<String, Type>
    val typeValueClass: KClass<*>

    fun buildTypedValue(data: Any? = null): TypedValue

    override operator fun equals(other: Any?): Boolean
}

interface TypedValue: Type {
    var data: Any?
}
