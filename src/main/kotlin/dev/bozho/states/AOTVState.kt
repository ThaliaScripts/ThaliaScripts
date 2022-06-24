package dev.bozho.states

import dev.bozho.utils.MouseUtil
import net.minecraft.util.Vec3
import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.StateSeries
import java.util.*

class AOTVState(block: Vec3, states: List<State> = getAOTVStates(block)) : StateSeries(states) {
    companion object {
        fun getAOTVStates(block: Vec3): List<State> {
            TODO("IMPLEMENT AOTV IDIOT")
            //return Arrays.asList(RotationState(block), WaitState((5..15).random()), ClickState(MouseUtil.Buttons.RightButton))
        }
    }

    private val teleportingTo = block
}