package rules

import playControl.Phase
import playControl.PlayControl

object reglamentRules {

    fun gameEnded(pc: PlayControl) : Boolean {

        return pc.cards.isEmpty() && pc.phase == Phase.EVOLUTION
    }
}