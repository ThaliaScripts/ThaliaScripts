package com.thaliascripts.states

import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.infiniteDurationState

class MineBlockState : State() {
    //override fun factory(): State = StateSeries()
    override val duration: Int = infiniteDurationState

    object BrokenBlockCheck {
        @SubscribeEvent
        fun onBreakBlock(event: BlockEvent.BreakEvent) {

        }
    }

    override fun onStart() {}
    override fun onUpdate() {}
    override fun onEnd() {}
}