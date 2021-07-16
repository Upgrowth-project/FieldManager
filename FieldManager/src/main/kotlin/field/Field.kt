package ru.honfate.upgrowth.fieldmanager.field

import ru.honfate.upgrowth.core.api.field.Animal
import ru.honfate.upgrowth.core.api.field.Card
import ru.honfate.upgrowth.core.api.field.Location
import java.util.*

class Field(
    val locations: Array<Location>,
    private val locationsGraph: Array<Array<Boolean>>,
    val deck: MutableList<Card>
) {

    private fun giveCard(): Card {
        return deck.removeAt(Random().nextInt(deck.size))
    }

    fun findAnimalById(id: Int): Animal {
        for (l in locations)
            for (locAnimal in l.animals)
                if (locAnimal.id == id)
                    return locAnimal

        throw Exception("There is no animal with id=$id")
    }

    private fun deleteAnimalDependencies(an: Animal) {

        // удаляет свойство животного a, которое связано со свойством an
        fun safeDeleteProperties(a: Animal) {
            for (p in a.properties)
                if (p.targetAnimalReference == an.id)
                    // a ссылается на свойство удаляемого животного an
                    a.properties.remove(p)
        }

        for (animal in getAllAnimals())
            safeDeleteProperties(animal)
        // TODO ? можно было проходить только по локации, содержащей удаляемое животное an
    }

    fun getCards(n: Int): Collection<Card> {
        return listOf(Array<Card>(n) { _ -> giveCard() })
    }

    fun findLocation(a: Animal) : Location? {

        for (location in locations)
            if (a in location.animals)
                return location

        return null
    }

    /*fun findAnimalByProperty(property: Property) : Animal {
        for (l in locations)
            for (a in l.animals)
                for (p in a.properties)
                    if (p.id == property.id)
                        a.properties.remove(p)
    }

    fun findLocation(aId: Int) : Location? {

        for (location in locations)
            if (location.animals.contains(aId))
                return location

        return null
    }*/

    fun getAllAnimals() : Array<Animal> {
        val lst = ArrayList<Animal>()
        for (l in locations)
            lst.addAll(listOf(l.animals))

        return Array(lst.size) { i -> lst[i] }
    }

    fun deleteAnimal(an: Animal) {
        for (l in locations)
            if (l.animals.contains(an)) {
                l.animals.remove(an)
                deleteAnimalDependencies(an)
                break
            }
    }

    fun canMove(from: Location, to: Location): Boolean {
        return locationsGraph[from.id][to.id]
    }

}