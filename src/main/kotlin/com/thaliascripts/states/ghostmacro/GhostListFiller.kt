package com.thaliascripts.states.ghostmacro

import com.thaliascripts.mc
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityCreeper

object GhostListFiller {
    var listOfGhosts: MutableList<EntityLivingBase> = mutableListOf()
        private set

    fun reload(): MutableList<EntityLivingBase> {
        listOfGhosts = getGhostsInBox()
        return listOfGhosts
    }

    private fun getGhosts(): List<EntityLivingBase> =
        mc.theWorld.loadedEntityList.filter { it is EntityCreeper && it.powered }.map { it as EntityLivingBase }
    private fun getGhostsInBox(): MutableList<EntityLivingBase> =
        getGhosts().filter { it.posY - 2 <= mc.thePlayer.posY }.toMutableList()
}