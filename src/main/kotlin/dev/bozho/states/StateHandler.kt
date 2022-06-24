package dev.bozho.states

import dev.bozho.ThaliaScripts.Companion.logger
import dev.bozho.ThaliaScripts.Companion.mc
import dev.bozho.mixins.MinecraftAccessor
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import dev.bozho.states.statelibrary.StateSeries
import net.minecraft.util.Vec3

object StateHandler {
    private var state: StateSeries = StateSeries()

    fun scheduleRotateState() {
        val block = mc.thePlayer.rayTrace(5.0, (mc as MinecraftAccessor).timer.renderPartialTicks).blockPos
        val vec = Vec3(block.x.toDouble() + 1.5, block.y.toDouble(), block.z.toDouble() + 0.5)
        state = StateSeries(
            EmptyState(20),
            RotationState(vec, 25)
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