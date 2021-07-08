package ru.honfate.upgrowth.core.api.field

interface CoreFieldInterface {
    // Возвращается массив со всеми сущностями
    val animals: Array<Animal>
    val players: Array<Player>
    val locations: Array<Location>

    // Передается один из дата-классов из DataClasses.kt
    // по его id возвращается актуальная версия сущности
    fun <T> refresh(entity: T): T

    // Размер колоды
    val deckSize: Int
    // Игроку передается нужное количество карт из колоды
    fun giveCards(player: Player, number: Int)
    // Количество карт, согласно функции благ
    fun amountOfCards(player: Player, location: Location): Int

    fun addAnimal(location: Location, player: Player): Animal
    fun deleteAnimal(animal: Animal)
    fun moveAnimal(animal: Animal, destination: Location)

    fun addProperty(property: Property, animal: Animal)
    fun deleteProperty(property: Property, animal: Animal)

    fun generateFood(location: Location)
    // Количество еды в локации
    fun food(location: Location): Int
    // Установить количество еды в локации
    fun setFood(location: Location, number: Int)
    // Установить количество еды животному
    fun setFood(animal: Animal, number: Int)

    // Может ли животное мигрировать из локации в локацию
    fun canMove(from: Location, to: Location): Boolean
}
