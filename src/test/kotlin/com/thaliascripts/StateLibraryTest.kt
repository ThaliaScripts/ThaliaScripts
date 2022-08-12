package com.thaliascripts

import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.StateGroup
import net.minikloon.fsmgasm.StateSelfSustaining
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class StateLibraryTest {
    class StateUpdatingNumber : State() {
        var counter = 0
        override fun onUpdate() { counter++ }

        override val duration: Int = 10
        override fun onStart() {}
        override fun onEnd() {}
    }

    @Test
    fun countSelfSustaining() {
        var overall = 0
        val selfSustaining = object : StateSelfSustaining() {
            var counter = 0
            override fun factory(): State {
                if (++counter == 10) {
                    last = true
                }
                return object : State() {
                    override fun onUpdate() {
                        overall++
                    }

                    override val duration: Int = 1
                    override fun onStart() {}
                    override fun onEnd() {}
                }
            }
        }

        selfSustaining.start()
        while (!selfSustaining.ended) {
            selfSustaining.update()
        }

        assertEquals(overall, 10)
    }

    @Test
    fun syncOfGroup() {
        val updateOne = StateUpdatingNumber()
        val updateTwo = StateUpdatingNumber()

        val group = StateGroup(
            updateOne,
            updateTwo
        )

        group.start()
        while (!group.ended) {
            assertEquals(updateOne.counter, updateTwo.counter)
            group.update()
        }
    }
}