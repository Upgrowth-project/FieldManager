package ru.honfate.upgrowth.core.model.exception

class RuntimeErrorException: Exception {
    constructor()
    constructor(message: String?): super(message)
}