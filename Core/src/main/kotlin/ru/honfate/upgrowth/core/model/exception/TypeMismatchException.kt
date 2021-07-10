package ru.honfate.upgrowth.core.model.exception

import ru.honfate.upgrowth.core.model.types.Type

class TypeMismatchException: CompilationErrorException {
    constructor(): super()
    constructor(message: String?): super(message)
    constructor(expected: Type, got: Type):
            super("Type mismatch: expected ${expected.typeName}, got ${got.typeName}")
}