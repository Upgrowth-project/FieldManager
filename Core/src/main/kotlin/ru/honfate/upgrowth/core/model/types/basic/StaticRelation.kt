package ru.honfate.upgrowth.core.model.types.basic

import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import kotlin.reflect.KClass

open class StaticRelationType(private val operands: Array<Type>): Type {
    override val typeName: String
        get() = "StaticRelation"

    override val typeInherits: Type
        get() = EmptyValue()

    private val _typeGenerics: Map<String, Type> = operands.mapIndexed {i, v -> "$i" to v}.toMap()

    override val typeGenerics: Map<String, Type>
        get() = _typeGenerics

    override val typeValueClass: KClass<*>
        get() = Boolean::class

    override fun buildTypedValue(data: Any?): TypedValue = StaticRelationValue(
        operands.map {it.buildTypedValue()}.toTypedArray(), if (data is Boolean) data else false
    )

    override fun equals(other: Any?): Boolean = other is StaticRelationType && operands.contentEquals(other.operands)

    override fun hashCode(): Int {
        return operands.contentHashCode()
    }
}

class StaticRelationValue(operands: Array<TypedValue>, private var value: Boolean):
    StaticRelationType(operands.toList().toTypedArray()),           // Ржу
    TypedValue {

    override var data: Any?
        get() = value
        set(value) {this.value = value as Boolean}
}