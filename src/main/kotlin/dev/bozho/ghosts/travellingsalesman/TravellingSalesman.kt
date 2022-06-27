package dev.bozho.ghosts.travellingsalesman

import com.google.common.graph.MutableNetwork
import dev.bozho.ghosts.EntityGhost

fun MutableNetwork<EntityGhost, Edge>.findTravellingSalesmanRoute(route: List<Edge>? = null): List<Edge>? {
    val routes = if (route == null) {
        edges().map { findTravellingSalesmanRoute(listOf(it)) }
    } else if (route.visited().toSet() == nodes()) {
        listOf(route)
    } else {
        outEdges(route.last().target)
            .filterNot { route.visited().contains(it.target) }
            .map { findTravellingSalesmanRoute(route + it) }
    }

    return routes.filterNotNull().minByOrNull { it.distance() }
}