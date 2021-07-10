package ru.honfate.upgrowth.core.model.exception

open class CompilationErrorException: Exception {
    constructor()
    constructor(message: String?): super(message)
}