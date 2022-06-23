package dev.bozho;

import dev.bozho.mixins.MinecraftAccessor;
import dev.bozho.mixins.RenderManagerAccessor;
import dev.bozho.utils.animations.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

@Mod(modid = "forgetemplate", name = "Forge Template", version = "0.0.1")
public class ThaliaScripts {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static KeyBinding debugBind;
    public static int debug = 0;
    public static final Logger logger = LogManager.getLogger();
    public final Animation animation = new Animation();

    @Mod.EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        debugBind = new KeyBinding("debug", Keyboard.KEY_O, "ThaliaScripts");
        // $USER = The username of the currently logged in user.
        // Simply prints out Hello, $USER.
        System.out.println("Hello, " + Minecraft.getMinecraft().getSession().getUsername() + "!");

        ClientRegistry.registerKeyBinding(debugBind);
    }

    public static boolean aimingAtGhost = false;
    @SubscribeEvent
    public void onRenderTick(RenderGameOverlayEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
        if (mc.currentScreen != null) return;

        mc.fontRendererObj.drawString("debug: " + aimingAtGhost, 5, 5, new Color(255,255,255).getRGB());
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(mc.thePlayer == null || mc.theWorld == null) return;

        aimingAtGhost = rayTrace(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, 50.0d) != null;

    }

    public static EntityLivingBase rayTrace(float yaw, float pitch, double distance) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld != null && mc.thePlayer != null) {
            Vec3 position = mc.thePlayer.getPositionEyes(((MinecraftAccessor)mc).getTimer().renderPartialTicks);
            Vec3 lookVector = getVectorForRotation(pitch, yaw);
            double reachDistance = distance;
            Entity pointedEntity = null;
            List<Entity> var5 = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(lookVector.xCoord * mc.playerController.getBlockReachDistance(), lookVector.yCoord * mc.playerController.getBlockReachDistance(), lookVector.zCoord * mc.playerController.getBlockReachDistance()).expand(reachDistance, reachDistance, reachDistance));
            for (int var6 = 0; var6 < var5.size(); ++var6) {
                Entity currentEntity = (Entity) var5.get(var6);
                if (currentEntity.canBeCollidedWith()) {
                    MovingObjectPosition objPosition = currentEntity.getEntityBoundingBox().expand((double) currentEntity.getCollisionBorderSize(), (double) currentEntity.getCollisionBorderSize(), (double) currentEntity.getCollisionBorderSize()).contract(0.1, 0.1, 0.1).calculateIntercept(position, position.addVector(lookVector.xCoord * reachDistance, lookVector.yCoord * reachDistance, lookVector.zCoord * reachDistance));
                    if (objPosition != null) {
                        double range = position.distanceTo(objPosition.hitVec);
                        if (range < reachDistance) {
                            if (currentEntity == mc.thePlayer.ridingEntity && reachDistance == 0.0D) {
                                pointedEntity = currentEntity;
                            } else {
                                pointedEntity = currentEntity;
                                reachDistance = range;
                            }
                        }
                    }
                }
            }
            if (pointedEntity != null && (pointedEntity instanceof EntityLivingBase))
                return (EntityLivingBase) pointedEntity;
        }
        return null;
    }

    public static final Vec3 getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }



    @SubscribeEvent
    public void onRender3D(RenderWorldLastEvent event) {
        FontRenderer fr = mc.fontRendererObj;
        mc.theWorld.loadedEntityList.stream().filter(entity -> entity instanceof EntityCreeper).forEach(entity -> {
            float distance;
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            double x2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)((MinecraftAccessor)(mc)).getTimer().renderPartialTicks - ((RenderManagerAccessor)(mc.getRenderManager())).getRenderPosX();
            double y2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)((MinecraftAccessor)(mc)).getTimer().renderPartialTicks - ((RenderManagerAccessor)(mc.getRenderManager())).getRenderPosY();
            double z2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)((MinecraftAccessor)(mc)).getTimer().renderPartialTicks - ((RenderManagerAccessor)(mc.getRenderManager())).getRenderPosZ();
            GL11.glPushMatrix();
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GlStateManager.translate(x2, y2, z2);
            String name = "1";

            float var13 = (distance = mc.thePlayer.getDistanceToEntity((Entity)entity)) <= 5.0f ? 5.0f : distance / 2.0f;
            float var14 = 0.016666668f * var13;

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, entity.height + 0.5f, 0.0f);
            if (mc.gameSettings.thirdPersonView == 2) {
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(mc.getRenderManager().playerViewX, -1.0f, 0.0f, 0.0f);
            } else {
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
            }
            GlStateManager.scale(-var14 / 2.0f, -var14 /2.0f, -var14 / 2.0f);

            int heightOffset = 0;
            if (entity.isSneaking()) {
                heightOffset += 4;
            }
            if ((heightOffset = (int)((float)heightOffset - distance / 5.0f)) < -8) {
                heightOffset = -8;
            }

            int width = (int)(fr.getStringWidth(name) / 2.0f) + 10;
            int height = heightOffset + 15;
            int posX = -width - 1;
            int expandY = heightOffset;
//            Gui.drawRect(posX, height, width, expandY, new Color(20, 20, 20, 150).getRGB());

            int textWidth = (int)fr.getStringWidth(name);
            int textHeight = (int)fr.FONT_HEIGHT;
            int centerX = (int)((float)posX + ((float)width / 2.0f));
            int centerY = (int)((float)height + (float)expandY / 2.0f);
            fr.drawString(name, centerX, (int) (centerY - textHeight * 1.5f), new Color(255,120,0).getRGB());
            GL11.glPopMatrix();
            GL11.glEnable(2929);
            GL11.glColor3f(1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        });
    }
}
