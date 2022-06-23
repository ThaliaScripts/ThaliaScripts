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

    @SubscribeEvent
    public void onRenderTick(RenderGameOverlayEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
        if (mc.currentScreen != null) return;

        mc.fontRendererObj.drawString("debug: " + animation.getValue(), 5, 5, new Color(255,255,255).getRGB());
    }

    public boolean animate = false;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(mc.theWorld == null || mc.thePlayer == null) {

            if(animate) animation.update();

        }
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if(debugBind.isPressed()) {
            debug = 0;
            animation.animate(100, 2);
            animate = true;
        }
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
