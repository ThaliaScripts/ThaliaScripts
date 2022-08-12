package com.thaliascripts.states.rotations

import com.thaliascripts.game.Rotation
import com.thaliascripts.mc
import com.thaliascripts.maths.rotationfunctions.RandomRotation
import com.thaliascripts.maths.rotationfunctions.RotationFunction
import net.minikloon.fsmgasm.StateSeries
import com.thaliascripts.maths.*
import com.thaliascripts.states.EmptyState
import kotlin.math.abs

class RotationState(angle: Rotation, ticks: Int) : StateSeries(emptyList()) {
    override val duration: Int = ticks

    init {
        val currentAngle = Vec2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
        val rotationLen = abs(angle.toVec().length() - currentAngle.length())

        RandomRotation.changeFunction()
        val window = RotationFunction.RotationWindow(0, ticks - 1, ticks)
        val lenSteps = RandomRotation.generateListWithRandomness(0.0f, rotationLen, window)

        if (lenSteps.isNotEmpty()) {
            this.addAll(List(ticks - 1) {
                RotationTickState(Rotation(changeLen(angle.toVec(), lenSteps[it])))
            })
            this.add(RotationTickState(angle))
        }
    }
}

open class RotationTickState(protected var angle: Rotation) : EmptyState(1) {
    override fun onUpdate() {
        mc.thePlayer.rotationYaw = angle.getYaw()
        mc.thePlayer.rotationPitch = angle.getPitch()
    }

    override fun onStart() { }
    override fun onEnd() { }

    override fun toString(): String {
        return "(RotationTickState) $angle duration = $duration started = $started ended = $ended readyToEnd=${isReadyToEnd()} remainingDuration = $remainingDuration"
    }
}


