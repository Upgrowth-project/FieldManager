package ru.honfate.upgrowth.core.model.exception

open class RuntimeErrorException: Exception {
    constructor()
    constructor(message: String?): super(message)
}