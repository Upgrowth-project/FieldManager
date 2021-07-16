package ru.honfate.upgrowth.server

import ru.honfate.upgrowth.core.api.field.Player
import ru.honfate.upgrowth.core.api.io.*
import ru.honfate.upgrowth.core.chat.ChatEntry
import ru.honfate.upgrowth.message.Message
import ru.honfate.upgrowth.message.Messenger

// класс представляет собой интерфейс взаимодействия с пользователем,
// предназначенный для отправки ему сообщений и получения ответов
class PlayerManager(
    private val serverName: String, // имя сервера
    val playerInfo: PlayerInfo, // информация об игроке, менеджером которого является данный объект
    private val player: Player,
    private val playerInput: PlayerInputInterface
) {
    private val messenger: Messenger = Messenger(serverName)

    private suspend fun asyncListen() {
        while (playerInput.getPlayStatus() == PlayStatus.IS_ON) {
            val clientMessage = messenger.asyncListening(playerInfo)
            makeAnswer(clientMessage)
        }
    }

    private fun makeAnswer(msg: Message) {
        // ответить на сообщение
        when(msg.type) {
            SendingMessageType.PLAY_STATUS -> messenger.send(
                playerInput.getPlayStatus(),
                SendingMessageType.PLAY_STATUS,
                playerInfo, MessageSync.SYNC)

            SendingMessageType.PAUSE_GAME -> playerInput.requestedPause(player)

            SendingMessageType.RESUME_GAME -> playerInput.requestedPause(player)

            SendingMessageType.DISCONNECT -> playerInput.disconnected(player)

            SendingMessageType.LEAVE -> playerInput.cleanExited(player)
        }
    }

    suspend fun <T> chooseOne(possibilities: Set<T>, timeout: Int): T {
        messenger.send(possibilities, SendingMessageType.CHOOSE_ONE, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as T
    }

    suspend fun <T> chooseOneOrNone(possibilities: Set<T>, timeout: Int): T? {
        messenger.send(possibilities, SendingMessageType.CHOOSE_ONE_OR_NONE, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as? T
    }

    suspend fun <T> chooseSet(
        possibilities: Set<T>,
        timeout: Int,
        minNumber: Int,
        maxNumber: Int
    ): Set<T> {
        messenger.send(ChooseCollectionParams<T>(possibilities, minNumber..maxNumber),
            SendingMessageType.CHOOSE_SET, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as Set<T>
    }

    suspend fun <T> chooseSequence(
        possibilities: Set<T>,
        timeout: Int,
        minNumber: Int,
        maxNumber: Int
    ): Array<T> {
        messenger.send(ChooseCollectionParams<T>(possibilities, minNumber..maxNumber),
            SendingMessageType.CHOOSE_SEQUENCE, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as Array<T>
    }

    suspend fun yesNo(invitation: String, timeout: Int): PlayerAnswers {
        messenger.send(invitation, SendingMessageType.ASK_YES_NO, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as PlayerAnswers
    }

    suspend fun inputString(invitation: String, maxLength: Int): String {
        messenger.send(AskStringParams(invitation, maxLength), SendingMessageType.ASK_STRING, playerInfo)
        return messenger.waitForResponse(playerInfo) as String
    }

    fun sendGameInfo(gameInfo: GameInfo) {
        messenger.send(gameInfo, SendingMessageType.INFO, playerInfo)
    }

    fun updateChat(chat: Array<ChatEntry>) {
        messenger.send(chat, SendingMessageType.INFO, playerInfo)
    }

    fun sendMessage(message: ServerMessage) {
        messenger.send(message, SendingMessageType.INFO, playerInfo)
    }

    fun pauseGame(timeout: Int?) {
        messenger.send(timeout!!, SendingMessageType.PAUSE_GAME, playerInfo)
    }

    fun resumeGame() {
        messenger.send("", SendingMessageType.RESUME_GAME, playerInfo)
    }

    fun endGame() {
        messenger.send("", SendingMessageType.END_GAME, playerInfo)
    }
}