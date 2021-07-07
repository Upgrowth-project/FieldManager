package field.animalActionHandler

import dataCreator.playCreator
import field.location.Location
import field.location.animal.BaseAnimal
import field.location.animal.DEFAULT_ANIMAL_NAME
import field.location.animal.PlayerAnimal
import field.location.animal.attribute.Attribute
import logger.logger
import java.util.*

class AnimalActionHandler {

    val DEFAULT_ANIMAL_NEEDS_FOOD: Int = 1
    val DEFAULT_EATEN_ANIMAL_FOOD: Int = 2

    var nextAnimalId: Int = 0

    private fun foodByEatenAnimal(a: BaseAnimal) : Int {

        return DEFAULT_EATEN_ANIMAL_FOOD
    }

    private fun eatAnimal(eatingAnimalId: Int, eatenAnimalId: Int) {

        val loc: Location = playCreator.field.findLocation(eatingAnimalId)!!

        val eating = loc.getAnimal(eatingAnimalId)
        val eaten = loc.getAnimal(eatenAnimalId)

        if (eating is PlayerAnimal)
            eating.eat(foodByEatenAnimal(eaten))
        loc.eatenAnimal(eatenAnimalId)

        logger write "${eating.name} съело ${eaten.name} и не постеснялось"
    }



    fun createNewAnimal(player: Int, name: String =DEFAULT_ANIMAL_NAME) : PlayerAnimal {

        return PlayerAnimal(nextAnimalId++, name, player, DEFAULT_ANIMAL_NEEDS_FOOD)
    }

    fun createNewBotAnimal(name: String =DEFAULT_ANIMAL_NAME) : BaseAnimal {

        return BaseAnimal(nextAnimalId++, name)
    }

    fun migrate(an: BaseAnimal, newLoc: Location) {

        val oldLoc: Location = playCreator.field.findLocation(an)!!
        oldLoc.removeAnimal(an)
        newLoc.addAnimal(an)
    }


}