package ru.honfate.upgrowth.core.model.types.functional

import ru.honfate.upgrowth.core.model.Callable
import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.exception.TypeMismatchException
import ru.honfate.upgrowth.core.model.kb.matcher.Matcher
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.types.basic.BooleanValue
import ru.honfate.upgrowth.core.model.types.basic.StaticRelationType

class Relation(val relationName: String,
               callable: Callable,
               val isTerminal: Boolean = false,
               val destructionMatcher: Matcher?) : FunctionalValue(callable) {
    init {
        if (!callable.body.returnType.typeEquals(BooleanValue()))
            throw TypeMismatchException("Relation body should have Boolean return value")
    }

    override val typeName: String
        get() = relationName + functionalArgTypes.joinToString(" x ", "(", ")") { it.typeName }

    private val _functionalReturnType: Type = StaticRelationType(relationName, callable.argTypes, isTerminal)

    override fun run(core: Core, args: Array<TypedValue>): TypedValue {
        return _functionalReturnType.buildTypedValue(
            super.run(core, args)
        )
    }

    val shouldBeCached: Boolean
    get() = destructionMatcher != null

    override val functionalReturnType: Type
        get() = _functionalReturnType
}