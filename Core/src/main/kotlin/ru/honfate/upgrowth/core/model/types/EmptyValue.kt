package ru.honfate.upgrowth.core.model.types

import kotlin.reflect.KClass

class EmptyValue: TypedValue {
    override val typeName = "Empty"
    override val typeInherits: Type?
        get() = null
    override val typeGenerics: Array<Type>
        get() = emptyArray()
    override val typeValueClass: KClass<*>
        get() = Unit::class

    override fun buildTypedValue(data: Any?): TypedValue = this

    override var data: Any?
        get() = throw UnsupportedOperationException("Cannot access empty value")
        set(_) = throw UnsupportedOperationException("Cannot access empty value")
}