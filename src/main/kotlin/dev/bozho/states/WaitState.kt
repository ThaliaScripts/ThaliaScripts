package dev.bozho.states

import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.StateSeries
import java.time.Duration

class WaitState(tickAmount: Int, states: List<State> = getWaitStates(tickAmount)) : StateSeries(states) {
    companion object {
        fun getWaitStates(size: Int): List<State> {
            return List(size) { WaitOneTickState() }
        }
    }

    class WaitOneTickState : State() {
        override val duration: Duration = Duration.ZERO

        override fun onStart() {

        }

        override fun onUpdate() {

        }

        override fun onEnd() {

        }
    }
}