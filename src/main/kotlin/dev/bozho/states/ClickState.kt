package dev.bozho.states

import dev.bozho.utils.MouseUtil

class ClickState(buttonType: MouseUtil.Buttons) : LoggedState() {
    private val button: MouseUtil.Buttons = buttonType

    override fun onStart() {
    }

    override fun onUpdate() {
        when (button) {
            MouseUtil.Buttons.RightButton -> MouseUtil.rightclick()
            MouseUtil.Buttons.MiddleButton -> MouseUtil.middleClick()
            else -> MouseUtil.leftClick()
        }
    }

    override fun onEnd() {
        this.logState()
    }

    override fun getLogMessage(): String {
        return super.getLogMessage() + " " + button.toString() + " Button Clicked"
    }

    override val duration: Int = 0
}