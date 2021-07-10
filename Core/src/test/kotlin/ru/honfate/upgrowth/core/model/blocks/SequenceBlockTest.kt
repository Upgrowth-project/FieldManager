package ru.honfate.upgrowth.core.model.blocks

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import io.mockk.verifySequence
import org.junit.Test
import ru.honfate.upgrowth.core.model.Core
import ru.honfate.upgrowth.core.model.Variable
import ru.honfate.upgrowth.core.model.types.basic.BooleanValue
import ru.honfate.upgrowth.core.model.types.basic.EmptyValue
import ru.honfate.upgrowth.core.model.types.basic.IntValue
import ru.honfate.upgrowth.core.model.types.basic.NaturalValue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SequenceBlockTest {
    val returnType = BooleanValue()

    val block1 = mockk<Block>()
    val block2 = mockk<Block>()
    val block3 = mockk<Block>()

    val sequenceBlock = SequenceBlock(arrayOf(block1, block2, block3))

    val core = mockk<Core>()

    val declaredVariable = Variable(returnType, "var1")

    val previousContext = contextOf(Variable(IntValue(), "var1"))

    @Test
    fun returnTypeIsCorrect() {
        every { block3.returnType } returns returnType
        assertEquals(returnType, sequenceBlock.returnType)
    }

    @Test
    fun allBlocksAreProcessed() {
        every {block3.returnType} returns EmptyValue()
        for (block in listOf(block1, block2, block3)) {
            every {block.additionalContext} returns emptyContext()
            every {block.run(any(), any())} returns EmptyValue()
        }
        sequenceBlock.run(core, emptyContext())
        verifyOrder {
            block1.run(any(), any())
            block2.run(any(), any())
            block3.run(any(), any())
        }
    }

    @Test
    fun declarationAreVisible() {
        every { block3.returnType } returns EmptyValue()
        every { block1.additionalContext } returns contextOf(declaredVariable)
        every { block1.run(any(), any()) } returns EmptyValue()
        listOf(block2, block3).forEach {
            every { it.additionalContext } returns emptyContext()
            every { it.run(any(), any()) } answers {
                assertTrue(arg<Context>(1).containsKey(declaredVariable.name))
                EmptyValue()
            }
        }
    }

    @Test
    fun shouldRewriteTopLevelContext() {
        every { block3.returnType } returns EmptyValue()
        every { block1.additionalContext } returns contextOf(declaredVariable)
        every { block1.run(any(), any()) } returns EmptyValue()
        listOf(block2, block3).forEach {
            every { it.additionalContext } returns emptyContext()
            every { it.run(any(), any()) } answers {
                assertEquals(returnType, arg<Context>(1).get(declaredVariable.name)!!.type)
                EmptyValue()
            }
        }
    }
}

