package com.thaliascripts.states

import com.thaliascripts.mc
import com.thaliascripts.states.rotations.DynamicRotationState
import net.minecraft.util.Vec3
import net.minikloon.fsmgasm.StateGroup
import net.minikloon.fsmgasm.infiniteDurationState
import kotlin.math.sqrt

class WalkToPointState(private val target: Vec3, private val distanceCheck: Int = 2) : StateGroup(DynamicRotationState(target, 20), KeyButtonState.SprintState(infiniteDurationState)) {
    override fun onUpdate() {
        val diffX = mc.thePlayer.posX - target.xCoord
        val diffZ = mc.thePlayer.posZ - target.zCoord

        val distance = sqrt(diffX * diffX + diffZ * diffZ)
        if (distance < distanceCheck) {
            end()
            return
        }

        super.onUpdate()
    }

    override val duration: Int = infiniteDurationState
}