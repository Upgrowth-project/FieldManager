package ru.honfate.upgrowth.core.api.interpreter.types

import ru.honfate.upgrowth.core.api.field.Location
import kotlin.reflect.KClass

class EmptyType: Type {
    override val typeName = "Empty"
    override val typeInherits: Type?
        get() = null
    override val typeGenerics: Array<Type>
        get() = emptyArray()
    override val typeValueClass: KClass<*>
        get() = Any::class

    override fun setValue(value: Any) {
        throw UnsupportedOperationException("Empty type cannot be accessed")
    }

    override fun getValue(): Any? {
        throw UnsupportedOperationException("Empty type cannot be accessed")
    }
}