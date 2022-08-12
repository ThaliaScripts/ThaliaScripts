package com.thaliascripts.states

import com.thaliascripts.logger
import com.thaliascripts.mc
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.StateGroup

object StateHandler {
    var state: State = EmptyState(1)
        private set

    fun scheduleState(vararg state: State) {
        logger.info("scheduling state")
        this.state = StateGroup(
            state.asList()
        )
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

    @SubscribeEvent
    fun onGUIOpen(event: GuiOpenEvent) {
        if (state.started && !state.ended) {
            mc.thePlayer.addChatMessage(ChatComponentText("IDIOT DON'T OPEN INVENTORY WHILE MACRO WORKING RETARD THIS IS INVENTORY WALKING"))
            mc.thePlayer.addChatMessage(ChatComponentText("GG ON THAT BEAM RETARDO"))
            state.end() // hehehehehehehe STOP INV WALK
        }
    }
}