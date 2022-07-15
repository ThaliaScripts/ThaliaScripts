package com.thaliascripts.states.rotationfunctions

import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random

abstract class Rotation {
    abstract fun getFloat(x: Float): Float

    class RotationWindow(startArg: Int = 0, endArg: Int, maxSizeArg: Int) {
        var maxSize: Int = -1
            private set

        var end: Int = -1
            private set(value) { field = min(value, maxSize) }

        var start: Int = -1
            private set(value) { field = min(value, end) }

        fun getSize(): Int {
            return end - start
        }

        init {
            maxSize = maxSizeArg
            end = endArg
            start = startArg
        }
    }

    private fun getRandomness(exponent: Float): Float {
        return Random.nextFloat() * 10f.pow(exponent)
    }

    fun generateListWithRandomness(start: Float, end: Float, window: RotationWindow, randomnessExponent: Float = -0.4f): List<Float> {
        return generateList(start, end, window)
            .toMutableList()
            .map { it + getRandomness(randomnessExponent) * if ((1..2).random() % 2 == 0) -1f else 1f }
    }

    fun generateList(start: Float, end: Float, window: RotationWindow): List<Float> {
        val length = end - start
        val pointsList = List(window.getSize()) {
            this.getFloat(1f / window.maxSize * (it + 1 + window.start)) * length + start
        }.toMutableList()

        return pointsList
    }
}