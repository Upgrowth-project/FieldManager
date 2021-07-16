# Upgrowth project documentation

## Менеджер поля

Менеджер поля представляет интерфейс для взаимодействия с сущностью "поле" в программе. FieldManager требует для инициализации параметры,
передаваемые в объекте FieldManagerInitializer. Класс имплементирует public поля и методы, описанные в интерфейсе CoreFieldInterface и позволяющие взаимодействовать с полем:

Описание public полей:

| Поле | Содержимое |
|------|------------|
| val animals: Array<Animal> | массив со всеми животными на игровом поле |
| val players: Array<Player> | массив со всеми игроками |
| val locations: Array<Location> | массив со всеми локациями |
| val deckSize: Int | размер колоды |

Описание public методов:

| Метод | Описание |
|------|------------|
| <T> refresh(entity: T): T | чево |
| giveCards(player: Player, number: Int) | дать игроку определенное количество карт |
| amountOfCards(player: Player, location: Location): Int | количество карт, которое должен получить игрок за данную локацию |
| addAnimal(location: Location, player: Player): Animal | создать в локации новое животное игрока и вернуть его |
| deleteAnimal(animal: Animal) | удалить животное с поля |
| moveAnimal(animal: Animal, destination: Location) | переместить животное в новую локацию |
| getLocation(animal: Animal): Location | найти локацию, в которой находится животное |
| addProperty(property: Property, animal: Animal) | добавить животному свойство |
| deleteProperty(property: Property, animal: Animal) | удалить свойство у животного |
| generateFood(location: Location) | сгенерировать еду в этой локации |
| food(location: Location): Int | количество еды в локации |
| setFood(location: Location, number: Int) | установить количество еды в локации |
| setFood(animal: Animal, number: Int) | установить количество пищи, съеденное животным (если животное не может столько съесть, установится максимальное число еды для него) |
| canMove(from: Location, to: Location): Boolean | возвращает true, если можно осуществить непосредственный переход между локациями |





### Игровое поле
Поле -- объект, используемый для представления игрового поля: колоды карт, локаций и животных в них. Локации также хранят функции благ.

Структура поля приведена ниже (элементы сомнительной целесообразности обозначены маркером недоумения "??" и сомнительным оранжевым цветом).
![Структура поля](./images/struct.png)

## Менеджмент ввода/вывода

В рамках модуля IOManager необходимо реализовать интерфейсы core.api.io.PlayerInputRequestedInterface, ServerOutputInterface,
позволяющие осуществлять связь сервера с пользователем, асинхронно отправляя ему сообщения и получая ответы. Также пользователь должен иметь
возможность асинхронно выполнять запросы к серверу, на некоторые из которых сервер должен дать ответ.

### Общие сведения о механизме ввода-вывода 

На рисунке показана схема взаимодействия сервера и клиента при отправке сервером запроса.
![Схема взаимодействия сервера и клиента](./images/clientServer.png)

Каждая сторона может отправлять синхронные и асинхронные сообщения. Асинхронные сообщения -- внезапно возникающие запросы на стороне клиента или сервера 
(запрос на приостановку игры от игрока, окончание игры, запрос выбора пользователем одного из нескольких вариантов). Синхронные сообщения -- ответы
на поступивший ранее запрос (такие сообщения отправляются, если запрос требовал от другой стороны дать ответ). Таким образом -- асинхронное сообщение -- вопрос или выводимая информация,
а синхронное -- ответ на поступивший ранее запрос.

Каждая сторона выделяет по одному порту для приема синхронных и асинхронных сообщений, эти порты называются соответственно синхронный и асинхронный.
Для случая, когда клиент отправляет асинхронный запрос к серверу, схема выглядит аналогично.

Ввод-вывод на стороне клиента осуществляется через консоль.

Архитектура системы ввода/вывода приведена на рисунке ниже. 
![Архитектура системы ввода/вывода](./images/clientServerInterface.png)

### Messenger

Messenger -- класс, представляющий интерфейс для передачи сообщений между сервером и клиентом. В общем случае экземпляр Messenger позволяет
отправлять объекты Any вместе с признаком SendingMessageType на синхронный или асинхронный адрес пользователя, характеризуемого передаваемым в конструктор PlayerInfo.

Messenger заворачивает объект в структуру Message, подлежащую сериализации в виде JSON, дальнейшей передаче по сети и десериализации.
Messenger так же позволяет ожидать сообщения по своему синхронному или асинхронному порту.

Код Messenger:
```kotlin
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
```

