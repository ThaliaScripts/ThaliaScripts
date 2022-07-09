package dev.bozho.utils

import com.google.gson.JsonObject
import dev.bozho.ThaliaScripts
import dev.bozho.ThaliaScripts.Companion.mc
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import java.util.*

object MapUtils {
    var inHypixel = false
    var gametype = ""
    var mode = ""
    var server = ""

    private var lastLoad: Long = 0
    private var check = false
    private var waitingForLocraw = false

    @SubscribeEvent
    fun onDisconnect(event: ClientDisconnectionFromServerEvent) {
        inHypixel = false
        check = false
        waitingForLocraw = false
        server = ""
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onWorldLoad(event: WorldEvent.Load?) {
        lastLoad = System.currentTimeMillis()
        check = true
    }

    fun parseLocation(loc: JsonObject) {
        gametype = if (loc.has("gametype")) loc.get("gametype").toString().replace("\"", "") else ""
        mode = if (loc.has("mode")) loc.get("mode").toString().replace("\"", "") else ""
        val newServer: String = loc.get("server").toString()
        if (newServer != server) {
            server = newServer
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    fun onMessage(event: ClientChatReceivedEvent) {
        if (!waitingForLocraw || event.type == 2.toByte()) {
            return
        }
        val msg: String = event.message.unformattedText
        if (msg.startsWith("{") &&
            msg.endsWith("}")
        ) {
            try {
                val obj: JsonObject = ThaliaScripts.gson.fromJson(msg, JsonObject::class.java)
                if (obj.has("server")) {
                    waitingForLocraw = false
                    parseLocation(obj)
                }
                event.isCanceled = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase !== TickEvent.Phase.START || mc.thePlayer == null || mc.theWorld == null ||
            mc.isSingleplayer() || mc.thePlayer.getClientBrand() == null || !check
        ) {
            return
        }

        // Only check after a new map is loaded
        val currentTime = System.currentTimeMillis()
        val msToWait = 1300
        if (currentTime - lastLoad > msToWait) {
            check = false

            // Check if in Hypixel
            if (!mc.thePlayer.clientBrand.lowercase(Locale.getDefault()).contains("hypixel")) {
                return
            }
            inHypixel = true
            mc.thePlayer.sendChatMessage("/locraw")
            waitingForLocraw = true
        }
    }

    enum class SkyblockIsland(var islandName: String, var mode: String) {
        PrivateIsland("Private Island", "dynamic"),
        SpiderDen("Spider's Den", "combat_1"),
        CrimsonIsle("Crimson Isle", "crimson_isle"),
        TheEnd("The End", "combat_3"),
        GoldMine("Gold Mine", "mining_1"),
        DeepCaverns("Deep Caverns", "mining_2"),
        DwarvenMines("Dwarven Mines", "mining_3"),
        CrystalHollows("Crystal Hollows", "crystal_hollows"),
        FarmingIsland("The Farming Islands", "farming_1"),
        ThePark("The Park", "foraging_1"),
        Dungeon("Dungeon", "dungeon"),
        DungeonHub("Dungeon Hub", "dungeon_hub"),
        Hub("Hub", "hub"),
        DarkAuction("Dark Auction", "dark_auction"),
        JerryWorkshop("Jerry's Workshop", "winter");
    }
}