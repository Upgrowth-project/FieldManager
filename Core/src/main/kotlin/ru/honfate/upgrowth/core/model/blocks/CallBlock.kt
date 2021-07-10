package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue

class CallBlock: Block {
    override val returnType: Type
        get() = TODO("Not yet implemented")
    override val additionalContext: Context
        get() = TODO("Not yet implemented")

    override fun run(core: Core, arguments: Context): TypedValue {
        TODO("Not yet implemented")
    }
}