package dev.bozho.ghosts.travellingsalesman

import com.google.common.graph.*
import dev.bozho.ThaliaScripts.Companion.logger
import dev.bozho.ghosts.EntityGhost

object TestClass {
    private var working = false

    fun printFound() {
        if (working) {
            return
        }

        working = true
        Thread {
            val graph: MutableNetwork<EntityGhost, Edge> = NetworkBuilder.directed().build()
            if (!graph.fillGraph()) {
                return@Thread
            }
            graph.findTravellingSalesmanRoute()?.let { route ->
                logger.info("Went to ${route.visited()} in ${route.distance()} distance!")
            }
        }.start()
    }
}