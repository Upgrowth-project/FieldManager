package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.Variable
import ru.honfate.upgrowth.core.model.exception.TypeMismatchException
import ru.honfate.upgrowth.core.model.types.basic.EmptyValue
import ru.honfate.upgrowth.core.model.types.collection.Iterable
import ru.honfate.upgrowth.core.model.types.Type

// Условие для range-based циклов в стиле Java:
// for (<variable>: <iterable>)
class IterableConditionBlock(private val entryVariable: Variable,
                             private val initBlock: Block): ConditionBlock {
    private val collectionType: Type
    init {
        if (initBlock.returnType !is Iterable)
            throw TypeMismatchException("Expected iterable type, got ${initBlock.returnType.typeName}")
        collectionType = initBlock.returnType.typeGenerics.first().second
        if (entryVariable.type !is EmptyValue && collectionType.typeEquals(entryVariable.type))
            throw TypeMismatchException(entryVariable.type, collectionType)
    }

    private lateinit var iterator: Iterator<*>

    override fun isTrue(core: Core, arguments: Context): Boolean {
        iterator = (initBlock.run(core, arguments) as Iterable).iterator
        return if (iterator.hasNext()) {
            entryVariable.set(collectionType.buildTypedValue(iterator.next()))
            true
        } else false
    }

    override val additionalContext: Context
        get() = contextOf(entryVariable)
}