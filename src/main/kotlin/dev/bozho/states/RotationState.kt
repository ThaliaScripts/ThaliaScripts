package dev.bozho.states

import dev.bozho.ThaliaScripts.Companion.mc
import dev.bozho.states.RotationFunctions.RandomRotation
import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.StateSeries
import java.time.Duration

class RotationState(private val rotateToYaw: Float, private val rotateToPitch: Float, private val ticks: Int) : StateSeries(emptyList()) {
    init {
        this.addAll(getRotationStates())
    }

    private fun getRotationStates(): List<State> {
        val rotateFromYaw = mc.thePlayer.rotationYaw
        val rotateFromPitch = mc.thePlayer.rotationPitch

        RandomRotation.changeFunction()
        val yawSteps = RandomRotation.generateListWithRandomness(rotateFromYaw, rotateToYaw, ticks)
        val pitchSteps = RandomRotation.generateListWithRandomness(rotateFromPitch, rotateToPitch, ticks)

        return List(ticks) {
            RotationTickState(yawSteps[it], pitchSteps[it])
        }
    }

    class RotationTickState(private val rotateToYaw: Float, private val rotateToPitch: Float) : LoggedState() {
        override val duration: Duration = Duration.ZERO

        override fun onStart() {
            mc.thePlayer.rotationYaw = rotateToYaw
            mc.thePlayer.rotationPitch = rotateToPitch
        }

        override fun onUpdate() {
        }

        override fun onEnd() {
        }
    }
}