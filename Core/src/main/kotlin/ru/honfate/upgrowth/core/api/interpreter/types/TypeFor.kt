package ru.honfate.upgrowth.core.api.interpreter.types

import kotlin.reflect.KClass

class TypeFor<T>(private var data: T): Type {
    override val typeName = data!!::class.simpleName!!
    override val typeInherits: Type
        get() = EmptyType()
    override val typeGenerics: Array<Type>
        get() = emptyArray()
    override val typeValueClass: KClass<*>
        get() = data!!::class

    override fun setValue(value: Any) {
        data = value as T
    }

    override fun getValue(): Any? = data
}