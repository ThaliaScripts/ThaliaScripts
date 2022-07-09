package dev.bozho.states

import dev.bozho.ThaliaScripts.Companion.mc
import dev.bozho.states.rotationfunctions.RandomRotation
import dev.bozho.states.rotationfunctions.Rotation
import net.minecraft.util.MathHelper
import net.minecraft.util.Tuple
import net.minecraft.util.Vec3
import net.minikloon.fsmgasm.StateSeries
import kotlin.math.atan2
import kotlin.math.sqrt

class DynamicRotationState(private val target: Vec3, ticks: Int, private val simulateDuck: Boolean = false) : StateSeries(emptyList()) {
    init {
        this.addAll(List(ticks) {
            val count = it + 1
            DynamicRotationTickState(target, ticks - count, simulateDuck)
        })
    }

    class DynamicRotationTickState(private val goalVec: Vec3, private val ticksLeft: Int, private val simulateDuck: Boolean) : RotationState.RotationTickState(0.0f, 0.0f) {
        override fun onStart() {
            val rotateFromYaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw)
            val rotateFromPitch = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationPitch)

            val goal = getRotationFromBlock(goalVec, simulateDuck)
            rotateToYaw = goal.first
            rotateToPitch = goal.second

            if (ticksLeft != 0) {
                val window = Rotation.RotationWindow(0, 1, ticksLeft)
                rotateToYaw = easeOutCubic.generateList(rotateFromYaw, rotateToYaw, window)[0]
                rotateToPitch = easeOutCubic.generateList(rotateFromPitch, rotateToPitch, window)[0]
            }

            super.onStart()
        }
    }

    companion object {
        val easeOutCubic = RandomRotation.EaseOutCubic()
    }
}

class RotationState(rotateToAngleYaw: Float, rotateToAnglePitch: Float, ticks: Int) : StateSeries(emptyList()) {
    constructor(target: Vec3, ticks: Int, simulateDuck: Boolean = false) : this(getRotationFromBlock(target, simulateDuck), ticks)
    constructor(rotateToAngle: Tuple<Float, Float>, ticks: Int) : this(rotateToAngle.first, rotateToAngle.second, ticks)

    override val duration: Int = ticks

    init {
        val rotateFromYaw = mc.thePlayer.rotationYaw
        val rotateFromPitch = mc.thePlayer.rotationPitch

        RandomRotation.changeFunction()
        val window = Rotation.RotationWindow(0, ticks - 1, ticks)
        val yawSteps = RandomRotation.generateListWithRandomness(rotateFromYaw, rotateToAngleYaw, window)
        val pitchSteps = RandomRotation.generateListWithRandomness(rotateFromPitch, rotateToAnglePitch, window)

        this.addAll(List(ticks - 1) {
            RotationTickState(yawSteps[it], pitchSteps[it])
        })
        this.add(RotationTickState(rotateToAngleYaw, rotateToAnglePitch))
    }

    open class RotationTickState(protected var rotateToYaw: Float, protected var rotateToPitch: Float) : LoggedState() {
        override fun onStart() {
            mc.thePlayer.rotationYaw = rotateToYaw
            mc.thePlayer.rotationPitch = rotateToPitch
        }

        override fun onUpdate() { }
        override fun onEnd() { }
        override val duration: Int = 1
    }
}

internal fun getRotationFromBlock(pos: Vec3, simulateDuck: Boolean): Tuple<Float, Float> {
    val eyeHeight = mc.thePlayer.eyeHeight - if (mc.thePlayer.isSneaking || simulateDuck) 0.08f else 0.0f
    val playerPos = Vec3(mc.thePlayer.posX, mc.thePlayer.posY + eyeHeight, mc.thePlayer.posZ)
    return getRotationFromBlock(playerPos, pos)
}

internal fun getRotationFromBlock(from: Vec3, to: Vec3): Tuple<Float, Float> {
    val diffX = to.xCoord - from.xCoord
    val diffY = to.yCoord - from.yCoord
    val diffZ = to.zCoord - from.zCoord

    val x = MathHelper.wrapAngleTo180_float((Math.toDegrees(atan2(diffZ, diffX)) - 90.0f).toFloat())
    val y = -Math.toDegrees(atan2(diffY, sqrt(diffX * diffX + diffZ * diffZ))).toFloat()
    return Tuple(x, y)
}
