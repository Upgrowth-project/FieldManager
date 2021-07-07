package player

import field.location.animal.BaseAnimal
import field.location.animal.attribute.Attribute

class EvolutionPlayerAction(pl: Int, animalParams: Array<BaseAnimal>,
                            val card: Card, val pat: EvolutionPlayerActionType, val attr: Attribute) :
    BasePlayerAction(pl, animalParams) {
}