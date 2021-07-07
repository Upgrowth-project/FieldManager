package field.location.animal.attribute

data class Attribute(
    val id: Int,
    val name: String,
    val foodChanging: Int,
    val influenceOnLocationGoods: Boolean,
    val possibleActions: Collection<AnimalPossibleAction>,
    val characteristics: Characteristics) {

    fun isProtecting(): Boolean {

        return characteristics.isProtecting
    }

}
