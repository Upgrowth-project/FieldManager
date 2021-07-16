package ru.honfate.upgrowth.client.ui

import ru.honfate.upgrowth.core.api.field.Animal
import ru.honfate.upgrowth.core.api.field.Card
import ru.honfate.upgrowth.core.api.field.Player
import ru.honfate.upgrowth.core.api.io.GameInfo
import ru.honfate.upgrowth.core.api.io.ServerMessage
import ru.honfate.upgrowth.core.chat.ChatEntry
import java.io.Writer

interface ServerOutput {

    val writer: Writer
    val players: Collection<Player>

    fun printGameInfo(destinationId: Int, gameInfo: GameInfo) {

        fun printAnimal(animal: Animal) {
            writer.write("\tЖивотное ${animal.name}\t Свойства:\n")
            for (p in animal.properties)
                writer.write("\t\t${p.name}\n")
        }

        fun printCard(card: Card) {
            writer.write("\tКарта со свойствами:\n")
            card.properties.forEach { p -> writer.write("\t\t${p.name}\n") }
        }


        for (location in gameInfo.locations) {
            writer.write("\nЛокация ${location.name}:\n")
            for (player in players) {
                writer.write("Игрок ${player.name}:\n")
                location.animals
                    .filter { a: Animal -> a.owner == player}
                    .forEach {animal -> printAnimal(animal)}

            }

            writer.write("Нейтральные животные:\n")
            location.animals
                .filter  { a: Animal -> a.owner == null}
                .forEach {animal -> printAnimal(animal)}
        }

        writer.write("\n\nКарт у других игроков:")
        for (p in gameInfo.amountsOfCard.keys.filter { p -> p.id != destinationId })
            writer.write("Игрок ${p.name}: ${gameInfo.amountsOfCard[p]}\n")

        writer.write("\nВывод карт на руках у Вас:\n")

        gameInfo.playerHand.forEach { card -> printCard(card) }

    }

    fun updateChat(chat: Array<ChatEntry>) {
        for (entry in chat)
            writer.write("[${entry.timeStamp}] ${entry.author.name} сказал:\n\t${entry.message}\n")
    }

    fun printMessage(message: ServerMessage) {
        writer.write("Сообщение от сервера: ${message.messageType.name}\t${message.message}")
    }

    fun pauseGame(timeout: Int?) {
        writer.write("Игра приостановлена на $timeout секунд")
    }

    fun resumeGame() {
        writer.write("Игра возоблена")
    }

    fun endGame()  {
        writer.write("Игра завершена")
    }

    fun leave(playerName: String) {
        writer.write("Игрок $playerName покинул сессию")
    }
}