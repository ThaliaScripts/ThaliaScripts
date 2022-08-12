package com.thaliascripts.maths.spline

import com.thaliascripts.maths.*
import net.minecraft.util.Vec3


fun hermiteInterpolate(
    y0: Vec3, y1: Vec3,
    y2: Vec3, y3: Vec3,
    mu: Double,
    tension: Double = -1.0,
    bias: Double = 0.0
): Vec3 {
    val mu2 = mu * mu
    val mu3 = mu2 * mu
    val btConst = (1.0+bias)*(1.0-tension)/2.0
    var m0 = (y1 - y0) * btConst
    m0 += (y2 - y1) * btConst
    var m1 = (y2 - y1) * btConst
    m1 += (y3 - y2) * btConst
    val a0 = 2.0 * mu3 - 3.0 * mu2 + 1.0
    val a1 = mu3 - 2.0 * mu2 + mu
    val a2 = mu3 - mu2
    val a3 = -2.0 * mu3 + 3.0 * mu2
    return y1 * a0 + m0 * a1 + m1 * a2 + y2 * a3
}

fun cubicInterpolate(
    y0: Vec3, y1: Vec3,
    y2: Vec3, y3: Vec3,
    mu: Double
): Vec3 {
    val mu2: Double = mu * mu
    val a0: Vec3 = y3 - y2 - y0 + y1
    val a1: Vec3 = y0 - y1 - a0
    val a2: Vec3 = y2 - y0
    val a3: Vec3 = y1
    return a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3
}