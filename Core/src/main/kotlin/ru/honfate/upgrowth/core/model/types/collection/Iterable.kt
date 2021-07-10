package ru.honfate.upgrowth.core.model.types.collection

import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.types.basic.EmptyValue

interface Iterable: TypedValue {
    val size: Int
    val iterator: Iterator<*>

    fun initialize(initList: Array<TypedValue>)

    fun get(where: TypedValue): TypedValue
    fun set(what: TypedValue, where: TypedValue)
    fun remove(where: TypedValue)
    fun contains(what: TypedValue): Boolean

    override val typeInherits: Type?
        get() = EmptyValue()


}