package dev.bozho.ghosts.travellingsalesman

import dev.bozho.ghosts.EntityGhost

fun Iterable<Edge>.distance() = sumOf { it.distance() }
fun Iterable<Edge>.visited() = map { it.source } + last().target

class Edge(val source: EntityGhost, val target: EntityGhost) {
    fun distance(): Double {
        return source.getDistance(target)
    }
}