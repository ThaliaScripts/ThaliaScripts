package dev.bozho.ghosts.travellingsalesman

import net.minecraft.entity.monster.EntityCreeper
import org.jgrapht.Graph
import org.jgrapht.graph.SimpleWeightedGraph

object TestClass {
    private var working = false
    var path: List<Edge>? = null
        private set

    fun printFound() {
        if (working) {
            return
        }

        working = true

        Thread {
            val hkTSP: NearestInsertionHeuristicTSP<EntityCreeper, Edge> = NearestInsertionHeuristicTSP()
            val graph = SimpleWeightedGraph<EntityCreeper, Edge>(Edge::class.java)
            graph.fillGraph()
            //path = graph.findTravellingSalesmanRoute()

            path = hkTSP.getTour(graph).edgeList

            working = false
        }.start()
    }
}

fun Graph<EntityCreeper, Edge>.findTravellingSalesmanRoute(route: List<Edge>? = null): List<Edge>? {
    // Assign a variable, based on if-conditions.
    val routes = if (route == null) {
        // If no route is given, this is the first call.
        // For every Edge in the Graph, we try out a Route that starts with it
        edgeSet().map { findTravellingSalesmanRoute(listOf(it)) }
    } else if (route.visited().toSet() == vertexSet()) {
        // If the Route contains all vertices from the Graph, we found a solution
        listOf(route)
    } else {
        // [route] contains a partial, unfinished route.
        // First, search for all outgoing edges of the current position of the route
        outgoingEdgesOf(route.last().target)
            // Then, remove all Edges whose targets already were visited
            .filterNot { route.visited().contains(it.target) }
            // Finally, append the new Edge to the route and go on recursively
            .map { findTravellingSalesmanRoute(route + it) }
    }

    // Filter all invalid routes and return the best route - the one with minimum duration
    return routes.filterNotNull().minByOrNull { it.distance() }
}