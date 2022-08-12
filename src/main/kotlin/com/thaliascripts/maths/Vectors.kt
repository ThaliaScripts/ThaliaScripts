package com.thaliascripts.maths

import com.thaliascripts.mc
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.sqrt

data class Vec2d(val x: Double, val y: Double) {
    constructor(v: Vec2f) : this(v.x.toDouble(), v.y.toDouble())

    fun normalize(): Vec2d {
        val len = 1.0 / length()
        return this * len
    }

    fun length(): Double {
        return sqrt(x * x + y * y)
    }

    operator fun plus(vec: Vec2d): Vec2d = Vec2d(x + vec.x, y + vec.y)
    operator fun minus(vec: Vec2d): Vec2d = Vec2d(x - vec.x, y - vec.y)
    operator fun times(vec: Vec2d): Vec2d = Vec2d(x * vec.x, y * vec.y)
    operator fun div(vec: Vec2d): Vec2d = Vec2d(x / vec.x, y / vec.y)
    operator fun plus(vec: Double): Vec2d = Vec2d(x + vec, y + vec)
    operator fun minus(vec: Double): Vec2d = Vec2d(x - vec, y - vec)
    operator fun times(vec: Double): Vec2d = Vec2d(x * vec, y * vec)
    operator fun div(vec: Double): Vec2d = Vec2d(x / vec, y / vec)
}

data class Vec2f(val x: Float, val y: Float) {
    constructor(v: Vec2d) : this(v.x.toFloat(), v.y.toFloat())

    fun normalize(): Vec2f {
        val len = 1.0f / length()
        return this * len
    }

    fun length(): Float {
        return sqrt(x * x + y * y)
    }

    operator fun plus(vec: Vec2f): Vec2f = Vec2f(x + vec.x, y + vec.y)
    operator fun minus(vec: Vec2f): Vec2f = Vec2f(x - vec.x, y - vec.y)
    operator fun times(vec: Vec2f): Vec2f = Vec2f(x * vec.x, y * vec.y)
    operator fun div(vec: Vec2f): Vec2f = Vec2f(x / vec.x, y / vec.y)
    operator fun plus(vec: Float): Vec2f = Vec2f(x + vec, y + vec)
    operator fun minus(vec: Float): Vec2f = Vec2f(x - vec, y - vec)
    operator fun times(vec: Float): Vec2f = Vec2f(x * vec, y * vec)
    operator fun div(vec: Float): Vec2f = Vec2f(x / vec, y / vec)
}

operator fun Vec3.plus(vec: Vec3): Vec3 = Vec3(xCoord + vec.xCoord, yCoord + vec.yCoord, zCoord + vec.zCoord)
operator fun Vec3.minus(vec: Vec3): Vec3 = Vec3(xCoord - vec.xCoord, yCoord - vec.yCoord, zCoord - vec.zCoord)
operator fun Vec3.times(vec: Vec3): Vec3 = Vec3(xCoord * vec.xCoord, yCoord * vec.yCoord, zCoord * vec.zCoord)
operator fun Vec3.div(vec: Vec3): Vec3 = Vec3(xCoord / vec.xCoord, yCoord / vec.yCoord, zCoord / vec.zCoord)
operator fun Vec3.plus(vec: Double): Vec3 = Vec3(xCoord + vec, yCoord + vec, zCoord + vec)
operator fun Vec3.minus(vec: Double): Vec3 = Vec3(xCoord - vec, yCoord - vec, zCoord - vec)
operator fun Vec3.times(vec: Double): Vec3 = Vec3(xCoord * vec, yCoord * vec, zCoord * vec)
operator fun Vec3.div(vec: Double): Vec3 = Vec3(xCoord / vec, yCoord / vec, zCoord / vec)

internal fun fixAngle(angle: Vec2f): Vec2f {
    val correct = Vec2f(mc.thePlayer.rotationYaw + angle.x - MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw), angle.y)
    return correct
}

internal fun getRotationFromBlock(pos: Vec3, simulateDuck: Boolean = false): Vec2f {
    val eyeHeight = mc.thePlayer.eyeHeight - if (mc.thePlayer.isSneaking || simulateDuck) 0.08f else 0.0f
    val playerPos = Vec3(mc.thePlayer.posX, mc.thePlayer.posY + eyeHeight, mc.thePlayer.posZ)
    return getRotationFromBlock(playerPos, pos)
}

internal fun getRotationFromBlock(from: Vec3, to: Vec3): Vec2f {
    val diffX = to.xCoord - from.xCoord
    val diffY = to.yCoord - from.yCoord
    val diffZ = to.zCoord - from.zCoord

    val x = MathHelper.wrapAngleTo180_float((Math.toDegrees(atan2(diffZ, diffX)) - 90.0f).toFloat())
    val y = -Math.toDegrees(atan2(diffY, sqrt(diffX * diffX + diffZ * diffZ))).toFloat()
    return Vec2f(x, y)
}

internal fun changeLen(from: Vec2f, to: Vec2f, len: Float): Vec2f {
    val newAngle = (to - from).normalize() * len
    return from + newAngle
}

internal fun changeLen(to: Vec2f, len: Float): Vec2f {
    return changeLen(Vec2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch), to, len)
}
