package com.thaliascripts.states

import com.thaliascripts.mc
import net.minecraft.client.settings.GameSettings
import net.minecraft.client.settings.KeyBinding
import net.minikloon.fsmgasm.StateGroup

class KeyButtonState(duration: Int, private val keyboardButton: KeyboardButtons) : EmptyState(duration) {
    enum class KeyboardButtons(val key: Int) {
        Sneak(gs.keyBindSneak.keyCode),
        Jump(gs.keyBindJump.keyCode),
        Sprint(gs.keyBindSprint.keyCode),
        Forward(gs.keyBindForward.keyCode),
        Back(gs.keyBindBack.keyCode),
        Left(gs.keyBindLeft.keyCode),
        Right(gs.keyBindRight.keyCode),
        Attack(gs.keyBindAttack.keyCode),
        UseItem(gs.keyBindUseItem.keyCode)
    }
    companion object { private val gs: GameSettings = mc.gameSettings }

    override fun onStart() {
        KeyBinding.setKeyBindState(keyboardButton.key, true)
    }

    override fun onEnd() {
        KeyBinding.setKeyBindState(keyboardButton.key, false)
    }

    override fun onUpdate() { }

    class SprintState(duration: Int) : StateGroup(KeyButtonState(duration, KeyboardButtons.Sprint), KeyButtonState(duration, KeyboardButtons.Forward))
}

