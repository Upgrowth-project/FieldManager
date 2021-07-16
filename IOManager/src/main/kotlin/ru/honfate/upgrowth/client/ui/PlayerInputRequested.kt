package ru.honfate.upgrowth.client.ui

import ru.honfate.upgrowth.core.api.io.PlayerAnswers
import java.io.Writer
import java.util.*

// Методы, которыми core будет пользоваться, запрашивая
// ввод пользователя.
interface PlayerInputRequested {

    val inputWriter: Writer
    val inputReader: Scanner

    private fun <T>getByIndex(c: Collection<T>, ind: Int): T {
        var i: Int = 0
        for (e in c) {
            if (i++ == ind)
                return e
        }
        throw Exception("Collection does not contains element with index = $ind")
    }

    fun getInt(range: IntRange, msg: String = ""): Int {
        var res: Int = -1
        do {
            try {
                inputWriter.write(msg)
                res = inputReader.nextInt()
                if (res !in range) {
                    inputWriter.write("Некорректный ввод\n")
                    continue
                }
            } catch (e: Exception) {
                inputWriter.write("Некорректный ввод\n")
                continue
            }
        } while (false)

        return res
    }

    fun getInts(range: IntRange, sizeBorders: IntRange): Collection<Int> {
        val res: MutableCollection<Int> = HashSet<Int>()
        for (i in 0..sizeBorders.last) {
            val done = i in sizeBorders
            val min = when (done) {
                true -> range.first-1 // добавим значение завершения ввода
                false -> range.first // иначе запрашиваем обычный ввод из разрешенного диапазона
            }

            val next = getInt(min..range.last, "Введите число (введено $i чисел): ")
            if (done && next == range.first - 1)
                break // поступил признак завершения ввода

            res.add(next)
            if (i+1 >= sizeBorders.first && i+1 < sizeBorders.last) {
                inputWriter.write("Введите ${range.first - 1} для завершения ввода\n")
            }
        }

        return res
    }

    fun <T> chooseOne(possibilities: Set<T>): T {
        inputWriter.write("Необходимо выбрать один вариант из следующего списка:\n")
        var i: Int = 1
        for (e in possibilities)
            inputWriter.write("\t[${i++}] $e\n")

        val res = getInt(1..possibilities.size, "Введите число: ")

        return getByIndex(possibilities, res - 1)
    }

    fun <T> chooseOneOrNone(possibilities: Set<T>): T? {
        inputWriter.write("Игроку необходимо выбрать из следующего списка:\n")
        var i: Int = 1
        inputWriter.write("\t[0] Ничего из перечисленного\n")
        for (e in possibilities)
            inputWriter.write("\t[${i++}] $e\n")

        val res = getInt(1..possibilities.size)

        return when(res) {
            0 -> null
            else -> getByIndex(possibilities, res)
        }
    }

    fun <T> chooseSet(
        possibilities: Set<T>,
        minNumber: Int,
        maxNumber: Int
    ): Set<T> {
        inputWriter.write("Выберите от $minNumber до $maxNumber вариантов:\n")
        var i: Int = 1
        for (e in possibilities)
            inputWriter.write("\t[${i++}] $e\n")

        val res = getInts(1..possibilities.size, minNumber..maxNumber)

        val hs = HashSet<T>()
        for (i in res) {
            hs.add(getByIndex(possibilities, i))
        }

        return hs
    }

    fun yesNo(invitation: String): PlayerAnswers {

        inputWriter.write(invitation + '\n')
        do {
            when (inputReader.next()) {
                "yes" -> return PlayerAnswers.Yes
                "no" -> return PlayerAnswers.No
                else -> inputWriter.write("Введите yes/no")
            }
        } while (true)
    }

    fun inputString(invitation: String, maxLength: Int): String {
        inputWriter.write("$invitation (максимальная длина - $maxLength)")
        return inputReader.nextLine().subSequence(0, maxLength).toString()
    }
}