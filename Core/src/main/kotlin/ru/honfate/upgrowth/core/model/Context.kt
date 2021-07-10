package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Variable

typealias Context = Map<String, Variable>

fun emptyContext() = emptyMap<String, Variable>()