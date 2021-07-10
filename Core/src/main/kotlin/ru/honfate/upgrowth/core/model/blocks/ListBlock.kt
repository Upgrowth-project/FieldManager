package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.Variable
import ru.honfate.upgrowth.core.model.types.basic.EmptyValue
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue

// Список для передачи в функцию и создания коллекций
class ListBlock(private val list: Array<Block>): Block {
    override val returnType: Type
        get() = EmptyValue()

    private fun listItemName(index: Int) = "__list_val$index"

    // Дополнительным контекстом являются все значения элементов списка
    private val _additionalContext: Context
        get() = list
            .mapIndexed { i, block -> Variable(block.returnType, listItemName(i)) }
            .toContext()

    val size: Int = list.size

    val types = list.map { it.returnType }.toTypedArray()

    override val additionalContext: Context
    get() = _additionalContext

    val values = _additionalContext.keys
        .sorted()
        .map {_additionalContext[it]!!.typedValue}
        .toTypedArray()

    override fun run(core: Core, arguments: Context): TypedValue {
        val cumulativeContext = arguments.toMutableMap()
        list.forEachIndexed { index, block ->
            _additionalContext[listItemName(index)]!!.set(block.run(core, cumulativeContext))
            cumulativeContext += block.additionalContext
        }
        return EmptyValue()
    }
}