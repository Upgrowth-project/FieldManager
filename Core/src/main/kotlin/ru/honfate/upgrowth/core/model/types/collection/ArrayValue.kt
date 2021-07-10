package ru.honfate.upgrowth.core.model.types.collection

import ru.honfate.upgrowth.core.model.exception.RuntimeErrorException
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.types.basic.IntValue
import kotlin.reflect.KClass

class ArrayValue(private val itemType: Type, initSize: Int = 0): Iterable {
    private val array: ArrayList<TypedValue> = ArrayList(initSize)

    override val size: Int
        get() = array.size
    override val iterator: Iterator<*>
        get() = array.iterator()

    override fun initialize(initList: Array<TypedValue>) {
        array.clear()
        array.addAll(initList)
    }



    override fun get(where: TypedValue): TypedValue {
        assertIntType(where)
        return array[where.data as Int]
    }

    private fun assertIntType(where: TypedValue) {
        if (!where.typeEquals(IntValue())) throw RuntimeErrorException("Only integer can be used as index")
    }

    override fun set(what: TypedValue, where: TypedValue) {
        assertIntType(where)
        array[where.data as Int] = what
    }

    override fun remove(where: TypedValue) {
        assertIntType(where)
        array.removeAt(where.data as Int)
    }

    override fun contains(what: TypedValue): Boolean = array.contains(what)

    override var data: Any?
        get() = array
        set(value) {
            array.clear()
            array.addAll(value as Collection<TypedValue>)
        }

    override fun equals(other: Any?): Boolean = other is ArrayValue && typeEquals(other) &&
            other.array == array

    override val typeName: String
        get() = "Array<${itemType.typeName}>"
    override val typeGenerics: Array<Pair<String, Type>>
        get() = arrayOf("T" to itemType)
    override val typeValueClass: KClass<*>
        get() = array::class

    override fun buildTypedValue(data: Any?): TypedValue = ArrayValue(itemType)

    override fun typeEquals(other: Type): Boolean = other is ArrayValue && itemType.typeEquals(other.itemType)

    override fun hashCode(): Int {
        var result = itemType.hashCode()
        result = 31 * result + array.hashCode()
        return result
    }
}