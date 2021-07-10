package ru.honfate.upgrowth.core.model.types.basic

import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import kotlin.reflect.KClass

class EmptyValue: TypedValue {
    override val typeName = "Empty"
    override val typeInherits: Type?
        get() = null
    override val typeGenerics: Map<String, Type>
        get() = emptyMap()
    override val typeValueClass: KClass<*>
        get() = Unit::class

    override fun buildTypedValue(data: Any?): TypedValue = this

    override fun equals(other: Any?): Boolean = other is EmptyValue

    override fun hashCode(): Int = 0

    override var data: Any?
        get() = throw UnsupportedOperationException("Cannot access empty value")
        set(_) = throw UnsupportedOperationException("Cannot access empty value")
}