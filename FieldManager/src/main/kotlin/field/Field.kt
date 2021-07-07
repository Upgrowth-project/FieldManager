package field

import dataCreator.locationDataCreator
import field.animalActionHandler.AnimalActionHandler
import field.location.Location
import field.location.animal.BaseAnimal
import player.Card
import player.Player

class Field(numberOfPlayers: Int) {

    val locations: Array<Location> = locationDataCreator.createLocations()
    val locationsGraph: Array<Array<Boolean>> = locationDataCreator.createLocationsGraph()
    val animalActionHandler: AnimalActionHandler = AnimalActionHandler()


    fun findLocation(a: BaseAnimal) : Location? {

        for (location in locations)
            if (location.contains(a))
                return location

        return null
    }

    fun findLocation(aId: Int) : Location? {

        for (location in locations)
            if (location.contains(aId))
                return location

        return null
    }


}