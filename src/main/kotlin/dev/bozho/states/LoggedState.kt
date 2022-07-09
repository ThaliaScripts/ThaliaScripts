package dev.bozho.states

import dev.bozho.ThaliaScripts
import net.minikloon.fsmgasm.State

abstract class LoggedState : State() {
    open fun getLogMessage(): String {
        return "Executing " + this.javaClass.name + " state!"
    }

    fun logState() {
        ThaliaScripts.logger.info(getLogMessage())
    }
}