package dev.bozho

import dev.bozho.ThaliaScripts.Companion.logger
import dev.bozho.ghosts.travellingsalesman.Edge
import dev.bozho.ghosts.travellingsalesman.HeldKarpTSP
import dev.bozho.ghosts.travellingsalesman.fillGraph
import dev.bozho.mixins.MinecraftAccessor
import dev.bozho.states.StateHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.jgrapht.graph.SimpleWeightedGraph
import org.lwjgl.opengl.GL11
import java.awt.Color

object TestClass {
    private var working = false
    var path: List<Edge>? = null
        private set

    fun printFound() {
        if (working) {
            return
        }

        working = true

        Thread {
            val hkTSP: HeldKarpTSP<EntityCreeper, Edge> = HeldKarpTSP()
            val graph = SimpleWeightedGraph<EntityCreeper, Edge>(Edge::class.java)
            graph.fillGraph()

            path = hkTSP.getTour(graph).edgeList
            path!!.map { it.source }.forEach {
                logger.info("Vec3(" + it.posX + ", " + it.posY + ", " + it.posZ + "),")
            }

            working = false
        }.start()
    }


    fun lineBetweenTwoEntities(e1: Entity, e2: Entity, color: Color) {
        val vpX: Double = ThaliaScripts.mc.renderManager.viewerPosX
        val vpY: Double = ThaliaScripts.mc.renderManager.viewerPosY
        val vpZ: Double = ThaliaScripts.mc.renderManager.viewerPosZ
        GlStateManager.pushMatrix()
        GL11.glDisable(2929)
        GL11.glDisable(2896)
        GL11.glDisable(3553)
        GL11.glColor4f(color.red.toFloat(), color.green.toFloat(), color.blue.toFloat(), color.alpha.toFloat())
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)
        worldrenderer.pos(e1.posX - vpX, e2.posY - vpY, e1.posZ - vpZ).endVertex()
        worldrenderer.pos(e2.posX - vpX, e2.posY - vpY, e2.posZ - vpZ).endVertex()
        tessellator.draw()
        GL11.glEnable(3553)
        GL11.glEnable(2896)
        GL11.glEnable(2929)
        GlStateManager.popMatrix()
    }

    /*fun rayTrace(yaw: Float, pitch: Float, distance: Double): EntityLivingBase? {
        val mc = Minecraft.getMinecraft()
        if (mc.theWorld != null && mc.thePlayer != null) {
            val position = mc.thePlayer.getPositionEyes((mc as MinecraftAccessor).timer.renderPartialTicks)
            val lookVector = getVectorForRotation(pitch, yaw)
            var reachDistance = distance
            var pointedEntity: Entity? = null
            val var5 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
                mc.thePlayer,
                mc.thePlayer.entityBoundingBox.addCoord(
                    lookVector.xCoord * mc.playerController.blockReachDistance,
                    lookVector.yCoord * mc.playerController.blockReachDistance,
                    lookVector.zCoord * mc.playerController.blockReachDistance
                ).expand(reachDistance, reachDistance, reachDistance)
            )
            for (var6 in var5.indices) {
                val currentEntity = var5[var6] as Entity
                if (currentEntity.canBeCollidedWith()) {
                    val objPosition = currentEntity.entityBoundingBox.expand(
                        currentEntity.collisionBorderSize.toDouble(),
                        currentEntity.collisionBorderSize.toDouble(),
                        currentEntity.collisionBorderSize.toDouble()
                    ).contract(0.1, 0.1, 0.1).calculateIntercept(
                        position,
                        position.addVector(
                            lookVector.xCoord * reachDistance,
                            lookVector.yCoord * reachDistance,
                            lookVector.zCoord * reachDistance
                        )
                    )
                    if (objPosition != null) {
                        val range = position.distanceTo(objPosition.hitVec)
                        if (range < reachDistance) {
                            if (currentEntity === mc.thePlayer.ridingEntity && reachDistance == 0.0) {
                                pointedEntity = currentEntity
                            } else {
                                pointedEntity = currentEntity
                                reachDistance = range
                            }
                        }
                    }
                }
            }
            if (pointedEntity != null && pointedEntity is EntityLivingBase) return pointedEntity
        }
        return null
    }

    fun getVectorForRotation(pitch: Float, yaw: Float): Vec3 {
        val f = MathHelper.cos(-yaw * 0.017453292f - Math.PI.toFloat())
        val f1 = MathHelper.sin(-yaw * 0.017453292f - Math.PI.toFloat())
        val f2 = -MathHelper.cos(-pitch * 0.017453292f)
        val f3 = MathHelper.sin(-pitch * 0.017453292f)
        return Vec3((f1 * f2).toDouble(), f3.toDouble(), (f * f2).toDouble())
    }*/
}