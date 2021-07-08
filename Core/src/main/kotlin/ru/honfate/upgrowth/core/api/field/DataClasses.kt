package ru.honfate.upgrowth.core.api.field

data class Property(
    val id: Int,
    val name: String,
    val foodIncrement: Int,
    val targetAnimalReference: Int?
)

data class Card(
    val id: Int,
    val properties: Array<Property>
)

data class Player(
    val id: Int,
    val name: String,
    val hand: MutableCollection<Card>,
    val animals: MutableCollection<Animal>
)

data class Animal(
    val id: Int,
    val name: String,
    val isActive: Boolean,
    val properties: MutableCollection<Property>,
    val location: Location,
    val owner: Player?,
    val foodNeeded: Int, // Количество еды, необходимое для выживания
    var foodGot: Int     // Количество еды, которое животное получило
)

data class Location(
    val id: Int,
    val name: String,
    val animals: MutableCollection<Animal>,
    val hasWater: Boolean,
    val hasLand: Boolean,
    val goodsFunctions: GoodsFunctions,

    var food: Int
)

// структура данных, используемая для инициализации FieldManager
data class FieldManagerInitializer(
    val players: Array<Player>,
    val locations: Array<Location>,
    val deck: MutableList<Card>,
    val neighboringLocations: Array<Array<Boolean>>
)
