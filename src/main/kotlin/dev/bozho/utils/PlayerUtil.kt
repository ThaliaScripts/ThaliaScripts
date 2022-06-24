package dev.bozho.utils

import dev.bozho.ThaliaScripts.Companion.mc


object PlayerUtil {
    fun fastEyeHeight(): Float {
        return if (mc.thePlayer.isSneaking) 1.54f else 1.62f
    }
}