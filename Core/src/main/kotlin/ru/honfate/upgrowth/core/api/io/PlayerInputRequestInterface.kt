package ru.honfate.upgrowth.core.api.io

import ru.honfate.upgrowth.core.api.field.Player

// Методы, которыми core будет пользоваться, запрашивая
// ввод пользователя.
interface PlayerInputRequestInterface {
    var defaultTimeout: Int
    var player: Player
    var timeOut: Int

    // Указанный игрок выбирает один вариант из возможных
    suspend fun <T> chooseOne(possibilities: Set<T>,
                      actor: Player = player,
                      timeout: Int = timeOut): T
    // Указанный игрок выбирает один вариант из возможных или отменяет
    // операцию. В случае отмены возвращается null
    suspend fun <T> chooseOneOrNone(possibilities: Set<T>,
                            actor: Player = player,
                            timeout: Int = timeOut): T?

    // Указанный игрок выбирает несколько вариантов из возможных
    // Возвращается множество размером [minNumber; maxNumber]
    suspend fun <T> chooseSet(possibilities: Set<T>,
                      actor: Player = player,
                      timeout: Int = timeOut,
                      minNumber: Int = 0,
                      maxNumber: Int = possibilities.size
    ): Set<T>

    // Указанный игрок выбирает последовательность вариантов из возможных
    // Возвращается массив размером [minNumber; maxNumber]
    // Пока правда это нигде не нужно, но штукенция прикольная
    suspend fun <T> chooseSequence(possibilities: Set<T>,
                           actor: Player = player,
                           timeout: Int = timeOut,
                           minNumber: Int = 0,
                           maxNumber: Int = possibilities.size
    ): Array<T>

    // Вопрос игроку да/нет. Игроку показывается вопрос invitation
    suspend fun yesNo(actor: Player = player,
              invitation: String = "",
              timeout: Int = timeOut
    ): PlayerAnswers

    // Ввод текстового значения. Игроку показывается вопрос invitation
    // результат должет быть не больше maxLength
    suspend fun inputString(actor: Player = player,
                    invitation: String = "",
                    maxLength: Int = 1024   // не будет же он_а Войну и Мир писать
    ): String

}