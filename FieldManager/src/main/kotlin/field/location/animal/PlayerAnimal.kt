package field.location.animal

import field.location.animal.attribute.Attribute
import java.util.*

class PlayerAnimal(id: Int, name: String, val player: Int,
                   var needFood: Int) :
    BaseAnimal(id, name) {

    private var ate: Int = 0
    private var savedFood: Int = 0


    fun eat(food: Int) {

        ate = Integer.min(ate + food, needFood)
    }

    fun saveFood() {

        if (ate <= 0)
            throw Exception("save food exception")

        ate--
        savedFood++
    }

    fun eatSavedFood() {

        if (savedFood <= 0)
            throw Exception("save food exception")

        ate++
        savedFood--
    }

    fun isFed(): Boolean {

        if (ate > needFood)
            throw Exception("Животное съело больше, чем может")
        return needFood == ate
    }
}