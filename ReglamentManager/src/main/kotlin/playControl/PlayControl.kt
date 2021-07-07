package playControl

import dataCreator.locationDataCreator
import dataCreator.playCreator
import player.Card
import player.Player
import rules.reglamentRules

class PlayControl(val numberOfPlayers: Int) {

    val START_NUMBER_OF_CARDS = 6
    val field = playCreator.field

    var phase: Phase = Phase.EVOLUTION
    var roundStartsBy: Int = 0
    val cards: MutableCollection<Card> = locationDataCreator.createCards()
    val players: Array<Player>

    init {
        var id = 0
        players = Array(numberOfPlayers
        ) {
            Player(id++)
        }
    }

    private fun nextPhase() : Phase {
        return when(phase) {
            Phase.EVOLUTION -> Phase.FOOD_ACSERT
            Phase.FOOD_ACSERT -> Phase.FEEDING
            Phase.FEEDING -> Phase.DYING
            Phase.DYING -> Phase.EVOLUTION
        }
    }

    fun run() {

        while (!reglamentRules.gameEnded(this)) {


        }
    }
}