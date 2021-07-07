package field.utils

import field.location.GoodsFunctions
import field.location.Location
import field.location.animal.BaseAnimal

fun mayAttack(a1: BaseAnimal, a2: BaseAnimal) = 0
fun needFood(a: BaseAnimal) = 0
fun mayMove(a: BaseAnimal, oldLocation: Int, newLocation: Int) = 0

fun makeGoodsFunctions(l: Location) : GoodsFunctions {
    var gf: GoodsFunctions = GoodsFunctions()
    // TODO
    return gf
}
