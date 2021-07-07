package ru.honfate.upgrowth.core.api.field

data class Property(
    val id: Int,
    val name: String,
    val foodIncrement: Int
)

data class Card(
    val id: Int,
    val properties: Array<Property>
)

data class Player(
    val id: Int,
    val name: String,
    val hand: Array<Card>,
    val animals: Array<Animal>
)

data class Animal(
    val id: Int,
    val name: Int,
    val isActive: Boolean,
    val properties: Array<Property>,
    val location: Location,
    val owner: Player?,
    val foodNeeded: Int, // Количество еды, необходимое для выживания
    val foodGot: Int     // Количество еды, которое животное получило
)

data class Location(
    val id: Int,
    val name: String,
    val animals: Array<Animal>,
    val hasWater: Boolean,
    val hasLand: Boolean
)
