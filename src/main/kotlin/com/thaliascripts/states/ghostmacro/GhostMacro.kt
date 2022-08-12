package com.thaliascripts.states.ghostmacro

import com.thaliascripts.logger
import com.thaliascripts.states.StateHandler

object GhostMacro {
    enum class MacroState { WAITING, RUNNING; }
    var currentState = MacroState.WAITING
        private set

    fun start() {
        if (currentState == MacroState.WAITING) {
            currentState = MacroState.RUNNING
            StateHandler.scheduleState(GhostMacroState(), GhostAimTrigger())
            StateHandler.state.start()
            GhostMacroState.TargetUpdater.scanGround()
            logger.info("Macro has been started idiot")
        }
    }

    fun stop() {
        if (currentState == MacroState.RUNNING) {
            currentState = MacroState.WAITING
            StateHandler.state.end()
            logger.info("Macro has been stopped")
        }
    }
}