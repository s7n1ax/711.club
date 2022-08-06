package me.bush.cornerstore.impl.module.modules.movement

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraftforge.client.event.InputUpdateEvent

/**
 * @author bush
 * @since jan 2022
 */
object AutoWalk : Module("When u walking") {
    private val swim = BooleanSetting("Swim", true).withInfo("Holds space if you are in water")

    @EventListener
    fun onInputUpdate() = listener<InputUpdateEvent> {
        if (it.entityPlayer != mc.player) return@listener
        if (swim.value && (mc.player.isInLava || mc.player.isInWater)) it.movementInput.jump = true
        it.movementInput.moveForward = 1.0f
    }
}
