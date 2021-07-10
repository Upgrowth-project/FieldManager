package ru.honfate.upgrowth.core.model.types.collection

import ru.honfate.upgrowth.core.model.exception.RuntimeErrorException
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.types.basic.StaticRelationValue
import kotlin.reflect.KClass

class MapValue(private val keyType: Type, private val valueType: Type): Iterable {
    private val mutableMap = emptyMap<TypedValue, TypedValue>().toMutableMap()

    override val size: Int
        get() = mutableMap.size
    override val iterator: Iterator<*>
        get() = mutableMap.iterator()

    override fun initialize(initList: Array<TypedValue>) {
        mutableMap.clear()
        initList.forEach { if (it !is StaticRelationValue || it.operands.size != 2)
            throw RuntimeErrorException("Map should be initialized with <key, val> pairs")
            mutableMap[it.operands.first()] = it.operands.last()
        }
    }

    override fun get(where: TypedValue): TypedValue = mutableMap[where]!!

    override fun set(what: TypedValue, where: TypedValue) {
        mutableMap[where] = what
    }

    override fun remove(where: TypedValue) {
        mutableMap.remove(where)
    }

    override fun contains(what: TypedValue): Boolean = mutableMap.contains(what)

    override var data: Any?
        get() = mutableMap
        set(value) {
            mutableMap.clear()
            mutableMap.putAll(value as Map<TypedValue, TypedValue>)
        }

    override fun equals(other: Any?): Boolean = other is MapValue && typeEquals(other) && mutableMap == other.mutableMap

    override val typeName: String
        get() = "Map<${keyType.typeName}, ${valueType.typeName}>"
    override val typeGenerics: Array<Pair<String, Type>>
        get() = arrayOf("K" to keyType, "V" to valueType)
    override val typeValueClass: KClass<*>
        get() = mutableMap::class

    override fun buildTypedValue(data: Any?): TypedValue = MapValue(keyType, valueType)

    override fun typeEquals(other: Type): Boolean = other is MapValue && keyType.typeEquals(other.keyType) &&
            valueType.typeEquals(other.valueType)

    override fun hashCode(): Int {
        var result = keyType.hashCode()
        result = 31 * result + valueType.hashCode()
        result = 31 * result + mutableMap.hashCode()
        return result
    }
}