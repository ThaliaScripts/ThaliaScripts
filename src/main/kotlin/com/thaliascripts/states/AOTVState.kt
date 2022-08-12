package com.thaliascripts.states

import net.minecraft.util.Vec3
import com.thaliascripts.states.KeyButtonState.KeyboardButtons
import com.thaliascripts.states.rotations.DynamicRotationState
import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.StateGroup
import net.minikloon.fsmgasm.StateSeries

class AOTVState(private val teleportingTo: Vec3, states: List<State> = getAOTVStates(teleportingTo)) : StateSeries(states) {
    companion object {
        fun getAOTVStates(block: Vec3): List<State> {
            val rotationRandomness = (5..15).random()
            val waitBeforeDuckRandomness = (5..15).random()
            val duckRandomness = (10..15).random()
            val rightClickRandomness = (8..duckRandomness).random()

            return listOf(
                DynamicRotationState(block, rotationRandomness, true),
                EmptyState(waitBeforeDuckRandomness),
                StateGroup(
                    KeyButtonState(duckRandomness, KeyboardButtons.Sneak),
                    StateSeries(
                        EmptyState(rightClickRandomness),
                        KeyButtonState(1, KeyboardButtons.UseItem)
                    )
                )
            )
        }
    }
}