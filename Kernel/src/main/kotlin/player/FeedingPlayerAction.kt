package player

import field.location.animal.BaseAnimal
import field.location.animal.attribute.Attribute

class FeedingPlayerAction(pl: Int, animalParams: Array<BaseAnimal>,
                          val pat: FeedingPlayerActionType, val attributeParams: Array<Attribute>) :
    BasePlayerAction(pl, animalParams) {
}