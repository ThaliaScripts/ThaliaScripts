package com.thaliascripts.game

import com.thaliascripts.maths.Vec2f


open class Rotation(private val angle: Vec2f) {
    open fun getYaw(): Float {
        return angle.x
    }

    open fun getPitch(): Float {
        return angle.y
    }

    open fun toVec(): Vec2f = angle

    override fun toString(): String {
        return "Rotation(${getYaw()}, ${getPitch()})"
    }
}