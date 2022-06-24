package dev.bozho.states

open class EmptyState(tickAmount: Int) : LoggedState() {
    override val duration: Int = tickAmount

    override fun onStart() {
    }

    override fun onUpdate() {
    }

    override fun onEnd() {
    }
}