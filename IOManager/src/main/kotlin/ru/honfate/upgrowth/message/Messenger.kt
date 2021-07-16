package ru.honfate.upgrowth.message

import kotlinx.coroutines.*
import ru.honfate.upgrowth.server.MessageSync
import ru.honfate.upgrowth.server.PlayerInfo
import ru.honfate.upgrowth.server.SendingMessageType
import java.io.*
import java.net.ServerSocket
import java.net.Socket

class Messenger(private val myName: String) {
    private val MIN_PORT = 1024
    private val MAX_PORT = 65000

    private val mySyncPort: Int = TODO()
    private val myAsyncPort: Int = TODO()

    private val gotMessages: MutableCollection<Message> = TODO() // Пул полученных сообщений от всех источников
        // TODO сделать нормальную ссылку

    private fun checkFreePort(fp: Int): Boolean {
        return try {
            val ss = ServerSocket(fp)
            ss.close()
            true
        } catch (e: IOException) {
            false
        }
    }

    private fun getNewPort(): Int {
        // найти минимальный свободный порт. Страшный костыль, конечно, но что делать
        var i: Int = MIN_PORT
        while (i < MAX_PORT) {
            if (checkFreePort(i))
                return i
            i++
        }
        throw Exception("Can't find free port")
    }

    private fun askResponse(responseFrom: PlayerInfo, messageSync: MessageSync): Message {
        // TODO доступ к разделяемому ресурсу корректен?
        val port = when (messageSync) {
            MessageSync.ASYNC -> myAsyncPort
            MessageSync.SYNC -> mySyncPort
        }

        val res = gotMessages.find {m: Message -> m.playerName == responseFrom.name}
        if (res != null) {
            gotMessages.remove(res)
            return res
        }

        val socket: ServerSocket = ServerSocket(port)

        socket.use { socket ->
            do {
                val responseStream = BufferedReader(InputStreamReader(socket.accept().getInputStream()))
                val res = Message().toObject(responseStream.readText())
                if (res.playerName == responseFrom.name) {
                    return res
                }
                gotMessages.add(res)
            } while (true)
        }
    }

    // отправить объект obj с признаком сообщения SendingMessageType по адресу targetAddress
    fun send(obj: Any?, messageType: SendingMessageType, target: PlayerInfo, sync: MessageSync = MessageSync.ASYNC) {
        val address = when (sync) {
            MessageSync.ASYNC -> target.asyncAddress
            MessageSync.SYNC -> target.syncAddress
        }

        val socket: Socket = Socket(address.ipAddress, address.port)
        val message = Message(messageType, obj, myName)

        val toDest: Writer = OutputStreamWriter(socket.getOutputStream())
        toDest.write(message.toJson(message))
    }

    // ожидает ответа от responseFrom в течение timeout секунд
    suspend fun waitForResponse(responseFrom: PlayerInfo, timeout: Int? = 0): Any? {

        suspend fun runWaiting(): Any? = coroutineScope {

            val answerWaiter = async(Dispatchers.IO) { askResponse(responseFrom, MessageSync.SYNC) }

            if (timeout != null && timeout != 0)
                // останавливаем передачу, если истечет таймаут
                launch {
                    delay(timeout * 1000L)
                    answerWaiter.cancelAndJoin()
                }

            return@coroutineScope try {
                answerWaiter.await()
            } catch (e: CancellationException) {
                null
            }
        }

        val res = runWaiting()
        return (res as Message?)?.obj
    }

    suspend fun asyncListening(target: PlayerInfo): Message = coroutineScope {
        return@coroutineScope withContext(Dispatchers.IO) { askResponse(target, MessageSync.ASYNC) }
    }
}