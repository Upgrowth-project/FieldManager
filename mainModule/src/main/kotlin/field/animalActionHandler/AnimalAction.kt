package field.animalActionHandler

import field.location.Location
import field.location.animal.BaseAnimal
import field.location.animal.attribute.AnimalActionType
import field.location.animal.attribute.Attribute

data class AnimalAction(val actor: BaseAnimal, val source: BaseAnimal? =null,
                        val actionType: AnimalActionType, val location: Location? =null, val attribute: Attribute) {
}