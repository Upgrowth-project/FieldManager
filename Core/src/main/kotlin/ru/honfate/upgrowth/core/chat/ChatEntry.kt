package ru.honfate.upgrowth.core.chat

import ru.honfate.upgrowth.core.api.field.Player
import java.time.Instant

data class ChatEntry (
    val timeStamp: Instant,
    val author: Player,
    val message: String,
)