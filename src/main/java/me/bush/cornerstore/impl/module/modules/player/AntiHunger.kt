package me.bush.cornerstore.impl.module.modules.player


import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.StagedEvent
import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.network.play.client.CPacketEntityAction
import net.minecraft.network.play.client.CPacketEntityAction.Action
import net.minecraft.network.play.client.CPacketPlayer

/**
 * @author perry.
 * Started 11/18/2021.
 */
object AntiHunger : Module("Attempts to slow down ur hunger usage.") {
    private val sprint: BooleanSetting = BooleanSetting("Sprint", true).withInfo("Cancels u sprinting which really helps with hunger.")
    private val ground: BooleanSetting = BooleanSetting("Ground", true).withInfo("Spoof u onGround since jumping can make hunger go down faster.")

    @EventListener
    fun onPacketSend() = listener<PacketEvent.Send> {
        if (it.stage == StagedEvent.Stage.POST) return@listener
        if (ground.value && it.packet is CPacketPlayer) {
            it.packet.onGround = mc.player.fallDistance >= 0 || mc.playerController.isHittingBlock
        }
        if (sprint.value && it.packet is CPacketEntityAction) {
            it.cancelled = it.packet.action == Action.START_SPRINTING || it.packet.action == Action.STOP_SPRINTING
        }
    }
}
