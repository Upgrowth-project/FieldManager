package ru.honfate.upgrowth.client.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.honfate.upgrowth.core.api.field.Player
import ru.honfate.upgrowth.core.api.io.PlayStatus
import ru.honfate.upgrowth.core.api.io.ServerMessage
import ru.honfate.upgrowth.core.chat.ChatEntry
import ru.honfate.upgrowth.message.Message
import ru.honfate.upgrowth.message.Messenger
import ru.honfate.upgrowth.server.*

// Класс предназначен для взаимодействия пользователя с сервером
class ClientManager (
    val player: Player,
    private val serverInfo: PlayerInfo) {

    private val input: PlayerAsyncInput = TODO()
    private val requesterInput: PlayerInputRequested = TODO()
    private val serverOutput: ServerOutput = TODO()
    private val messenger: Messenger = Messenger(player.name)

    suspend fun run() = coroutineScope {
        launch(Dispatchers.IO) { asyncListen() } // слушать сообщения от сервера
        runAsyncInput() // слушать асинхронный ввод пользователя
    }

    // статус игры
    private suspend fun status(): Boolean {
        messenger.send("", SendingMessageType.PLAY_STATUS, serverInfo)
        return (messenger.waitForResponse(serverInfo) as Message).obj as PlayStatus == PlayStatus.IS_ON
    }

    private suspend fun runAsyncInput() {
        while (status()) {
            input.asyncMenu(this)
        }
    }

    private fun disconnect() {
        // TODO()
    }

    private suspend fun asyncListen() {
        while (status()) {
            val serverMessage = messenger.asyncListening(serverInfo)
            makeAnswer(serverMessage)
        }
    }

    private fun makeAnswer(msg: Message) {
        when (msg.type) {
            SendingMessageType.INFO -> serverOutput.printMessage(msg.obj as ServerMessage)

            SendingMessageType.CHOOSE_ONE -> messenger.send(requesterInput.chooseOne(msg.obj as Set<*>),
                SendingMessageType.CHOOSE_ONE, serverInfo, MessageSync.SYNC)

            SendingMessageType.CHOOSE_ONE_OR_NONE -> messenger.send(
                requesterInput.chooseOneOrNone(msg.obj as Set<*>),
                SendingMessageType.CHOOSE_ONE_OR_NONE, serverInfo, MessageSync.SYNC)

            SendingMessageType.CHOOSE_SET -> messenger.send(requesterInput.chooseSet(
                    (msg.obj as ChooseCollectionParams<*>).possibilities,
                    (msg.obj as ChooseCollectionParams<*>).range.first,
                    (msg.obj as ChooseCollectionParams<*>).range.last
                ), SendingMessageType.CHOOSE_SET, serverInfo, MessageSync.SYNC)

            SendingMessageType.CHOOSE_SEQUENCE -> {} // раз нигде не используется, то и фиг с ним

            SendingMessageType.ASK_YES_NO -> messenger.send(requesterInput.yesNo(msg.obj as String),
                SendingMessageType.ASK_YES_NO, serverInfo, MessageSync.SYNC)

            SendingMessageType.ASK_STRING -> messenger.send(requesterInput.inputString(
                    (msg.obj as AskStringParams).invitation,
                    (msg.obj as AskStringParams).maxLen
                ), SendingMessageType.ASK_STRING, serverInfo, MessageSync.SYNC)

            SendingMessageType.PAUSE_GAME -> serverOutput.pauseGame(msg.obj as Int)

            SendingMessageType.RESUME_GAME -> serverOutput.resumeGame()

            SendingMessageType.END_GAME -> serverOutput.endGame()

            SendingMessageType.DISCONNECT -> disconnect()
            SendingMessageType.LEAVE -> serverOutput.leave(msg.obj as String)
        }
    }

    fun newChatEntry(message: ChatEntry){
        messenger.send(message, SendingMessageType.INFO, serverInfo)
    }

    fun requestedPause(){
        messenger.send("", SendingMessageType.PAUSE_GAME, serverInfo)
    }

    fun resumedGame(){
        messenger.send("", SendingMessageType.RESUME_GAME, serverInfo)
    }

    fun cleanExited(){
        leave()
    }

    @Deprecated("шутка")
    fun rageQuit(){
        // TODO добавить шутку
        leave()
    }

    fun disconnected(){
        messenger.send("", SendingMessageType.DISCONNECT, serverInfo)
    }

    private fun leave(){
        messenger.send(player.name, SendingMessageType.LEAVE, serverInfo)
    }
}