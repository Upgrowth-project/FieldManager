package ru.honfate.upgrowth.core.api.interpreter.types

import kotlin.reflect.KClass

class SetType(private var data: MutableSet<Type>, type: Type): Iterable {
    override val size: Int
        get() = data.size
    override val iterator: Iterator<*>
        get() = data.iterator()
    override val typeName: String
        get() = "Set"
    override val typeInherits: Type?
        get() = null
    private val _typeGenerics = arrayOf(type)
    override val typeGenerics: Array<Type>
        get() = _typeGenerics
    override val typeValueClass: KClass<*>
        get() = data::class

    override fun setValue(value: Any) {
        data = value as MutableSet<Type>
    }

    override fun getValue() = data
}