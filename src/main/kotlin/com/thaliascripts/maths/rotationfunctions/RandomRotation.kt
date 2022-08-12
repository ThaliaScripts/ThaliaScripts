package com.thaliascripts.maths.rotationfunctions

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

object RandomRotation : RotationFunction() {
    private val rotationList: List<RotationFunction>
    private var usingFunction: RotationFunction

    init {
        rotationList = listOf(
            EaseInSine(),
            EaseOutSine(),
            EaseInOutSine(),
            EaseInQuad(),
            EaseOutQuad(),
            EaseInOutQuad(),
            EaseInCubic(),
            EaseOutCubic(),
            EaseInOutCubic(),
            EaseInQuart(),
            EaseOutQuart(),
            EaseInOutQuart()
        )

        usingFunction = rotationList.random()
    }

    fun changeFunction() {
        usingFunction = rotationList.random()
    }

    override fun getFloat(x: Float): Float {
        return usingFunction.getFloat(x)
    }

    class EaseInSine : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return 1f - cos((x * PI.toFloat()) / 2f)
        }
    }

    class EaseOutSine : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return sin((x * PI.toFloat()) / 2f)
        }
    }

    class EaseInOutSine : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return -(cos(PI.toFloat() * x) - 1f) / 2f
        }
    }

    class EaseInQuad : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return x * x
        }
    }

    class EaseOutQuad : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return 1f - (1f - x) * (1f - x)
        }
    }

    class EaseInOutQuad : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return if (x < 0.5f) {
                2f * x * x
            }
            else {
                1f - (-2f * x + 2f).pow(2f) / 2f
            }
        }
    }

    class EaseInCubic : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return x * x * x
        }
    }

    class EaseOutCubic : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return 1f - (1f - x).pow(3f)
        }
    }

    class EaseInOutCubic : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return if(x < 0.5f) {
                4f * x * x * x
            }
            else {
                1f - (-2f * x + 2f).pow(3f) / 2f
            }
        }
    }

    class EaseInQuart : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return x * x * x * x
        }
    }

    class EaseOutQuart : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return 1f - (1f - x).pow(4f)
        }
    }

    class EaseInOutQuart : RotationFunction() {
        override fun getFloat(x: Float): Float {
            return if (x < 0.5f) {
                8f * x * x * x * x
            }
            else {
                1f - (-2f * x + 2f).pow(4f) / 2f
            }
        }
    }
}