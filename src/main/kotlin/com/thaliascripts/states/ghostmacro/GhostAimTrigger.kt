package com.thaliascripts.states.ghostmacro

import com.thaliascripts.logger
import com.thaliascripts.mc
import com.thaliascripts.events.GhostWaveSpawnedEvent
import com.thaliascripts.ghosts.travellingsalesman.Edge
import com.thaliascripts.ghosts.travellingsalesman.fillGraph
import com.thaliascripts.states.DynamicRotationState
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minikloon.fsmgasm.*
import org.jgrapht.graph.SimpleWeightedGraph
import java.awt.Color

class GhostAimTrigger : State() {
    override fun onUpdate() {
        if (mc.objectMouseOver.entityHit == null || mc.objectMouseOver.entityHit !is EntityCreeper) {
            return
        }

        val creeper: EntityCreeper = mc.objectMouseOver.entityHit as EntityCreeper
        (mc as com.thaliascripts.mixins.MinecraftAccessor).leftClick()
        WalkTowardsGhost.UpdateGhostList.removeFromList(creeper)
    }

    override fun onEnd() { }
    override fun onStart() { }
    override val duration: Int = infiniteDurationState
}

class WalkTowardsGhost : StateHolder() {
    object UpdateGhostList {
        internal var ghosts = mutableListOf<EntityCreeper>()
        internal var currentTarget: EntityCreeper? = null
        internal var scheduledUpdate: Boolean = false

        @SubscribeEvent
        fun onGhostsSpawn(event: GhostWaveSpawnedEvent) {
            updateUsingNearestInsertion()
        }

        @SubscribeEvent
        fun onGhostKilled(event: LivingDeathEvent) {
            if (event.entity == null || event.entity !is EntityCreeper) {
                return
            }

            val creeper =  event.entity as EntityCreeper
            removeFromList(creeper)
        }

        @SubscribeEvent
        fun onDraw(event: RenderWorldLastEvent) {
            if (currentTarget == null) {
                return
            }

            com.thaliascripts.TestClass.lineBetweenTwoEntities(currentTarget!!, mc.thePlayer, Color(255, 0, 0))
        }

        fun updateUsingNearestInsertion() {
            Thread {
                val tsp =
                    com.thaliascripts.ghosts.travellingsalesman.NearestInsertionHeuristicTSP<EntityCreeper, Edge>()
                val graph = SimpleWeightedGraph<EntityCreeper, Edge>(Edge::class.java)
                graph.fillGraph()
                if (graph.vertexSet().isEmpty()) {
                    return@Thread
                }

                val path = tsp.getTour(graph).edgeList.map { it.source }
                val nearest = path.minByOrNull { mc.thePlayer.getDistance(it.posX, mc.thePlayer.posY, it.posZ) } ?: return@Thread
                val nearestIndex = path.indexOf(nearest)

                ghosts = path.let { it.slice(nearestIndex until path.size) + it.slice(0 until nearestIndex) }.toMutableList()
                scheduledUpdate = true
            }.start()
        }

        fun removeFromList(entity: EntityCreeper) {
            ghosts.remove(entity)
            scheduledUpdate = true
        }
    }

    override fun onUpdate() {
        if (UpdateGhostList.scheduledUpdate || UpdateGhostList.currentTarget?.isDead == true) {
            updateCurrentTarget()
            UpdateGhostList.scheduledUpdate = false
        }
        this.states.forEach(State::onUpdate)
    }

    override fun onStart() {
        updateCurrentTarget()
    }

    private fun updateCurrentTarget() {
        UpdateGhostList.currentTarget = UpdateGhostList.ghosts[0]
        val loc = Vec3(UpdateGhostList.currentTarget!!.posX, UpdateGhostList.currentTarget!!.posY + 1.5, UpdateGhostList.currentTarget!!.posZ)
        logger.info("Target: $loc")
        this.states.forEach(State::end)
        this.states.clear()
        add(DynamicRotationState(loc, 5))
        this.states.forEach(State::start)
    }

    override fun onEnd() { }
    override val duration: Int = infiniteDurationState
}

