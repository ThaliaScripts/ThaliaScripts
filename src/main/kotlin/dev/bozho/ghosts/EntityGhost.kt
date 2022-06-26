package dev.bozho.ghosts

import dev.bozho.ThaliaScripts
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.world.World
import kotlin.math.sqrt

class EntityGhost private constructor(world: World?) : EntityCreeper(world) {
    companion object {
        fun List<EntityGhost>.reload(): List<EntityGhost> {
            EntityGhost.listOfGhosts = EntityGhost.getGhostsInBox()
            return EntityGhost.listOfGhosts
        }

        var listOfGhosts: List<EntityGhost> = emptyList()
            private set


        private fun getGhosts(): List<EntityGhost> {
            return ThaliaScripts.mc.theWorld.loadedEntityList.filter { it is EntityCreeper && it.isInvisible }.map { it as EntityGhost }
        }

        private fun getGhostsInBox(): List<EntityGhost> {
            return getGhosts()
        }
    }

    fun getDistance(entity: EntityGhost): Double {
        val diffX = posX - entity.posX
        val diffY = posY - entity.posY
        return sqrt(diffX * diffX + diffY * diffY)
    }
}