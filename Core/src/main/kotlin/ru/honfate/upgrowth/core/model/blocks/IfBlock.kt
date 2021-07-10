package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.types.EmptyValue

class IfBlock(private val condition: BooleanConditionBlock,
              private val trueBranch: Block,
              private val falseBranch: Block) : Block {

    private val _returnType: Type =
        if (trueBranch.returnType.typeName != falseBranch.returnType.typeName ||
            trueBranch.returnType is EmptyValue ||
            falseBranch.returnType is EmptyValue) EmptyValue()
        else trueBranch.returnType

    override val returnType: Type
        get() = _returnType

    override val additionalContext: Context
        get() = emptyContext()

    override fun run(core: Core, arguments: Context): TypedValue {
        val branchContext = arguments + condition.additionalContext
        return if (condition.run(core, arguments).data as Boolean) {
            trueBranch.run(core, branchContext)
        } else {
            falseBranch.run(core, branchContext)
        }
    }
}