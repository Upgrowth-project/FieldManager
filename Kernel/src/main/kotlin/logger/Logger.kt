package logger

fun println(s: String) {} // fixme

class Logger(val autoOutput: Boolean = true) {

    var log: String = ""

    infix fun write(record: String) {
        log += record + '\n'
        if (autoOutput)
            println(record)
    }
}

var logger: Logger = Logger()