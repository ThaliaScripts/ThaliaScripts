package com.thaliascripts.states.ghostmacro.hud

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minikloon.fsmgasm.State
import net.minikloon.fsmgasm.infiniteDurationState

class GhostStatsHud : State() {
    object MessageTracker {
        var magicFind: Int = 0
        var sorrowFound: Int = 0
        var plasmaFound: Int = 0
        var voltaFound: Int = 0
        var ghostlyBootsFound: Int = 0

        @SubscribeEvent
        fun onChatMessage(event: ClientChatReceivedEvent) {
            val msg = event.message.unformattedText
            if (msg.contains("RARE DROP!")) {
                magicFind = msg.substring(msg.indexOf('+') + 1 until msg.indexOf('%')).toInt()
                if (msg.contains("Plasma")) {
                    plasmaFound++
                }
                else if (msg.contains("Sorrow")) {
                    sorrowFound++
                }
                else if (msg.contains("Volta")) {
                    voltaFound++
                }
                else if (msg.contains("Ghostly Boots")) {
                    ghostlyBootsFound++
                }
            }
        }
    }

    override fun onStart() { }
    override fun onUpdate() { }
    override fun onEnd() { }

    override val duration: Int = infiniteDurationState
}