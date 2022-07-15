package com.thaliascripts.ghosts.travellingsalesman

import com.thaliascripts.ghosts.EntityCreeperHelper
import net.minecraft.entity.monster.EntityCreeper
import org.jgrapht.graph.SimpleWeightedGraph


fun SimpleWeightedGraph<EntityCreeper, Edge>.fillGraph(): Boolean {
    val list = EntityCreeperHelper.reload()
    if (list.isEmpty()) {
        return false
    }

    fillGhosts(list)
    fillEdges()

    return true
}

private fun SimpleWeightedGraph<EntityCreeper, Edge>.fillGhosts(ghosts: List<EntityCreeper>) {
    ghosts.forEach {
       addVertex(it)
    }
}

private fun SimpleWeightedGraph<EntityCreeper, Edge>.fillEdges() {
    val nodes = vertexSet()
    nodes.forEach { source ->
        nodes.filter { target -> source != target }.forEach { target ->
            addEdge(source, target, Edge(source, target))
        }
    }

    edgeSet().forEach {
        setEdgeWeight(it, it.distance)
    }
}