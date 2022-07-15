package com.thaliascripts.ghosts.travellingsalesman

import net.minecraft.entity.monster.EntityCreeper
import com.thaliascripts.ghosts.getDistance

fun Iterable<Edge>.distance() = sumOf { it.distance }
fun Iterable<Edge>.visited() = map { it.source } + last().target

class Edge(val source: EntityCreeper, val target: EntityCreeper) {
    val distance = source.getDistance(target)
}