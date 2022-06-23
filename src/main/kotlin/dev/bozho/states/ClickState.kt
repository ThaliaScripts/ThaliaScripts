package dev.bozho.states

import dev.bozho.utils.MouseUtil
import java.time.Duration

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
        this.LogState()
    }

    override fun GetLogMessage(): String {
        return super.GetLogMessage() + " " + button.toString() + " Button Clicked"
    }

    override val duration: Duration = Duration.ZERO
}