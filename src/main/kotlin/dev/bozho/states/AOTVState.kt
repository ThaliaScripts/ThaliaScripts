package dev.bozho.states

import dev.bozho.utils.MouseUtil
import net.minecraft.util.Vec3
import dev.bozho.states.statelibrary.State
import dev.bozho.states.statelibrary.StateGroup
import dev.bozho.states.statelibrary.StateSeries

class AOTVState(private val teleportingTo: Vec3, states: List<State> = getAOTVStates(teleportingTo)) : StateGroup(states) {
    companion object {
        fun getAOTVStates(block: Vec3): List<State> {

            val series = StateSeries(
                RotationState(block, 5),
                EmptyState((5..15).random()), ClickState(MouseUtil.Buttons.RightButton)
            )

            return listOf(
                series,
                DuckState(series.duration)
            )
        }
    }
}