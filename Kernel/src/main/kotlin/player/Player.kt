package player

import java.util.*

class Player(val id: Int) {

    val cards: MutableCollection<Card> = ArrayList<Card>()

    fun getCard(card: Card) {
        cards.add(card)
    }

    fun getCards(_cards: Collection<Card>) {
        cards.addAll(_cards)
    }

    fun removeCard(card: Card) {
        cards.remove(card)
    }
}