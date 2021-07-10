package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.exception.CompilationErrorException
import ru.honfate.upgrowth.core.model.types.EmptyValue
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.Core

class SequenceBlock(sequence: Array<Block>): Block {
    private val sequence: Array<Block> = if (sequence.isNotEmpty()) sequence
        else throw CompilationErrorException("Sequence cannot be empty")

    override val returnType: Type
        get() = sequence.last().returnType

    override val additionalContext: Context
        get() = emptyContext()

    override fun run(core: Core, arguments: Context): TypedValue {
        var res: TypedValue = EmptyValue()
        val cumulativeContext = arguments.toMutableMap()
        for (block in sequence) {
            res = block.run(core, cumulativeContext)
            cumulativeContext.putAll(block.additionalContext)
        }
        return res
    }
}