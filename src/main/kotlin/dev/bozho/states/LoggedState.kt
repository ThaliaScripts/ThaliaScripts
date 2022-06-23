package dev.bozho.states

import dev.bozho.ThaliaScripts
import net.minikloon.fsmgasm.State

abstract class LoggedState : State() {
    open fun GetLogMessage(): String {
        return "Executing " + this.javaClass.name + " state!"
    }

    fun LogState() {
        ThaliaScripts.logger.info(GetLogMessage())
    }
}