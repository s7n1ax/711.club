package me.bush.cornerstore.impl.module.modules.misc

import me.bush.cornerstore.api.event.StagedEvent
import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.settings.IntSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.timer.MilliTimer
import me.bush.cornerstore.util.timer.Timer
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.network.play.server.SPacketConfirmTransaction
import net.minecraft.network.play.server.SPacketKeepAlive

/**
 * @author perry.
 * @since 1/24/2022.
 */
object PingSpoof : Module("Allows u to spoof ur ms.") {
    private val keepalive: BooleanSetting = BooleanSetting("C00", false).withInfo("Whether to cancel SPacketKeepAlive.")
    private val transaction: BooleanSetting = BooleanSetting("C0F", false).withInfo("Whether to cancel SPacketConfirmTransaction.")
    private val antikick: BooleanSetting = BooleanSetting("AntiKick", false).withInfo("Attempts to stop u from being kicked.")
    private val delay: IntSetting = IntSetting("Delay", 30, 5, 60).withVis(antikick::value).withInfo("How much time in seconds to wait to send the antikick packet.")
    private val timer: Timer = MilliTimer()

    @EventListener
    fun onPacketReceive() = listener<PacketEvent.Receive> {
        if (it.stage == StagedEvent.Stage.POST) return@listener
        if (keepalive.value && it.packet is SPacketKeepAlive || transaction.value && it.packet is SPacketConfirmTransaction) {
            it.cancel()
        }
        if (antikick.value && timer.passedMs(delay.value * 1000) && keepalive.value && it.packet is SPacketKeepAlive || transaction.value && it.packet is SPacketConfirmTransaction) {
            it.cancelled = false
            timer.reset()
        }
    }
}
