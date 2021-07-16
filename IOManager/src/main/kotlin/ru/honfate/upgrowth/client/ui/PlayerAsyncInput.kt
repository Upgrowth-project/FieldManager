package ru.honfate.upgrowth.client.ui

import ru.honfate.upgrowth.core.chat.ChatEntry
import java.io.Writer
import java.time.Instant
import java.util.*

abstract class PlayerAsyncInput {

    abstract val messageWriter: Writer
    abstract val inputReader: Scanner
    abstract val clientManager: ClientManager

    fun asyncMenu(clientManager: ClientManager) {
        messageWriter.write("\n\n[1] Написать сообщение\n[2] Попросить паузу\n[3] Возобновить игру\n[4] Покинуть игру\n[5] Покинуть игру и поискать какой-нибудь гетеросексуальный коллектив оппонентов\n" +
                "[0] Бездействовать\n")

        val res = getInt(1..5)
        when (res) {
            1 -> {
                val res = newChatEntry()
                if (res != null)
                    clientManager.newChatEntry(res)}

            2 -> clientManager.requestedPause()
            3 -> clientManager.resumedGame()
            4 -> clientManager.cleanExited()
            5 -> clientManager.rageQuit()
        }
    }

    private fun getInt(range: IntRange, msg: String = ""): Int {
        var res: Int = -1
        do {
            try {
                messageWriter.write(msg)
                res = inputReader.nextInt()
                if (res !in range) {
                    messageWriter.write("Некорректный ввод\n")
                    continue
                }
            } catch (e: Exception) {
                messageWriter.write("Некорректный ввод\n")
                continue
            }
        } while (false)

        return res
    }

    private fun newChatEntry(): ChatEntry? {
        messageWriter.write("Введите сообщение:\t")
        val message = inputReader.nextLine()
        if (message.isNotEmpty())
            return ChatEntry(Instant.now(), clientManager.player, message)

        return null
    }

}