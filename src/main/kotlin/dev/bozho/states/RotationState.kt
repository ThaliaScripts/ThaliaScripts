package dev.bozho.states

import net.minecraft.util.Vec3
import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.StateSeries
import java.time.Duration

class RotationState(block: Vec3, states: List<State> = GetRotationStates()) : StateSeries(states) {
    companion object {
        fun GetRotationStates(): List<State> {
            TODO("Not yet implemented")
        }
    }

    val rotatingTo = block

    class RotationTickState : LoggedState() {
        override val duration: Duration = Duration.ZERO

        override fun onStart() {
            TODO("Not yet implemented")
        }

        override fun onUpdate() {
            TODO("Not yet implemented")
        }

        override fun onEnd() {
            TODO("Not yet implemented")
        }
    }
}