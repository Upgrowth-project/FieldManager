package ru.honfate.upgrowth.core.model.types.functional

import ru.honfate.upgrowth.core.model.Callable
import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.exception.RuntimeErrorException
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.types.basic.EmptyValue
import kotlin.reflect.KClass


interface FunctionalType: Type {
    val functionalReturnType: Type
    val functionalArgTypes: Array<Type>

    @Deprecated("Functional types are not generalized in runtime")
    override val typeGenerics: Array<Pair<String, Type>>
        get() = emptyArray()

    override val typeInherits: Type?
        get() = EmptyValue()

    @Deprecated("Functional types cannot be built")
    override fun buildTypedValue(data: Any?): TypedValue =
        throw RuntimeErrorException("Functional types cannot be built")

    @Deprecated("Functional types cannot be accessed")
    override val typeValueClass: KClass<*>
        get() = throw RuntimeErrorException("Functional types cannot be accessed")

}

abstract class FunctionalValue(val callable: Callable): FunctionalType, TypedValue {
    private val argsTypes = callable.args.map { it.type }.toTypedArray()

    override val functionalReturnType: Type
    get() = callable.body.returnType

    override val functionalArgTypes: Array<Type>
        get() = argsTypes

    @Deprecated("Data of functional value cannot be accessed")
    override var data: Any?
        get() = throw RuntimeErrorException("Data of functional value cannot be accessed")
        set(value) { throw RuntimeErrorException("Data of functional value cannot be accessed") }

    override fun typeEquals(other: Type): Boolean = other is FunctionalValue &&
            argsTypes.contentEquals(other.argsTypes) &&
            functionalReturnType.typeEquals(other.functionalReturnType)

    open fun run(core: Core, args: Array<TypedValue>) = callable.run(core, args)

    @Deprecated("Functional types cannot be compared")
    override fun equals(other: Any?): Boolean = throw RuntimeErrorException("Functional types cannot be compared")

    override fun hashCode(): Int {
        var result = functionalReturnType.hashCode()
        result = 31 * result + functionalArgTypes.contentHashCode()
        return result
    }

}