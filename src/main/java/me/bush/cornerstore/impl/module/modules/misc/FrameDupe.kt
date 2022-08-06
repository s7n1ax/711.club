package me.bush.cornerstore.impl.module.modules.misc

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.StagedEvent
import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.minecraft.randomizeHotbar
import me.bush.cornerstore.util.timer.MilliTimer
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.network.play.client.CPacketUseEntity
import net.minecraft.network.play.client.CPacketUseEntity.Action
import net.minecraft.util.EnumHand
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

/**
 * @author noat
 * @since 3/17/2022
 */
// noat turn off unused checks!!!
object FrameDupe : Module("Did anybody say bad servers?") {
    private val delay = IntSetting("Delay", 150, 0, 500)
    private val swing = BooleanSetting("Swing", true)
    private val randomHotbar = BooleanSetting("Random Hotbar", false)
    private val place = BooleanSetting("Place", true)
    private val clear = ActionSetting("Clear IDs") { ids.clear() }
    private val timer = MilliTimer()
    private val ids = mutableListOf<Int>()

    override fun onEnable() = timer.reset()

    @EventListener
    fun onPacketSend() = listener<PacketEvent.Send> {
        if (it.stage == StagedEvent.Stage.PRE && it.packet is CPacketUseEntity) {
            val entity = (it.packet.getEntityFromWorld(mc.world) as? EntityItemFrame) ?: return@listener
            if (it.packet.action == Action.ATTACK && entity.displayedItem.isEmpty && entity.entityId in ids) it.cancel()
            else if (mc.gameSettings.keyBindSneak.isKeyDown && entity.entityId !in ids) {
                ids += entity.entityId
                message("Added")
                it.cancel()
            }
        }
    }

    @EventListener
    fun onTick() = listener<ClientTickEvent> {
        ids.forEach { id ->
            (mc.world?.getEntityByID(id) as? EntityItemFrame)?.let { frame ->
                if (randomHotbar.value) mc.player.inventory?.randomizeHotbar()
                if (place.value) {
                    mc.player.connection?.sendPacket(CPacketUseEntity(frame, EnumHand.MAIN_HAND))
                    if (swing.value) mc.player.swingArm(EnumHand.MAIN_HAND)
                }
                if (!frame.displayedItem.isEmpty && timer.passedMsReset(delay.value)) {
                    mc.player.connection?.sendPacket(CPacketUseEntity(frame))
                    if (swing.value) mc.player.swingArm(EnumHand.MAIN_HAND)
                }
            }
        }
    }
}
