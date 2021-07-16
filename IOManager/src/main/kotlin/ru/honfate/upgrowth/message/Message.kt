package ru.honfate.upgrowth.message

import ru.honfate.upgrowth.server.SendingMessageType
import kotlinx.serialization.Serializable

@Serializable
class Message() {
    var type: SendingMessageType
    var obj: Any?
    var playerName: String

    init {
        type = SendingMessageType.RESUME_GAME
        obj = ""
        playerName = ""
    }

    constructor(_type: SendingMessageType, _obj: Any?, _playerName: String): this() {
        type = _type
        obj = _obj
        playerName = _playerName
    }

    fun toObject(stringValue: String): Message {
        return JSON.parse(Message.serializer(), stringValue)
    }

    fun toJson(message: Message): String {
        // Обратите внимание, что мы вызываем Serializer, который автоматически сгенерирован из нашего класса
        // Сразу после того, как мы добавили аннотацию @Serializer
        return JSON.stringify(Message.serializer(), message)
    }
}
