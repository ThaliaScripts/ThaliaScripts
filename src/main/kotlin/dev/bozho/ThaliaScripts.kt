package dev.bozho

import com.google.gson.Gson
import dev.bozho.events.PostGhostWaveSpawnedEvent
import dev.bozho.states.StateHandler
import dev.bozho.states.ghostmacro.WalkTowardsGhost
import dev.bozho.utils.MapUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.apache.logging.log4j.LogManager
import org.lwjgl.input.Keyboard

@Mod(modid = "forgetemplate", name = "Forge Template", version = "0.0.1")
class ThaliaScripts {
    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent?) {
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(StateHandler)
        MinecraftForge.EVENT_BUS.register(MapUtils)
        MinecraftForge.EVENT_BUS.register(PostGhostWaveSpawnedEvent)
        MinecraftForge.EVENT_BUS.register(WalkTowardsGhost.UpdateGhostList)
        debugBind = KeyBinding("debug", Keyboard.KEY_O, "ThaliaScripts")
        ClientRegistry.registerKeyBinding(debugBind)
    }

    @SubscribeEvent
    fun onInput(event: InputEvent) {
        if (debugBind?.isPressed == true) {
            //TestClass.printFound()
            logger.info("WUT???")
            StateHandler.scheduleState()
            StateHandler.state.start()
            /*logger.info("key pressed")
            if (mc.objectMouseOver.entityHit is EntityCreeper) {
                creeper = mc.objectMouseOver.entityHit as EntityCreeper
                return
            }

            if (creeper == null) {
                return
            }

            StateHandler.scheduleState(creeper!!.positionVector)
            StateHandler.startState()*/
        }
    }

    companion object {
        var mc = Minecraft.getMinecraft()
        var debugBind: KeyBinding? = null
        val logger = LogManager.getLogger()
        var gson = Gson()
    }
}