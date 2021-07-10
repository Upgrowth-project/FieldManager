package ru.honfate.upgrowth.core.model

import ru.honfate.upgrowth.core.api.field.CoreFieldInterface
import ru.honfate.upgrowth.core.api.io.PlayerInputRequestInterface
import ru.honfate.upgrowth.core.api.io.ServerOutputInterface
import ru.honfate.upgrowth.core.api.kb.KBInterface
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation

class Core(
    fieldManager: CoreFieldInterface,
    requestManager: PlayerInputRequestInterface,
    outputManager: ServerOutputInterface,
    kbManager: KBInterface
) {
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BasicInstruction(val readOnly: Boolean = false)



    val instructionNameMapping: Map<String, KCallable<*>> = this::class.members
        .filter {it.findAnnotation<BasicInstruction>() != null}
        .associateBy { it.name }

}