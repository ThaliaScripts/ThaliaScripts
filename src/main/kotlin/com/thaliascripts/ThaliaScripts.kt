package com.thaliascripts

import com.thaliascripts.events.PostGhostWaveSpawnedEvent
import com.thaliascripts.states.StateHandler
import com.thaliascripts.hypixel.RawLocationParser
import com.thaliascripts.states.ghostmacro.GhostMacro
import com.thaliascripts.states.ghostmacro.GhostMacroState
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.input.Keyboard

var mc: Minecraft = Minecraft.getMinecraft()
var logger: Logger = LogManager.getLogger()

@Mod(modid = "forgetemplate", name = "Forge Template", version = "0.0.1")
class ThaliaScripts {
    private lateinit var debugBind: KeyBinding

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(StateHandler)
        MinecraftForge.EVENT_BUS.register(RawLocationParser)
        MinecraftForge.EVENT_BUS.register(PostGhostWaveSpawnedEvent)
        MinecraftForge.EVENT_BUS.register(GhostMacroState.TargetUpdater)
        //MinecraftForge.EVENT_BUS.register(GhostStatsHud.MessageTracker)
        debugBind = KeyBinding("debug", Keyboard.KEY_O, "ThaliaScripts")
        ClientRegistry.registerKeyBinding(debugBind)
    }

    @SubscribeEvent
    fun onInput(event: InputEvent) {
        if (debugBind.isPressed) {
            if (GhostMacro.currentState == GhostMacro.MacroState.RUNNING) {
                GhostMacro.stop()
            }
            else {
                GhostMacro.start()
            }
        }
    }
}