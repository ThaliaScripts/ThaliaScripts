package dev.bozho.ghosts.travellingsalesman

import dev.bozho.ThaliaScripts.Companion.logger
import dev.bozho.ghosts.EntityGhost
import org.jgrapht.graph.SimpleDirectedGraph

object TestClass {
    private var working = false

    fun printFound() {
        if (working) {
            return
        }

        working = true
        Thread {
            val graph = SimpleDirectedGraph<EntityGhost, Edge>(Edge::class.java)
            if (!graph.fillGraph()) {
                return@Thread
            }
            graph.findTravellingSalesmanRoute()?.let { route ->
                logger.info("Went to ${route.visited()} in ${route.distance()} distance!")
            }
        }.start()
    }
}