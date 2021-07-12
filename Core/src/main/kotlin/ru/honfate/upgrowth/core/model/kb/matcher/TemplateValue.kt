package ru.honfate.upgrowth.core.model.kb.matcher

import ru.honfate.upgrowth.core.model.Variable
import ru.honfate.upgrowth.core.model.blocks.Context
import ru.honfate.upgrowth.core.model.types.Type
import ru.honfate.upgrowth.core.model.types.TypedValue
import ru.honfate.upgrowth.core.model.types.basic.EmptyValue

interface TemplateValue {
    val type: Type
    fun matches(value: TypedValue, templateContext: Context): Boolean
}

class Exact(private val value: TypedValue): TemplateValue {
    override val type: Type
        get() = value

    override fun matches(value: TypedValue, templateContext: Context): Boolean = value == this.value
}

class Any: TemplateValue {
    override val type: Type
        get() = EmptyValue()

    override fun matches(value: TypedValue, templateContext: Context): Boolean = true
}

class AnyOf(override val type: Type): TemplateValue {
    override fun matches(value: TypedValue, templateContext: Context): Boolean = type.typeEquals(value)
}

class NamedValue(override val type: Type, private val name: String): TemplateValue {
    override fun matches(value: TypedValue, templateContext: Context): Boolean {
        return if (!type.typeEquals(value)) false
        else if(name in templateContext) value == templateContext[name]!!.typedValue
        else {
            templateContext[name] = Variable(value, name, value)
            true
        }
    }

}