package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.Core

interface Block {
    val returnType: Type
    // Дополнительные переменные из этого блока, которые должны появиться на уровень выше
    val additionalContext: Context

    fun run(core: Core, arguments: Context): TypedValue
}