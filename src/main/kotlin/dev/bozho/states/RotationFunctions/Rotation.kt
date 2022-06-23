package me.mi0.copycatmacro.RotationFunctions

import kotlin.math.pow
import kotlin.random.Random

abstract class Rotation {
    abstract fun getFloat(x: Float): Float

    fun getRandomness(exponent: Float): Float {
        return Random.nextFloat() * 10f.pow(exponent)
    }

    fun generateListWithRandomness(start: Float, end: Float, size: Int, fixedEnds: Boolean = true, randomnessExponent: Float = -0.4f): List<Float> {
        val pointsList = generateList(start, end, size).toMutableList()
        pointsList.map { it + getRandomness(randomnessExponent) * if ((1..2).random() % 2 == 0) -1f else 1f }

        if (fixedEnds) {
            pointsList[0] = start
            pointsList[size - 1] = end
        }

        return pointsList
    }

    fun generateList(start: Float, end: Float, size: Int): List<Float> {
        if (size < 2) {
            throw IllegalArgumentException()
        }

        val length = end - start
        val pointsList = List(size) {
            this.getFloat(1f / size * it) * length + start
        }.toMutableList()

        return pointsList
    }
}