package ru.honfate.upgrowth.fieldmanager.field

import ru.honfate.upgrowth.core.api.field.*
import java.lang.Integer.min
import java.util.*

class FieldManager(initializer: FieldManagerInitializer) : CoreFieldInterface {
    private var animalId: Int = 0
    private val ANIMAL_DEFAULT_NAME = "Animal"
    private val DEFAULT_FOOD_NEEDED = 1

    private val field: Field = Field(initializer.locations, initializer.neighboringLocations, initializer.deck)

    override val animals: Array<Animal>
        get() = this.field.getAllAnimals()

    override val players: Array<Player> = initializer.players
        get() = field

    override val locations: Array<Location>
        get() = this.field.locations

    override fun <T> refresh(entity: T): T {
        TODO("Not yet implemented")
    }

    override val deckSize: Int
        get() = this.field.deck.size

    override fun giveCards(player: Player, number: Int) {
        player.hand.addAll(field.getCards(number))
    }

    override fun amountOfCards(player: Player, location: Location): Int {
        return location.goodsFunctions.growthCards(location)
    }

    override fun addAnimal(location: Location, player: Player): Animal {
        val animal = Animal(animalId++, ANIMAL_DEFAULT_NAME, true, HashSet<Property>(),
            player, DEFAULT_FOOD_NEEDED, 0)
        location.animals.add(animal)
        player.animals.add(animal)
        return animal
    }

    override fun deleteAnimal(animal: Animal) {
        field.deleteAnimal(animal)
        if (animal.owner != null)
            animal.owner.animals.remove(animal)
    }

    override fun moveAnimal(animal: Animal, destination: Location) {
        field.deleteAnimal(animal)
        destination.animals.add(animal)
        TODO("Парная миграция, удаление парных свойств")
    }

    override fun getLocation(animal: Animal): Location {
        return field.findLocation(animal)!!
    }

    override fun addProperty(property: Property, animal: Animal) {
        animal.properties.add(property)
    }

    override fun deleteProperty(property: Property, animal: Animal) {
        animal.properties.remove(property)
        if (property.targetAnimalReference != null) {
            val otherAnimal = field.findAnimalById(property.targetAnimalReference)
            for (p in otherAnimal.properties)
                if (p.targetAnimalReference != null && p.targetAnimalReference == animal.id)
                    otherAnimal.properties.remove(p)
        }
    }

    override fun generateFood(location: Location) {
        location.food = location.goodsFunctions.growthFood(location)
    }

    override fun food(location: Location): Int {
        return location.food
    }

    override fun setFood(location: Location, number: Int) {
        location.food = number
    }

    override fun setFood(animal: Animal, number: Int) {
        animal.foodGot = min(number, animal.foodNeeded)
    }

    override fun canMove(from: Location, to: Location): Boolean {
        return field.canMove(from, to)
    }
}