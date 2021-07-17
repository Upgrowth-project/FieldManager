package ru.honfate.upgrowth.core.api.io

import ru.honfate.upgrowth.core.api.field.Player
import ru.honfate.upgrowth.core.chat.ChatEntry

// Методы, которыми core будет пользоваться, когда
// серверу необходимо отправить игроку данные
interface ServerOutputInterface {
    fun sendGameInfo(destination: Player, gameInfo: GameInfo)
    fun updateChat(chat: Array<ChatEntry>)

    fun sendMessage(destination: Player, message: ServerMessage)

    fun pauseGame(timeout: Int?)
    fun resumeGame()
    fun endGame()
}