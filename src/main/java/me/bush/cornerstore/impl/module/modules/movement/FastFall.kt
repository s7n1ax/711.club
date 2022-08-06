package me.bush.cornerstore.impl.module.modules.movement

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.StagedEvent
import me.bush.cornerstore.api.event.events.PlayerEvent
import me.bush.cornerstore.api.setting.settings.FloatSetting
import me.bush.cornerstore.api.setting.settings.IntSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.minecraft.*
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener

/**
 * @author bush
 * @since 2/1/2022
 */
object FastFall : Module("Aka reversestep, makes u fall faster") {
    private val speed = FloatSetting("Speed", 5f, 0f, 10f)
    private val height = IntSetting("Max Height", 0, 0, 10).withInfo("Set to 0 to ignore height check")

    @EventListener
    fun onUpdateWalking() = listener<PlayerEvent.Walking> {
        if (it.stage != StagedEvent.Stage.PRE) return@listener
        if (mc.player.shouldMovementHack(true) && mc.player.onGround && !tooHigh() &&
            !mc.gameSettings.keyBindJump.isKeyDown && !mc.player.isAboveLiquid
        ) mc.player.motionY -= speed.value
    }

    private fun tooHigh() = if (height.value == 0) false else mc.player.heightAboveGround > height.value
}
