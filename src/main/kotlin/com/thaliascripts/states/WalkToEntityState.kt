package com.thaliascripts.states

import com.thaliascripts.mc
import com.thaliascripts.states.rotations.DynamicRotationState
import com.thaliascripts.states.rotations.FollowRotationState
import net.minecraft.entity.EntityLivingBase
import net.minikloon.fsmgasm.StateGroup
import net.minikloon.fsmgasm.StateSeries
import net.minikloon.fsmgasm.infiniteDurationState
import kotlin.math.sqrt

class WalkToEntityState(private val target: EntityLivingBase, private val rotation: Int = 2, private val distanceCheck: Int = 2) :
    StateGroup(
        StateSeries(
            DynamicRotationState(target, rotation),
            FollowRotationState(target)
        ),
        KeyButtonState.SprintState(infiniteDurationState)
    ) {

    override fun onUpdate() {
        val diffX = mc.thePlayer.posX - target.posX
        val diffZ = mc.thePlayer.posZ - target.posZ

        val distance = sqrt(diffX * diffX + diffZ * diffZ)
        if (distance < distanceCheck || target.isDead) {
            end()
            return
        }

        super.onUpdate()
    }

    override val duration: Int = infiniteDurationState
}