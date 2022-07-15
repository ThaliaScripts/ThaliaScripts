package com.thaliascripts.states

import com.thaliascripts.logger
import com.thaliascripts.mc
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minikloon.fsmgasm.StateSeries

object StateHandler {
    var state: StateSeries = StateSeries()
        private set

    fun scheduleState() {
        logger.info("scheduling state")

        /*val listWalk: List<State> = listOf(
            Vec3(62.0, 199.0, -109.0),
            Vec3(79.0, 191.0, -53.0),
            Vec3(96.0, 193.0, -24.0),
            Vec3(119.0, 186.0, -6.0),
            Vec3(114.0, 185.0, 20.0),
            Vec3(114.0, 153.0, 41.0)
        ).map { WalkToPointState(it) }

        val listAOTV: List<State> = listOf(
            Vec3(119.5, 160.5, 65.5),
            Vec3(129.5, 109.0, 43.5),
            Vec3(148.5, 107.0, 35.5)
        ).map { AOTVState(it) }

        state = StateSeries(listWalk + listAOTV)*/
        /*state = StateSeries(
            StateGroup(
                KillGhosts(),
                WalkTowardsGhost(),
                KeyButtonState.SprintState(infiniteDurationState)
            )
        )*/
        mc.thePlayer.addChatMessage(ChatComponentText("WHAT ARE YOU DOING RETARD"))
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