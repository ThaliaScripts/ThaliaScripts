package com.thaliascripts.events

import net.minecraft.entity.monster.EntityCreeper
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class GhostWaveSpawnedEvent(val ghostCount: Int) : Event()

object PostGhostWaveSpawnedEvent {
    private var spawnedThisTick = 0

    @SubscribeEvent
    fun onSpawn(event: EntityJoinWorldEvent) {
        /*if (!MapUtils.inHypixel || MapUtils.mode != MapUtils.SkyblockIsland.DwarvenMines.mode) {
            return
            TODO("BOUNDING BOX DETECTION")
        }*/

        if (event.entity !is EntityCreeper) {
            return
        }

        if (spawnedThisTick == 0) {
            Thread {
                Thread.sleep(250)
                MinecraftForge.EVENT_BUS.post(GhostWaveSpawnedEvent(spawnedThisTick))
                spawnedThisTick = 0
            }.start()
        }

        spawnedThisTick++
    }
}