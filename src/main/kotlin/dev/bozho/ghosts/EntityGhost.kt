package dev.bozho.ghosts

import dev.bozho.ThaliaScripts
import net.minecraft.entity.monster.EntityCreeper

fun EntityCreeper.getDistance(entity: EntityCreeper): Double {
    return getDistance(entity.posX, posY, entity.posZ)
}

object EntityCreeperHelper {
    fun List<EntityCreeper>.reload(): List<EntityCreeper> {
        listOfGhosts = getGhostsInBox()
        return listOfGhosts
    }

    var listOfGhosts: List<EntityCreeper> = emptyList()
        private set

    private fun getGhosts(): List<EntityCreeper> {
        return ThaliaScripts.mc.theWorld.loadedEntityList.filter { it is EntityCreeper && it.powered }.map { it as EntityCreeper }}

    private fun getGhostsInBox(): List<EntityCreeper> {
        return getGhosts()
    }
}