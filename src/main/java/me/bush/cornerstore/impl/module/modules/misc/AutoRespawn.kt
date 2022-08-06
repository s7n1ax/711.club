package me.bush.cornerstore.impl.module.modules.misc

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.cancel
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.client.gui.GuiGameOver
import net.minecraftforge.client.event.GuiOpenEvent

/**
 * @author bush
 * @since 1/29/2022
 */
object AutoRespawn : Module("Automatically respawns you when you die") {

    @EventListener
    fun onGuiOpen() = listener<GuiOpenEvent> {
        if (it.gui is GuiGameOver) {
            it.cancel()
            mc.player?.respawnPlayer()
        }
    }
}
