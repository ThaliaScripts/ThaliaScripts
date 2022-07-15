package com.thaliascripts.states

import net.minikloon.fsmgasm.State

open class EmptyState(tickAmount: Int) : State() {
    override fun onStart() { }
    override fun onUpdate() { }
    override fun onEnd() { }
    override val duration: Int = tickAmount
}