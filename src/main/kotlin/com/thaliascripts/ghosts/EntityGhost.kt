package com.thaliascripts.ghosts

import com.thaliascripts.mc
import net.minecraft.entity.monster.EntityCreeper

fun EntityCreeper.getDistance(entity: EntityCreeper): Double = getDistance(entity.posX, posY, entity.posZ)

object EntityCreeperHelper {

    var listOfGhosts: List<EntityCreeper> = emptyList()
        private set

    fun reload(): List<EntityCreeper> {
        listOfGhosts = getGhostsInBox()
        return listOfGhosts
    }

    private fun getGhosts(): List<EntityCreeper> {
        return mc.theWorld.loadedEntityList.filter { it is EntityCreeper && it.powered }.map { it as EntityCreeper }}

    private fun getGhostsInBox(): List<EntityCreeper> {
        return getGhosts()
    }
}