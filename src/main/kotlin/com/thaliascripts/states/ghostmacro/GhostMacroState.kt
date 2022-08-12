package com.thaliascripts.states.ghostmacro

import com.thaliascripts.mc
import com.thaliascripts.events.GhostWaveSpawnedEvent
import com.thaliascripts.states.EmptyState
import com.thaliascripts.states.WalkToEntityState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityCreeper
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minikloon.fsmgasm.*

class GhostAimTrigger : State() {
    override fun onUpdate() {
        if (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null || mc.objectMouseOver.entityHit !is EntityCreeper) {
            return
        }
        (mc as com.thaliascripts.mixins.MinecraftAccessor).leftClick()
    }

    override fun onEnd() { }
    override fun onStart() { }
    override val duration: Int = infiniteDurationState
}

class GhostMacroState : StateSelfSustaining() {
    object TargetUpdater {
        lateinit var entitiesInOrder: Array<EntityLivingBase>
        private var currentTarget: Int = 0
        private val algo = FarthestInsertion()

        internal fun next(): EntityLivingBase? {
            if (!TargetUpdater::entitiesInOrder.isInitialized) {
                return null
            }

            val entity: EntityLivingBase?
            synchronized(this) {
                entity = entitiesInOrder.getOrNull((currentTarget + 1) % entitiesInOrder.size)
            }
            if (entity != null && entity != mc.thePlayer) {
                currentTarget++
                return entity
            }

            return null
        }

        private val threadLock = Any()
        private var threadRunning = false
        @Suppress("UNUSED_PARAMETER")
        @SubscribeEvent
        fun onGhostWave(event: GhostWaveSpawnedEvent) {
            scanGround()
        }

        fun scanGround() {
            Thread {
                if (GhostMacro.currentState != GhostMacro.MacroState.RUNNING) {
                    return@Thread
                }

                synchronized(threadLock) {
                    if (threadRunning) {
                        return@Thread
                    }
                    threadRunning = true
                }

                val list = GhostListFiller.reload()
                list.add(mc.thePlayer)
                algo.addEntities(list)
                val solved = algo.solve().toTypedArray()

                synchronized(this) {
                    entitiesInOrder = solved
                    currentTarget = entitiesInOrder.indexOf<EntityLivingBase>(mc.thePlayer)
                }

                threadRunning = false
            }.start()
        }
    }

    override fun factory(): State {
        val target = TargetUpdater.next() ?: return EmptyState(10)
        return WalkToEntityState(target)
    }
}

/*
class WalkTowardsGhost : StateHolder() {
    object UpdateGhostList {
        internal var ghosts = mutableListOf<EntityLiving>()
        internal var currentTarget: EntityLiving? = null
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
                val fs = FarthestInsertion()
                fs.addEntities(GhostListFiller.reload())
                val path = fs.solve()
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
        logger.info("Target: ${UpdateGhostList.currentTarget!!.positionVector}")
        this.states.forEach(State::end)
        this.states.clear()
        add(DynamicRotationState(UpdateGhostList.currentTarget!!, 5))
        this.states.forEach(State::start)
    }

    override fun onEnd() { }
    override val duration: Int = infiniteDurationState
}

*/