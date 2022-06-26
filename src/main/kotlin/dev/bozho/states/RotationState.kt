package dev.bozho.states

import dev.bozho.ThaliaScripts
import dev.bozho.ThaliaScripts.Companion.mc
import dev.bozho.states.rotationfunctions.RandomRotation
import dev.bozho.states.rotationfunctions.Rotation
import dev.bozho.states.statelibrary.State
import dev.bozho.states.statelibrary.StateSeries
import dev.bozho.utils.PlayerUtil
import net.minecraft.util.MathHelper
import net.minecraft.util.Tuple
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.sqrt


class RotationState(private val rotateToAngle: Tuple<Float, Float>, private val ticks: Int) : StateSeries(emptyList()) {
    companion object {
        fun getAngleFromBlock(target: Vec3): Tuple<Float, Float> {
            val diffX: Double = target.xCoord - mc.thePlayer.posX
            val diffY: Double = target.yCoord - mc.thePlayer.posY + PlayerUtil.fastEyeHeight()
            val diffZ: Double = target.zCoord - mc.thePlayer.posZ
            val dist = sqrt(diffX * diffX + diffZ * diffZ)

            val yaw = MathHelper.wrapAngleTo180_double(atan2(diffZ, diffX) * 57.29577951308232 - 90.0 - mc.thePlayer.rotationYaw) + mc.thePlayer.rotationYaw
            val pitch = MathHelper.wrapAngleTo180_double(-(atan2(-diffY, dist) * 57.29577951308232) - mc.thePlayer.rotationPitch) + mc.thePlayer.rotationPitch

            return Tuple(yaw.toFloat(), pitch.toFloat())
        }

        fun getRotations(posX: Double, posY: Double, posZ: Double): Tuple<Float, Float> {
            val x: Double = posX - mc.thePlayer.posX
            val y: Double = posY - mc.thePlayer.posY + mc.thePlayer.getEyeHeight()
            val z: Double = posZ - mc.thePlayer.posZ
            ThaliaScripts.logger.info("mc.thePlayer: " + mc.thePlayer.posX + " mc.thePlayer: " + mc.thePlayer.posY + " mc.thePlayer: " + mc.thePlayer.posZ)
            val dist: Double = sqrt(x * x + z * z)
            val yaw = (atan2(z, x) * 180.0 / Math.PI).toFloat() - 90.0f
            val pitch = -(atan2(y, dist) * 180.0 / Math.PI).toFloat()

            return Tuple(yaw, pitch)
        }

        fun getRotationsPizza(target: Vec3, playerYaw: Float, playerPitch: Float): Tuple<Float, Float> {
            val diffX: Double = target.xCoord - mc.thePlayer.posX
            val diffY: Double = target.yCoord - mc.thePlayer.posY + PlayerUtil.fastEyeHeight()
            val diffZ: Double = target.zCoord - mc.thePlayer.posZ
            val dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ).toDouble()

            val yaw = MathHelper.wrapAngleTo180_double(atan2(diffZ, diffX) * 57.29577951308232 - 90.0 - playerYaw)
            val pitch = MathHelper.wrapAngleTo180_double(-(atan2(diffY, dist) * 57.29577951308232) - playerPitch)

            return Tuple(yaw.toFloat(), pitch.toFloat())
        }
    }

    constructor(target: Vec3, ticks: Int) : this(getAngleFromBlock(target), ticks)
    constructor(yaw: Float, pitch: Float, ticks: Int) : this(Tuple(yaw, pitch), ticks)

    init {
        this.addAll(getRotationStates())
    }

    private fun getRotationStates(): List<State> {
        val rotateFromYaw = mc.thePlayer.rotationYaw
        val rotateFromPitch = mc.thePlayer.rotationPitch

        RandomRotation.changeFunction()
        val yawSteps = RandomRotation.generateListWithRandomness(rotateFromYaw, rotateToAngle.first, ticks)
        val pitchSteps = RandomRotation.generateListWithRandomness(rotateFromPitch, rotateToAngle.second, ticks)

        return List(ticks) {
            RotationTickState(yawSteps[it], pitchSteps[it])
        }
    }

    class RotationTickState(private val rotateToYaw: Float, private val rotateToPitch: Float) : LoggedState() {
        override val duration: Int = 0

        override fun onStart() {
            mc.thePlayer.rotationYaw = rotateToYaw
            mc.thePlayer.rotationPitch = rotateToPitch
        }

        override fun onUpdate() {
        }

        override fun onEnd() {
        }
    }
}