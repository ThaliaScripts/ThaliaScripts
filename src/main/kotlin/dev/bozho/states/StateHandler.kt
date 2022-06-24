package dev.bozho.states

import dev.bozho.ThaliaScripts.Companion.mc
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minikloon.fsmgasm.StateSeries

object StateHandler {
    private var state: StateSeries = StateSeries()

    fun scheduleRotateState() {
        state = StateSeries(
            WaitState(20),
            RotationState(mc.thePlayer.rotationYaw + 180F, mc.thePlayer.rotationPitch, 25)
        )
    }

    fun startState() {
        state.start()
    }

    fun freezeState() {
        state.frozen = true
    }

    fun unfreezeState() {
        state.frozen = false
    }

    fun stopState() {
        state.end()
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            return
        }

        if (state.started && !state.ended) {
            state.update()
        }
    }
}