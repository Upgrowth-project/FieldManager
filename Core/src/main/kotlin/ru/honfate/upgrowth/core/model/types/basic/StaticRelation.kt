package ru.honfate.upgrowth.core.model.types.basic

import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import kotlin.reflect.KClass

open class StaticRelationType(val name: String,
                              private val operands: Array<Type>,
                              val isTerminal: Boolean = false): Type {
    override val typeName: String
        get() = name + _typeGenerics.joinToString(", ", "<", ">") { it.second.typeName }

    override val typeInherits: Type
        get() = EmptyValue()

    private val _typeGenerics: Array<Pair<String, Type>> = operands.mapIndexed {i, v -> "$i" to v}.toTypedArray()

    override val typeGenerics: Array<Pair<String, Type>>
        get() = _typeGenerics

    override val typeValueClass: KClass<*>
        get() = Boolean::class

    override fun buildTypedValue(data: Any?): TypedValue = StaticRelationValue(
        name,
        operands.map {it.buildTypedValue()}.toTypedArray(),
        if (data is Boolean) data else false
    )

    override fun typeEquals(other: Type): Boolean = other is StaticRelationType &&
            name == other.name &&
            operands.contentEquals(other.operands)
}

class StaticRelationValue(name: String,
                          val operands: Array<TypedValue>,
                          var value: Boolean, isTerminal: Boolean = false):
    StaticRelationType(name, operands.toList().toTypedArray(), isTerminal),           // Ржу
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