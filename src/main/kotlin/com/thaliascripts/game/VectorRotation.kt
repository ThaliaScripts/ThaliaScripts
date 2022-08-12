package com.thaliascripts.game

import com.thaliascripts.maths.*
import net.minecraft.entity.Entity
import net.minecraft.util.Vec3

class VectorRotation(target: Vec3, simulateDuck: Boolean = false) : Rotation(fixAngle(getRotationFromBlock(target, simulateDuck))) {
    constructor(target: Entity, offset: Vec3 = Vec3(0.0, 0.0, 0.0), simulateDuck: Boolean = false) : this(target.positionVector + offset, simulateDuck)

    override fun toString(): String {
        return "VectorRotation(${getYaw()}, ${getPitch()})"
    }
}
