package me.bush.cornerstore.impl.module.modules.misc

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.settings.StringSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.network.play.server.SPacketChat

/**
 * @author noat
 * @since 28/4/2022
 */
object AutoCope : Module("Automatically reply cope harder to the selected target.") {
    private val player by StringSetting("Player", "noatmc").withInfo("The player you want to reply to.")
    private val direct by BooleanSetting("Direct", false).withInfo("Send a direct message instead of global chat.")

    @EventListener // Higher priority so it checks before any timestamp is added
    fun onPacket() = listener<PacketEvent>(priority = Int.MAX_VALUE) {
        if (it.packet is SPacketChat &&
            !it.packet.isSystem &&
            it.packet.chatComponent.formattedText.startsWith("<$player>")
        ) mc.player?.sendChatMessage("${if (direct) "/msg $player" else null} cope")
    }
}
