package com.thaliascripts.states

import com.thaliascripts.logger
import net.minikloon.fsmgasm.State

abstract class LoggedProxyState : State() {
    override fun start() {
        super.start()
        logger.info("State::start -> $this")
    }

    override fun update() {
        super.update()
        logger.info("State::update -> $this")
    }

    override fun end() {
        super.end()
        logger.info("State::end -> $this")
    }
}