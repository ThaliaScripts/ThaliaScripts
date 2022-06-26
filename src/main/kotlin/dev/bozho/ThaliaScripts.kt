package dev.bozho

import dev.bozho.mixins.MinecraftAccessor
import dev.bozho.mixins.RenderManagerAccessor
import dev.bozho.states.StateHandler
import dev.bozho.travellingsalesman.TestClass
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.apache.logging.log4j.LogManager
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color

@Mod(modid = "forgetemplate", name = "Forge Template", version = "0.0.1")
class ThaliaScripts {
    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent?) {
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(StateHandler)
        debugBind = KeyBinding("debug", Keyboard.KEY_O, "ThaliaScripts")
        ClientRegistry.registerKeyBinding(debugBind)
    }

    @SubscribeEvent
    fun onInput(event: InputEvent) {
        if (debugBind?.isPressed == true) {
            TestClass.printFound()
        }
    }

    @SubscribeEvent
    fun onRenderTick(event: RenderGameOverlayEvent) {
        if (mc.thePlayer == null || mc.theWorld == null) return
        if (event.isCancelable || event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) return
        if (mc.currentScreen != null) return
        mc.fontRendererObj.drawString("debug: " + aimingAtGhost, 5, 5, Color(255, 255, 255).rgb)
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent?) {
        if (mc.thePlayer == null || mc.theWorld == null) return
        aimingAtGhost = rayTrace(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, 50.0) != null
    }

    @SubscribeEvent
    fun onRender3D(event: RenderWorldLastEvent?) {
        val fr = mc.fontRendererObj
        mc.theWorld.loadedEntityList.stream().filter { entity: Entity? -> entity is EntityCreeper }
            .forEach { entity: Entity ->
                var distance: Float
                val entityLivingBase = entity as EntityLivingBase
                val x2 =
                    entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (mc as MinecraftAccessor).timer.renderPartialTicks.toDouble() - (mc.renderManager as RenderManagerAccessor).renderPosX
                val y2 =
                    entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (mc as MinecraftAccessor).timer.renderPartialTicks.toDouble() - (mc.renderManager as RenderManagerAccessor).renderPosY
                val z2 =
                    entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (mc as MinecraftAccessor).timer.renderPartialTicks.toDouble() - (mc.renderManager as RenderManagerAccessor).renderPosZ
                GL11.glPushMatrix()
                GL11.glBlendFunc(770, 771)
                GL11.glDisable(2929)
                GlStateManager.translate(x2, y2, z2)
                val name = "1"
                val var13 =
                    if (mc.thePlayer.getDistanceToEntity(entity as Entity)
                            .also {
                                distance = it
                            } <= 5.0f
                    ) 5.0f else distance / 2.0f
                val var14 = 0.016666668f * var13
                GlStateManager.pushMatrix()
                GlStateManager.translate(0.0f, entity.height + 0.5f, 0.0f)
                if (mc.gameSettings.thirdPersonView == 2) {
                    GlStateManager.rotate(
                        -mc.renderManager.playerViewY,
                        0.0f,
                        1.0f,
                        0.0f
                    )
                    GlStateManager.rotate(
                        mc.renderManager.playerViewX,
                        -1.0f,
                        0.0f,
                        0.0f
                    )
                } else {
                    GlStateManager.rotate(
                        -mc.renderManager.playerViewY,
                        0.0f,
                        1.0f,
                        0.0f
                    )
                    GlStateManager.rotate(
                        mc.renderManager.playerViewX,
                        1.0f,
                        0.0f,
                        0.0f
                    )
                }
                GlStateManager.scale(-var14 / 2.0f, -var14 / 2.0f, -var14 / 2.0f)
                var heightOffset = 0
                if (entity.isSneaking()) {
                    heightOffset += 4
                }
                if ((heightOffset.toFloat() - distance / 5.0f).toInt().also { heightOffset = it } < -8) {
                    heightOffset = -8
                }
                val width = (fr.getStringWidth(name) / 2.0f).toInt() + 10
                val height = heightOffset + 15
                val posX = -width - 1
                val expandY = heightOffset
                //            Gui.drawRect(posX, height, width, expandY, new Color(20, 20, 20, 150).getRGB());
                val textWidth = fr.getStringWidth(name)
                val centerX = (posX.toFloat() + width.toFloat() / 2.0f).toInt()
                val centerY = (height.toFloat() + expandY.toFloat() / 2.0f).toInt()
                fr.drawString(
                    name,
                    centerX,
                    (centerY - fr.FONT_HEIGHT * 1.5f).toInt(),
                    Color(255, 120, 0).rgb
                )
                GL11.glPopMatrix()
                GL11.glEnable(2929)
                GL11.glColor3f(1.0f, 1.0f, 1.0f)
                GL11.glPopMatrix()
            }
    }

    companion object {
        var mc = Minecraft.getMinecraft()
        var debugBind: KeyBinding? = null
        var debug = 0
        val logger = LogManager.getLogger()
        var aimingAtGhost = false
        fun rayTrace(yaw: Float, pitch: Float, distance: Double): EntityLivingBase? {
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
        }
    }
}