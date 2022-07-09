package dev.bozho.states

open class EmptyState(tickAmount: Int) : LoggedState() {
    override fun onStart() { }
    override fun onUpdate() { }
    override fun onEnd() { }
    override val duration: Int = tickAmount
}