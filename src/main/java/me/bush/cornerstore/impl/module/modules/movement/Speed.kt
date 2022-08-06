package me.bush.cornerstore.impl.module.modules.movement

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.events.MoveEvent
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.minecraft.*
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.entity.MoverType

/**
 * @author bush
 * @since 2/1/2022
 */
object Speed : Module("Move fast") {
    private val mode = ModeSetting("Mode", "Static")
    private val staticFactor = FloatSetting("Factor", 1.0f, 0.5f, 3.0f).withVis { mode.isValue("Static") }
    private val liquidCheck = BooleanSetting("Liquid Check", true)

    @EventListener // Higher priority because this overrides the values completely and should be ran first
    fun onMove() = listener<MoveEvent>(priority = 1000) { event ->
        if (!mc.player.shouldMovementHack(liquidCheck.value) || event.mover != MoverType.SELF) return@listener
        when (mode.value) {
            "Static" -> if (event.moving) mc.player.getDirectionComponents(mc.player.movementSpeed * staticFactor.value).let {
                event.x = it[0]
                event.z = it[1]
            }
        }
    }
}
