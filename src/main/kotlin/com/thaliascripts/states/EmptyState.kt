package com.thaliascripts.states

open class EmptyState(private val tickAmount: Int) : LoggedProxyState() {
    override fun onStart() { }
    override fun onUpdate() { }
    override fun onEnd() { }
    override val duration: Int = tickAmount

    override fun toString(): String {
        return "(EmptyState) tickAmount = $tickAmount"
    }
}