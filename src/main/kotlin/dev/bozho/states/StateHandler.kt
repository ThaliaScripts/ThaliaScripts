package dev.bozho.states

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import dev.bozho.states.statelibrary.StateSeries

object StateHandler {
    private var state: StateSeries = StateSeries()

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