Код Message:
```kotlin
@Serializable
class Message() {
    var type: SendingMessageType
    var obj: Any?
    var playerName: String

    init {
        type = SendingMessageType.RESUME_GAME
        obj = ""
        playerName = ""
    }

    constructor(_type: SendingMessageType, _obj: Any?, _playerName: String): this() {
        type = _type
        obj = _obj
        playerName = _playerName
    }

    fun toObject(stringValue: String): Message {
        return JSON.parse(Message.serializer(), stringValue)
    }

    fun toJson(message: Message): String {
        // Обратите внимание, что мы вызываем Serializer, который автоматически сгенерирован из нашего класса
        // Сразу после того, как мы добавили аннотацию @Serializer
        return JSON.stringify(Message.serializer(), message)
    }
}
```

### PlayerManager

PlayerManager представляет собой интерфейс взаимодействия сервера с пользователем, предназначенный для отправки ему сообщений и получения ответов.
PlayerManager содержит имя сервера для инициализации объекта Messenger, информацию об игроке, менеджером которого является данный объект, а так же 
ссылку на интерфейс PlayerInputInterface, полученную от ядра.

Класс содержит функции, почти совпадающие с функциями PlayerInputRequestedInterface и ServerOutputInterface (его функции не принимают объект Player).
<code>[spoiler]
```kotlin
class PlayerManager(
    private val serverName: String, // имя сервера
    val playerInfo: PlayerInfo, // информация об игроке, менеджером которого является данный объект
    private val player: Player,
    private val playerInput: PlayerInputInterface
) {
    private val messenger: Messenger = Messenger(serverName)

    private suspend fun asyncListen() {
        while (playerInput.getPlayStatus() == PlayStatus.IS_ON) {
            val clientMessage = messenger.asyncListening(playerInfo)
            makeAnswer(clientMessage)
        }
    }

    private fun makeAnswer(msg: Message) {
        // ответить на сообщение
        when(msg.type) {
            SendingMessageType.PLAY_STATUS -> messenger.send(
                playerInput.getPlayStatus(),
                SendingMessageType.PLAY_STATUS,
                playerInfo, MessageSync.SYNC)

            SendingMessageType.PAUSE_GAME -> playerInput.requestedPause(player)

            SendingMessageType.RESUME_GAME -> playerInput.requestedPause(player)

            SendingMessageType.DISCONNECT -> playerInput.disconnected(player)

            SendingMessageType.LEAVE -> playerInput.cleanExited(player)
        }
    }

    suspend fun <T> chooseOne(possibilities: Set<T>, timeout: Int): T {
        messenger.send(possibilities, SendingMessageType.CHOOSE_ONE, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as T
    }

    suspend fun <T> chooseOneOrNone(possibilities: Set<T>, timeout: Int): T? {
        messenger.send(possibilities, SendingMessageType.CHOOSE_ONE_OR_NONE, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as? T
    }

    suspend fun <T> chooseSet(
        possibilities: Set<T>,
        timeout: Int,
        minNumber: Int,
        maxNumber: Int
    ): Set<T> {
        messenger.send(ChooseCollectionParams<T>(possibilities, minNumber..maxNumber),
            SendingMessageType.CHOOSE_SET, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as Set<T>
    }

    suspend fun <T> chooseSequence(
        possibilities: Set<T>,
        timeout: Int,
        minNumber: Int,
        maxNumber: Int
    ): Array<T> {
        messenger.send(ChooseCollectionParams<T>(possibilities, minNumber..maxNumber),
            SendingMessageType.CHOOSE_SEQUENCE, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as Array<T>
    }

    suspend fun yesNo(invitation: String, timeout: Int): PlayerAnswers {
        messenger.send(invitation, SendingMessageType.ASK_YES_NO, playerInfo)
        return messenger.waitForResponse(playerInfo, timeout) as PlayerAnswers
    }

    suspend fun inputString(invitation: String, maxLength: Int): String {
        messenger.send(AskStringParams(invitation, maxLength), SendingMessageType.ASK_STRING, playerInfo)
        return messenger.waitForResponse(playerInfo) as String
    }

    fun sendGameInfo(gameInfo: GameInfo) {
        messenger.send(gameInfo, SendingMessageType.INFO, playerInfo)
    }

    fun updateChat(chat: Array<ChatEntry>) {
        messenger.send(chat, SendingMessageType.INFO, playerInfo)
    }

    fun sendMessage(message: ServerMessage) {
        messenger.send(message, SendingMessageType.INFO, playerInfo)
    }

    fun pauseGame(timeout: Int?) {
        messenger.send(timeout!!, SendingMessageType.PAUSE_GAME, playerInfo)
    }

    fun resumeGame() {
        messenger.send("", SendingMessageType.RESUME_GAME, playerInfo)
    }

    fun endGame() {
        messenger.send("", SendingMessageType.END_GAME, playerInfo)
    }
}
```
[/spoiler]
</code>




