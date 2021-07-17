package ru.honfate.upgrowth.core.api.io

import ru.honfate.upgrowth.core.api.field.Card
import ru.honfate.upgrowth.core.api.field.Location
import ru.honfate.upgrowth.core.api.field.Player

enum class PlayerAnswers {
    Yes, No, Cancel
}

enum class MessageType {
    Error, Warning, Info,
    GameStarted, GameEnded, GamePaused, GameResumed
}

enum class PlayStatus {
    IS_ON, ENDED
}

data class GameInfo(
    val locations: Array<Location>,      // все локации, то есть вся информация о том, что на столе
    val playerHand: Array<Card>,         // карты на руке у игрока
    val amountsOfCard: Map<Player, Int>  // количество карт других игроков
)

data class ServerMessage (
    val messageType: MessageType,
    val message: String
)