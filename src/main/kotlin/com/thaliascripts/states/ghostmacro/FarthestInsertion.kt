package com.thaliascripts.states.ghostmacro

import net.minecraft.entity.EntityLivingBase

class AlgorithmException(str: String) : Exception(str)
class FarthestInsertion {
    private var nodes = mutableListOf<EntityLivingBase>()
    private val edges = mutableMapOf<EntityLivingBase, MutableMap<EntityLivingBase, Double>>()

    fun addEntities(entities: List<EntityLivingBase>) {
        nodes = entities.toMutableList()
        edges.clear()
        entities.forEach { key ->
            entities.forEach {
                edges.getOrPut(key) { mutableMapOf() }[it] = if (key == it) 0.0 else it.get2DDistance(key)//FindPerpendicularPath.findDistance(key, it)
            }
        }
    }

    fun solve(): List<EntityLivingBase> {
        if (nodes.isEmpty()) {
            throw IllegalArgumentException("Empty node list!")
        }

        val first = nodes[0]
        val notVisited = nodes.filter { it != first }.toMutableList()
        var last = findMax(notVisited, first)
        var subTour: MutableList<EntityLivingBase> = mutableListOf(first, last)

        var max: EntityLivingBase
        while (subTour.count() != nodes.count()) {
            max = findMax(notVisited, last)
            notVisited.remove(last)
            subTour.add(findArc(subTour, max), max)
            last = max
        }

        return subTour
    }

    private fun findArc(list: List<EntityLivingBase>, factor: EntityLivingBase): Int {
        if (list.isEmpty()) {
            throw IllegalArgumentException("Empty sub-tour list.")
        }

        var currentMin: Double = Double.MAX_VALUE
        var currentMinIndex: Int? = null
        list.forEachIndexed { index, vec ->
            if (index != list.count() - 1) {
                val nextVec = list[index + 1]
                val dist = nodeDistance(vec, factor) + nodeDistance(factor, nextVec) - nodeDistance(vec, nextVec)
                if (currentMin > dist) {
                    currentMin = dist
                    currentMinIndex = index + 1
                }
            }
        }

        return currentMinIndex ?: throw AlgorithmException("Impossible case idk what to tell you.")
    }

    private fun findMax(list: List<EntityLivingBase>, factor: EntityLivingBase): EntityLivingBase =
        list.maxByOrNull { nodeDistance(it, factor) } ?: throw IllegalArgumentException("Distance matrix not full.")

    private fun nodeDistance(node1: EntityLivingBase, node2: EntityLivingBase): Double =
        edges[node1]?.get(node2) ?: throw IllegalArgumentException("Distance matrix not full.")
}
