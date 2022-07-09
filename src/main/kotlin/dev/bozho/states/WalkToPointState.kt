package dev.bozho.states

import dev.bozho.ThaliaScripts.Companion.mc
import net.minecraft.util.Vec3
import net.minikloon.fsmgasm.StateGroup
import net.minikloon.fsmgasm.infiniteDurationState
import kotlin.math.sqrt


class WalkToPointState(private val target: Vec3) : StateGroup(DynamicRotationState(target, 20), KeyButtonState.SprintState(infiniteDurationState)) {
    override fun onUpdate() {
        val diffX = mc.thePlayer.posX - target.xCoord
        val diffZ = mc.thePlayer.posZ - target.zCoord

        val distance = sqrt(diffX * diffX + diffZ * diffZ)
        if (distance < 2) {
            end()
            return
        }

        super.onUpdate()
    }

    override val duration: Int = infiniteDurationState
}