package me.bush.cornerstore.impl.module.modules.player

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.StagedEvent
import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.api.event.events.PlayerEvent
import me.bush.cornerstore.api.setting.settings.ModeSetting
import me.bush.cornerstore.impl.module.Module
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.network.play.client.CPacketPlayer
import java.util.concurrent.ThreadLocalRandom

/**
 * @author perry.
 * Started 11/18/2021.
 */
object NoFall : Module("Attempts to negate fall damage") {
    private val mode = ModeSetting("Mode", "Normal", "NCP", "OffGround", "OnGround", "Anti", "Vanilla")

    @EventListener
    fun onUpdateWalkingPlayer() = listener<PlayerEvent.Walking> {
        if (!shouldCancel()) return@listener
        when (mode.value) {
            "Anti" -> mc.connection?.sendPacket(CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1, mc.player.posZ, true))
            "NCP" -> mc.connection?.sendPacket(CPacketPlayer(ThreadLocalRandom.current().nextBoolean()))
        }
    }

    @EventListener
    fun onPacketSend() = listener<PacketEvent.Send> {
        if (it.packet !is CPacketPlayer || it.stage == StagedEvent.Stage.POST) return@listener
        when (mode.value) {
            "Normal" -> if (shouldCancel()) it.packet.onGround = true
            "Vanilla" -> if (shouldCancel()) mc.player.fallDistance = 0f
            "OffGround" -> it.packet.onGround = false
            "OnGround" -> it.packet.onGround = true
        }
    }

    private fun shouldCancel() =
        !mc.player.isElytraFlying && !mc.player.capabilities.allowFlying && mc.player.fallDistance > 3
}
