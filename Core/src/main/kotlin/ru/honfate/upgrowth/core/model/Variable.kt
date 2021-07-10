package ru.honfate.upgrowth.core.model

import ru.honfate.upgrowth.core.model.types.EmptyValue
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.exception.TypeMismatchException
import ru.honfate.upgrowth.core.model.types.TypedValue

class Variable(var type: Type, val name: String, initValue: TypedValue? = null) {
    var typedValue: TypedValue = if (initValue == null || initValue is EmptyValue) EmptyValue()
        else if (type.typeName == initValue.typeName || type is EmptyValue) {
            type = initValue
            initValue
    }
        else throw TypeMismatchException(type, initValue)
    private set

    fun get() = typedValue.data

    fun set(newValue: TypedValue) {
        when {
            type is EmptyValue -> {
                typedValue = newValue
                type = newValue
            }
            type.typeName == newValue.typeName -> {
                typedValue = newValue
            }
            else -> throw TypeMismatchException(type, newValue)
        }
    }
}