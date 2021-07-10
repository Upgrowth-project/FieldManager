package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue

class ContainerInitializerBlock(private val listBlock: ListBlock): Block {
    override val returnType: Type
        get() = TODO("Complete when containers will be initialize")
    override val additionalContext: Context
        get() = emptyContext()
    override fun run(core: Core, arguments: Context): TypedValue {
        TODO("Not yet implemented")
    }
}