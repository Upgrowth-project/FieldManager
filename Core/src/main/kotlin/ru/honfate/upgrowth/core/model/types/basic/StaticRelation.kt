package ru.honfate.upgrowth.core.model.types.basic

import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import kotlin.reflect.KClass

open class StaticRelationType(private val operands: Array<Type>, val isTerminal: Boolean = false): Type {
    override val typeName: String
        get() = "StaticRelation" + _typeGenerics.map { it.second.typeName }
            .joinToString(", ", "<", ">")

    override val typeInherits: Type
        get() = EmptyValue()

    private val _typeGenerics: Array<Pair<String, Type>> = operands.mapIndexed {i, v -> "$i" to v}.toTypedArray()

    override val typeGenerics: Array<Pair<String, Type>>
        get() = _typeGenerics

    override val typeValueClass: KClass<*>
        get() = Boolean::class

    override fun buildTypedValue(data: Any?): TypedValue = StaticRelationValue(
        operands.map {it.buildTypedValue()}.toTypedArray(), if (data is Boolean) data else false
    )

    override fun typeEquals(other: Type): Boolean = other is StaticRelationType && operands.contentEquals(other.operands)
}

class StaticRelationValue(val operands: Array<TypedValue>, private var value: Boolean, isTerminal: Boolean = false):
    StaticRelationType(operands.toList().toTypedArray(), isTerminal),           // Ржу
    TypedValue {

    override var data: Any?
        get() = value
        set(value) {this.value = value as Boolean}

    override fun equals(other: Any?): Boolean = other is StaticRelationValue && typeEquals(other) && value == other.value

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + operands.contentHashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}