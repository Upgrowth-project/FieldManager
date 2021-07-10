package ru.honfate.upgrowth.core.model.blocks

import ru.honfate.upgrowth.core.model.Variable

typealias Context = MutableMap<String, Variable>

fun emptyContext() = emptyMap<String, Variable>().toMutableMap()

fun Iterable<Variable>.toContext() = associateBy { it.name }.toMutableMap()

fun contextOf(vararg list: Variable) = listOf(*list).toContext()