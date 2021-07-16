package ru.honfate.upgrowth.server

data class PlayerInfo(
    val playerId: Int,
    val name: String,
    val syncAddress: PlayerAddress,
    val asyncAddress: PlayerAddress
)

fun serverInfo(syncAddress: PlayerAddress, asyncAddress: PlayerAddress): PlayerInfo {
    return PlayerInfo(-1, "SERVER", syncAddress, asyncAddress)
}

data class PlayerAddress(
    val ipAddress: String,
    val port: Int
)

data class ChooseCollectionParams<T>(
    val possibilities: Set<T>,
    val range: IntRange
)

data class AskStringParams(
    val invitation: String,
    val maxLen: Int
)

enum class SendingMessageType {
    INFO, CHOOSE_ONE, CHOOSE_ONE_OR_NONE, CHOOSE_SET, CHOOSE_SEQUENCE, ASK_YES_NO, ASK_STRING, PLAY_STATUS,
    PAUSE_GAME, RESUME_GAME, END_GAME, DISCONNECT, LEAVE
}

enum class MessageSync {
    ASYNC, SYNC
}