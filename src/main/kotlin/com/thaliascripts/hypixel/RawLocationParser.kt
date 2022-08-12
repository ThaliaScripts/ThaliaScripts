package com.thaliascripts.hypixel

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.thaliascripts.mc
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import java.lang.Thread.sleep
import java.util.*

private val gson = Gson()

object RawLocationParser {
    private var inHypixel = false
    private var gametype = ""
    private var mode = ""
    private var server = ""

    private var waitingForLocraw = false

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onWorldLoad(event: WorldEvent.Load) {
        if (!inHypixel) {
            return
        }

        Thread {
            sleep(1300)
            mc.thePlayer.sendChatMessage("/locraw")
            waitingForLocraw = true
        }.start()
    }

    private fun parseLocation(msg: String) {
        lateinit var loc: JsonObject
        try {
            loc = gson.fromJson(msg, JsonObject::class.java)
            if (loc.has("server")) {
                waitingForLocraw = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        gametype = if (loc.has("gametype")) loc.get("gametype").toString().replace("\"", "") else ""
        mode = if (loc.has("mode")) loc.get("mode").toString().replace("\"", "") else ""
        server = loc.get("server").toString()
    }

    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    fun onMessage(event: ClientChatReceivedEvent) {
        if (!waitingForLocraw || event.type == 2.toByte()) {
            return
        }
        val msg: String = event.message.unformattedText
        if (msg.startsWith("{") && msg.endsWith("}")) {
            parseLocation(msg)
        }
    }

    fun onClientBrandChanged(brand: String) {
        inHypixel = brand.lowercase(Locale.getDefault()).contains("hypixel")
    }

    @SubscribeEvent
    fun onDisconnect(event: ClientDisconnectionFromServerEvent) {
        inHypixel = false
    }

    fun checkMode(mode: SkyblockIsland): Boolean {
        return inHypixel && this.mode == mode.mode
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
