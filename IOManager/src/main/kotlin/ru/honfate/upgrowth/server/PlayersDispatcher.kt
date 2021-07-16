package ru.honfate.upgrowth.server

import ru.honfate.upgrowth.core.api.field.Player
import ru.honfate.upgrowth.core.api.io.*
import ru.honfate.upgrowth.core.chat.ChatEntry

class PlayersDispatcher(serverName: String,
                        playerInfs: Array<PlayerInfo>):
    PlayerInputRequestInterface, ServerOutputInterface {

    override var defaultTimeout: Int = 5
    override var player: Player = TODO() // чё
    override var timeOut: Int = defaultTimeout

    private val playerManagers: Collection<PlayerManager> =
        List(playerInfs.size) { i -> PlayerManager(serverName, playerInfs[i]) }

    private val playerInput: PlayerInputInterface = TODO()


    private fun findManager(actor: Player): PlayerManager {
        return playerManagers.find { pm -> pm.playerInfo.playerId == actor.id } !!
    }

    override suspend fun <T> chooseOne(possibilities: Set<T>, actor: Player, timeout: Int): T {
        return findManager(actor).chooseOne(possibilities, timeout)
    }

    override suspend fun <T> chooseOneOrNone(possibilities: Set<T>, actor: Player, timeout: Int): T? {
        return findManager(actor).chooseOneOrNone(possibilities, timeout)
    }

    override suspend fun <T> chooseSet(
        possibilities: Set<T>,
        actor: Player,
        timeout: Int,
        minNumber: Int,
        maxNumber: Int
    ): Set<T> {
        return findManager(actor).chooseSet(possibilities, timeout, minNumber, maxNumber)
    }

    override suspend fun <T> chooseSequence(
        possibilities: Set<T>,
        actor: Player,
        timeout: Int,
        minNumber: Int,
        maxNumber: Int
    ): Array<T> {
        return findManager(actor).chooseSequence(possibilities, timeout, minNumber, maxNumber)

    }

    override suspend fun yesNo(actor: Player, invitation: String, timeout: Int): PlayerAnswers {
        return findManager(actor).yesNo(invitation, timeout)
    }

    override suspend fun inputString(actor: Player, invitation: String, maxLength: Int): String {
        return findManager(actor).inputString(invitation, maxLength)
    }

    override fun sendGameInfo(destination: Player, gameInfo: GameInfo) {
        findManager(destination).sendGameInfo(gameInfo)
    }

    override fun updateChat(chat: Array<ChatEntry>) {
        playerManagers.forEach {pm -> pm.updateChat(chat)}
    }

    override fun sendMessage(destination: Player, message: ServerMessage) {
        findManager(destination).sendMessage(message)
    }

    override fun pauseGame(timeout: Int?) {
        playerManagers.forEach {pm -> pm.pauseGame(timeout)}
    }

    override fun resumeGame() {
        playerManagers.forEach {pm -> pm.resumeGame()}
    }

    override fun endGame() {
        playerManagers.forEach {pm -> pm.endGame()}
    }
}