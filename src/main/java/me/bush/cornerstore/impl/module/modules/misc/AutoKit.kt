package me.bush.cornerstore.impl.module.modules.misc

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.events.JoinEvent
import me.bush.cornerstore.api.event.events.RespawnEvent
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.minecraft.Format
import me.bush.cornerstore.util.timer.MilliTimer
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraftforge.client.event.ClientChatEvent

/**
 * @author bush
 * @since 1/31/2022
 */
object AutoKit : Module("Sends a /kit command on respawn or login") {
    private val kit = StringSetting("Kit", "pvp")
    private val smart = BooleanSetting("Smart", true).withInfo("Will not /kit if you /kill right after respawning")
    private val adapt = BooleanSetting("Adapt", true).withInfo("Changes the selected kit when you manually /kit")
    private val kitColor = FormatSetting("Kit Color", Format.AQUA)
    private val timer = MilliTimer()

    @EventListener
    fun onRespawn() = listener<RespawnEvent> {
        if ((timer.passedSecondsReset(5) || !smart.value)) sendCommand()
    }

    @EventListener
    fun onJoin() = listener<JoinEvent> {
        sendCommand() // this still doesnt work lol
    }

    @EventListener
    fun onChatSend() = listener<ClientChatEvent> { event ->
        if (adapt.value) event.message.let {
            // Check length so entering the kit menu won't trigger
            if ("/kit" in it && it.length > 5) {
                kit.value = it.drop(5)
            }
        }
    }

    private fun sendCommand() {
        mc.player?.sendChatMessage("/kit ${kit.value}")
        message("Kit {} loaded", kit.value)
    }
}
