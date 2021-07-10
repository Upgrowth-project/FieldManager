package ru.honfate.upgrowth.core.model.types.collection

import ru.honfate.upgrowth.core.model.types.TypedValue

interface Iterable: TypedValue {
    val size: Int
    val iterator: Iterator<*>
}