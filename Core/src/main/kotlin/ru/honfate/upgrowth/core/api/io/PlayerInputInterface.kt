package ru.honfate.upgrowth.core.api.io

import ru.honfate.upgrowth.core.api.field.Player
import ru.honfate.upgrowth.core.chat.ChatEntry

// Методы core (!), которые будет вызывать модуль IO (!)
// в случае асинхронного ввода пользователя
interface PlayerInputInterface {
    fun newChatEntry(newEntry: ChatEntry)
    fun requestedPause(player: Player)
    fun resumedGame(player: Player)
    fun cleanExited(player: Player)
    @Deprecated("шутка") fun rageQuit(player: Player)
    fun disconnected(player: Player)

    fun getPlayStatus(): PlayStatus
}