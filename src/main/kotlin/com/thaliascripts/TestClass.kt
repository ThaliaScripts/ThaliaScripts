package com.thaliascripts

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11
import java.awt.Color

object TestClass {
    fun lineBetweenTwoEntities(e1: Entity, e2: Entity, color: Color) {
        val vpX: Double = mc.renderManager.viewerPosX
        val vpY: Double = mc.renderManager.viewerPosY
        val vpZ: Double = mc.renderManager.viewerPosZ
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
}