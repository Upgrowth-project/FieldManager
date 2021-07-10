package ru.honfate.upgrowth.core.model.types.basic

import ru.honfate.upgrowth.core.api.field.Location
import ru.honfate.upgrowth.core.api.field.Player
import ru.honfate.upgrowth.core.api.field.Property
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class NaturalValue(private var value: Any? = null,
                   private val _typeValueClass: KClass<*>) : TypedValue {

    private val _typeName: String = _typeValueClass.simpleName!!

    override var data: Any?
        get() = value
        set(value) {
            this.value = value
        }
    override val typeName: String
        get() = _typeName
    override val typeInherits: Type
        get() = EmptyValue()
    override val typeGenerics: Array<Pair<String, Type>>
        get() = emptyArray()
    override val typeValueClass: KClass<*>
        get() = _typeValueClass

    override fun buildTypedValue(data: Any?): TypedValue = build(_typeValueClass.safeCast(data))

    override fun typeEquals(other: Type): Boolean = other is NaturalValue && other._typeName == _typeName

    override fun equals(other: Any?): Boolean = other is NaturalValue && typeEquals(other) && data == other.data

    override fun hashCode(): Int {
        return _typeName.hashCode()
    }

    companion object {
        inline fun <reified T> build(value: T? = null) = NaturalValue(value, T::class)
    }
}

fun BooleanValue(data: Boolean? = null) = NaturalValue.build(data)
fun IntValue(data: Int? = null) = NaturalValue.build(data)
fun PlayerValue(data: Player? = null) = NaturalValue.build(data)
fun LocationValue(data: Location? = null) = NaturalValue.build(data)
fun PropertyValue(data: Property? = null) = NaturalValue.build(data)

