package com.thaliascripts.states.ghostmacro

import com.thaliascripts.maths.div
import com.thaliascripts.maths.minus
import com.thaliascripts.maths.plus
import com.thaliascripts.mc
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.Vec3

fun EntityLivingBase.get2DDistance(entity: EntityLivingBase): Double = getDistance(entity.posX, posY, entity.posZ)
fun EntityLivingBase.get2DDistance(point: Vec3): Double = getDistance(point.xCoord, posY, point.zCoord)

object Perpendicular {
    private var pointA = Vec3(0.0, 0.0, 0.0)
    private var pointB = Vec3(0.0, 0.0, 0.0)
    private var midPoint = Vec3(0.0, 0.0, 0.0)
    private var slope: Double = 0.0
    private var yIntercept = 0.0

    fun setPoints(pointA: Vec3, pointB: Vec3) {
        Perpendicular.pointA = pointA
        Perpendicular.pointB = pointB

        midPoint = (pointA + pointB) / 2.0
        findSlope()
        yIntercept = -slope * midPoint.xCoord + midPoint.zCoord
    }

    fun getPerpendicular(len: Double): Vec3 =
        Vec3(midPoint.xCoord + len, midPoint.yCoord, slope * (midPoint.zCoord + len) + yIntercept)

    private fun findSlope() {
        val slopePoint = pointA - pointB
        slope = -1.0 / (slopePoint.zCoord / slopePoint.xCoord) // hope this never breaks
    }
}

private val middleOfMap: Vec3 = Vec3(140.0, 76.0, 69.0)
private const val distanceFactor = 3.0
object FindPerpendicularPath {
    fun findDistance(first: EntityLivingBase, second: EntityLivingBase): Double {
        if (first.canEntityBeSeen(second)) {
            return first.get2DDistance(second)
        }

        val additionalPoint = findAdditionalPoint(first.positionVector, second.positionVector)
        return first.get2DDistance(additionalPoint) + second.get2DDistance(additionalPoint)
    }

    enum class PerpendicularSign(val factor: Int) { POSITIVE(1), NEGATIVE(-1) }
    fun findSide(first: Vec3, second: Vec3): PerpendicularSign {
        Perpendicular.setPoints(first, second)
        val positive = Perpendicular.getPerpendicular(PerpendicularSign.POSITIVE.factor.toDouble())
        val negative = Perpendicular.getPerpendicular(PerpendicularSign.NEGATIVE.factor.toDouble())
        val positiveDistance = middleOfMap.distanceTo(positive)
        val negativeDistance = middleOfMap.distanceTo(negative)

        return if (negativeDistance > positiveDistance) {
            PerpendicularSign.POSITIVE
        }
        else {
            PerpendicularSign.NEGATIVE
        }
    }

    fun findAdditionalPoint(first: Vec3, second: Vec3): Vec3 {
        val signFactor: PerpendicularSign = findSide(first, second)
        var currentCandidate: Vec3
        var i = 1
        do {
            val currentDistance = distanceFactor * i++
            currentCandidate = Perpendicular.getPerpendicular(currentDistance * signFactor.factor)
        } while (mc.theWorld.rayTraceBlocks(first, currentCandidate) != null || mc.theWorld.rayTraceBlocks(second, currentCandidate) != null)

        return currentCandidate
    }
}