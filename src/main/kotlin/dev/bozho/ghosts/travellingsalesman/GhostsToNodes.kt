package dev.bozho.ghosts.travellingsalesman

import com.google.common.graph.MutableNetwork
import dev.bozho.ghosts.EntityGhost
import dev.bozho.ghosts.EntityGhost.Companion.reload

operator fun <V> MutableNetwork<V, *>.plusAssign(node:V) { addNode(node) }
operator fun MutableNetwork<EntityGhost, Edge>.plusAssign(edge: Edge) { addEdge(edge.source, edge.target, edge) }

const val minDistance = 5

fun MutableNetwork<EntityGhost, Edge>.fillGraph(): Boolean {
    val list = EntityGhost.listOfGhosts.reload()
    if (list.isNotEmpty()) {
        return false
    }

    fillGhosts(list)
    fillEdges(list)

    return true
}

private fun MutableNetwork<EntityGhost, Edge>.fillGhosts(ghosts: List<EntityGhost>) {
    ghosts.forEach {
        this += it
    }
}

private fun MutableNetwork<EntityGhost, Edge>.fillEdges(ghosts: List<EntityGhost>) {
    val edges = mutableListOf<Edge>()

    ghosts.forEach { source ->
        val found = mutableListOf<Edge>()

        val ghostsWithoutIt = ghosts.filter { target -> source != target }
        ghostsWithoutIt.forEach { target ->
            val distance = source.getDistance(target)
            if (distance < minDistance) {
                found.add(Edge(source, target))
                return
            }
        }

        if (found.isEmpty()) {
            val ghostsMinBy = ghostsWithoutIt.minByOrNull { target -> source.getDistance(target) }
            found.add(Edge(source, ghostsMinBy!!))
        }

        edges += found
    }

    edges.forEach { this += it }
}