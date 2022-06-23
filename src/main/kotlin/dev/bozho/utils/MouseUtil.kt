package dev.bozho.utils

import dev.bozho.ThaliaScripts.mc

object MouseUtil {
    enum class Buttons(val button: Int) {
        LeftButton(1),
        RightButton(2),
        MiddleButton(3)
    }

    fun leftClick() {
        if (!ReflectionUtil.invoke(mc as Any, "func_147116_af")) { // Obfuscated method - clickMouse
            ReflectionUtil.invoke(mc as Any, "clickMouse")
        }
    }

    fun rightclick() {
        if (!ReflectionUtil.invoke(mc as Any, "func_147121_ag")) { // Obfuscated method - rightClickMouse
            ReflectionUtil.invoke(mc as Any, "rightClickMouse")
        }
    }

    fun middleClick() {
        if (!ReflectionUtil.invoke(mc, "func_147112_ai")) { // Obfuscated method - middleClickMouse
            ReflectionUtil.invoke(mc, "middleClickMouse")
        }
    }
}