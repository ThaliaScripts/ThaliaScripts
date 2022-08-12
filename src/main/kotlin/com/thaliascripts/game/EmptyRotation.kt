package com.thaliascripts.game

import com.thaliascripts.maths.Vec2f

class EmptyRotation : Rotation(Vec2f(0.0f, 0.0f)) {
    override fun toString(): String {
        return "EmptyRotation()"
    }
}
