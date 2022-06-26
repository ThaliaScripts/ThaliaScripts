package dev.bozho.travellingsalesman

import dev.bozho.ghosts.EntityGhost
import dev.bozho.ghosts.EntityGhost.Companion.reload
import org.jgrapht.Graph
import org.jgrapht.graph.SimpleDirectedGraph

operator fun <V> Graph<V, *>.plusAssign(vertex:V) { addVertex(vertex) }
operator fun Graph<EntityGhost, Edge>.plusAssign(edge: Edge) { addEdge(edge.source, edge.target, edge) }
operator fun Graph<EntityGhost, Edge>.plusAssign(edges: List<Edge>) { edges.forEach { this += it } }

const val minDistance = 5

fun SimpleDirectedGraph<EntityGhost, Edge>.fillGraph(): Boolean {
    val list = EntityGhost.listOfGhosts.reload()
    if (!list.isEmpty()) {
        return false
    }

    this.fillGhosts(list)
    this.fillEdges(list)

    return true
}

private fun SimpleDirectedGraph<EntityGhost, Edge>.fillGhosts(ghosts: List<EntityGhost>) {
    ghosts.forEach {
        this += it
    }
}

private fun SimpleDirectedGraph<EntityGhost, Edge>.fillEdges(ghosts: List<EntityGhost>) {
    val edges = mutableListOf<Edge>()

    ghosts.forEach { source ->
        val found = mutableListOf<Edge>()

        val ghostsWithoutIt = ghosts.filter { target -> source != target}
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