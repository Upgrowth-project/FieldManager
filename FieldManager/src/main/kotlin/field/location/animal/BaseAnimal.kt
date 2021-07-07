package field.location.animal

import field.location.animal.attribute.Attribute
import java.util.*

const val DEFAULT_ANIMAL_NAME: String = "Эволюционирующее животное"

open class BaseAnimal(val id: Int, var name: String = DEFAULT_ANIMAL_NAME) {
    val attributes: ArrayList<Attribute> = ArrayList()
    var isActive: Boolean = true

    fun addAttribute(a: Attribute) {
        attributes.add(a)
    }

    fun removeAttribute(a: Attribute) {
        attributes.remove(a)
    }

    // returns collection of attributes of the animal, which are allowed by filter
    fun getAttributes(filter: (attr: Attribute) -> Boolean) : Collection<Attribute> {

        val res: MutableCollection<Attribute> = HashSet<Attribute>()

        for (attr in attributes)
            if (filter(attr))
                res.add((attr))

        return res
    }
}