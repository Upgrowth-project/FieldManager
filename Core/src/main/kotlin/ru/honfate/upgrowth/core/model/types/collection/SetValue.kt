package ru.honfate.upgrowth.core.model.types.collection

import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import kotlin.reflect.KClass

class SetValue(private val itemType: Type): Iterable {

    private val mutableSet: MutableSet<TypedValue> = emptySet<TypedValue>().toMutableSet()

    override val size: Int
        get() = mutableSet.size
    override val iterator: Iterator<*>
        get() = mutableSet.iterator()

    override fun initialize(initList: Array<TypedValue>) {
        mutableSet.clear()
        mutableSet.addAll(initList)
    }

    override fun get(where: TypedValue): TypedValue = mutableSet.find { it.data == where.data }!!

    override fun set(what: TypedValue, where: TypedValue) {
        mutableSet.add(what)
    }

    override fun remove(where: TypedValue) {
        mutableSet.remove(where)
    }

    override fun contains(what: TypedValue): Boolean = mutableSet.contains(what)

    override var data: Any?
        get() = mutableSet
        set(value) {
            mutableSet.clear()
            mutableSet.addAll(value as Collection<TypedValue>)
        }

    override val typeName: String
        get() = "Set<${itemType.typeName}>"

    override val typeGenerics: Array<Pair<String, Type>>
        get() = arrayOf("T" to itemType)

    override val typeValueClass: KClass<*>
        get() = mutableSet::class

    override fun buildTypedValue(data: Any?): TypedValue = SetValue(itemType)

    override fun typeEquals(other: Type): Boolean = other is SetValue && other.itemType.typeEquals(itemType)

    override fun equals(other: Any?): Boolean = other is SetValue && mutableSet == other.mutableSet

    override fun hashCode(): Int {
        var result = itemType.hashCode()
        result = 31 * result + mutableSet.hashCode()
        return result
    }
}