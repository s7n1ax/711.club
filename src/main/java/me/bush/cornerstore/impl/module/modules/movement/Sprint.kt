package me.bush.cornerstore.impl.module.modules.movement

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.events.KeyEvent
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.settings.ModeSetting
import me.bush.cornerstore.impl.module.Module
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener

/**
 * @author perry + bush.
 * Started 11/12/2021.
 */
object Sprint : Module("Use static speed instead lol") {
    private val mode = ModeSetting("Mode", "Normal", "Rage")
    private val hunger = BooleanSetting("Hunger Check", true)

    @EventListener
    fun onKey() = listener<KeyEvent.InGame> {
        if (mode.isValue("Normal")) setSprinting(mc.gameSettings.keyBindForward.isKeyDown)
        if (mode.isValue("Rage")) message("bush deleted rage mode cuz it sucks if u really want sprint dm him")
    }

    private fun setSprinting(enabled: Boolean) {
        if (!hunger.value || mc.player.foodStats.foodLevel >= 6 || !enabled) {
            mc.player.isSprinting = enabled
        }
    }
}
