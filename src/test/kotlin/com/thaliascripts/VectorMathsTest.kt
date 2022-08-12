package com.thaliascripts

import com.thaliascripts.maths.*
import net.minecraft.util.Vec3
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class VectorMathsTest {
    @Test
    fun testSigns() {
        val first = Vec3(50.0, 50.0, 50.0)
        val second = Vec3(25.0, 50.0, 100.0)
        assertVecEquals(first * second, Vec3(1250.0, 2500.0, 5000.0))
        assertVecEquals(first / second, Vec3(2.0, 1.0, 0.5))
        assertVecEquals(first + second, Vec3(75.0, 100.0, 150.0))
        assertVecEquals(first - second, Vec3(25.0, 0.0, -50.0))
        assertVecEquals(first * 2.0, Vec3(100.0, 100.0, 100.0))
        assertVecEquals(first / 2.0, Vec3(25.0, 25.0, 25.0))
        assertVecEquals(first + 25.0, Vec3(75.0, 75.0, 75.0))
        assertVecEquals(first - 25.0, Vec3(25.0, 25.0, 25.0))
    }

    fun assertVecEquals(first: Vec3, second: Vec3) {
        assertEquals(first.xCoord, second.xCoord)
        assertEquals(first.yCoord, second.yCoord)
        assertEquals(first.zCoord, second.zCoord)
    }
}