package dev.bozho.travellingsalesman

import dev.bozho.ghosts.EntityGhost
import org.jgrapht.graph.SimpleDirectedGraph

fun SimpleDirectedGraph<EntityGhost, Edge>.findTravellingSalesmanRoute(route: List<Edge>? = null): List<Edge>? {
    val routes = if (route == null) {
        edgeSet().map { findTravellingSalesmanRoute(listOf(it)) }
    } else if (route.visited().toSet() == vertexSet()) {
        listOf(route)
    } else {
        outgoingEdgesOf(route.last().target)
            .filterNot { route.visited().contains(it.target) }
            .map { findTravellingSalesmanRoute(route + it) }
    }

    return routes.filterNotNull().minByOrNull { it.distance() }
}
