package field.location

import field.location.animal.BaseAnimal
import field.location.animal.PlayerAnimal
import field.utils.*
import logger.logger
import java.util.*

class Location(val name: String, topology: LocationTopology, size: Int) {

    val animals: ArrayList<BaseAnimal> = ArrayList()
    val goodsFunctions: GoodsFunctions = makeGoodsFunctions(this)

    fun contains(a: BaseAnimal): Boolean {

        return animals.contains(a)
    }

    fun contains(animalId: Int): Boolean {

        for (a in animals)
            if (a.id == animalId)
                return true
        return false
    }

    fun getAnimal(animalId: Int) : BaseAnimal {

        for (a in animals)
            if (a.id == animalId)
                return a

        throw Exception("error while getting animal")
    }

    fun killUnfedAnimals() {

        for (a in animals)
            if (a is PlayerAnimal && !a.isFed()) {
                animals.remove(a)
                logger write "Животное ${a.name} игрока ${a.player} умирает от голода"
            }
    }

    fun eatenAnimal(animalId: Int) {

        for (a in animals)
            if (a.id == animalId) {
                animals.remove(a)
                return
            }
    }

    fun removeAnimal(an: BaseAnimal) {

        if (!contains(an))
            throw Exception("animal does not live in this location")

        animals.remove(an)
    }

    fun addAnimal(an: BaseAnimal) {
        if (contains(an))
            throw Exception("location already contains this animal")

        animals.add(an)
    }

}