package com.thaliascripts.states.rotations

import com.thaliascripts.game.EmptyRotation
import com.thaliascripts.game.Rotation
import com.thaliascripts.maths.*
import com.thaliascripts.mc
import net.minecraft.entity.Entity
import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.StateSelfSustaining
import kotlin.math.abs

class FollowRotationState(private val target: Entity, private val stepSize: Float = 10.0f) : StateSelfSustaining() {
    override fun factory(): State {
        return StepRotationState(target, stepSize)
    }
}

class StepDynamicRotationState(private val target: Entity, private val stepSize: Float = 10.0f) : StateSelfSustaining() {
    override fun factory(): State {
        val state = StepRotationState(target, stepSize)
        last = state.lessThanStepSize
        return state
    }
}

class StepRotationState(private val target: Entity, private val stepSize: Float = 10.0f) : RotationTickState(
    EmptyRotation()
) {
    var lessThanStepSize: Boolean = false

    init {
        val currentAngle = Vec2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
        var mutableAngle = getRotationFromBlock(target.positionVector)
        if (abs(mutableAngle.length() - currentAngle.length()) > stepSize) {
            mutableAngle = changeLen(mutableAngle, stepSize)
        }
        else {
            lessThanStepSize = true
        }
        angle = Rotation(fixAngle(mutableAngle))
    }

    override fun toString(): String {
        return "(RotationStepState) ${target.position} stepSize = $stepSize"
    }
}
