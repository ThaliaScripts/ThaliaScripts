package dev.bozho.states

import dev.bozho.ThaliaScripts.Companion.mc
import net.minecraft.client.settings.KeyBinding

class DuckState(duration: Int) : EmptyState(duration) {

    override fun onStart() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, true)
    }

    override fun onUpdate() {
    }

    override fun onEnd() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, false)
    }
}