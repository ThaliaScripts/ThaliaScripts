package com.thaliascripts.states.rotations

import com.thaliascripts.game.EmptyRotation
import com.thaliascripts.game.Rotation
import com.thaliascripts.game.VectorRotation
import com.thaliascripts.maths.*
import com.thaliascripts.maths.rotationfunctions.RandomRotation
import com.thaliascripts.maths.rotationfunctions.RotationFunction
import com.thaliascripts.mc
import net.minecraft.entity.Entity
import net.minecraft.util.Vec3
import net.minikloon.fsmgasm.StateSeries
import kotlin.math.abs

class DynamicRotationState<T>(private val target: T, ticks: Int, private val simulateDuck: Boolean = false) : StateSeries() {
    init {
        when(target) {
            is Vec3, is Entity ->
                this.addAll(List(ticks) {
                    val count = it + 1
                    DynamicRotationTickState(target, ticks - count, simulateDuck)
                })
            else -> throw IllegalArgumentException("goalVec can only be a Vector or an Entity")
        }
    }
}

class DynamicRotationTickState<T>(private val goalVec: T, private val ticksLeft: Int, private val simulateDuck: Boolean) : RotationTickState(
    EmptyRotation()
) {
    override fun onUpdate() {
        val currentAngle = Vec2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)

        angle = when (goalVec) {
            is Vec3 -> VectorRotation(goalVec, simulateDuck)
            is Entity -> VectorRotation(goalVec, Vec3(0.0, 1.5, 0.0), simulateDuck)
            else -> throw IllegalArgumentException("goalVec can only be a Vector or an Entity")
        }

        if (ticksLeft != 0) {
            val rotationLen = abs(angle.toVec().length() - currentAngle.length())
            val window = RotationFunction.RotationWindow(0, 1, ticksLeft)
            val len = easeOutCubic.generateList(0.0f, rotationLen, window).getOrNull(0) ?: return
            angle = Rotation(changeLen(angle.toVec(), len))
        }

        super.onUpdate()
    }

    override fun toString(): String {
        return when (goalVec) {
            is Vec3 -> "(DynamicRotationTickState) $goalVec"
            is Entity -> "(DynamicRotationTickState) ${goalVec.positionVector}"
            else -> "(DynamicRotationTickState) Illegal Argument!!!"
        }
    }

    companion object {
        val easeOutCubic = RandomRotation.EaseOutCubic()
    }
}
