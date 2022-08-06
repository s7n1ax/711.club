package me.bush.cornerstore.impl.module.modules.combat

import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.settings.ParentSetting
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.eventbuskotlin.EventListener


/**
 * @author bush
 * @since fall 2021
 */
object CrystalAura : Module("hypixel cheat") {
    // Place
    private val doPlace: BooleanSetting = BooleanSetting("Do Place", true).withInfo("CA will only place if this is on")
    private val place = ParentSetting("Place", doPlace)
    // Break
    private val doBreak: BooleanSetting = BooleanSetting("Do Break", true).withInfo("CA will only break if this is on")
    private val boom = ParentSetting("Break", doBreak)
    // Render
    private val doRender: BooleanSetting = BooleanSetting("Do Render", true).withInfo("CA will not render if this is off")
    private val onlyRender: BooleanSetting = BooleanSetting("Only Render", false).withInfo("CA will ONLY render if this is on")
    private val render = ParentSetting("Render", doRender, onlyRender)
    // Misc
    private val misc = ParentSetting("Misc")

    @EventListener
    fun onPacketRecieve(event: PacketEvent.Receive) {
//        if (event.stage == StagedEvent.Stage.POST) return
//        if (event.packet is SPacketSpawnObject) {
//            val attackPacket = CPacketUseEntity()
//            attackPacket.entityId = event.packet.entityID
//            attackPacket.entityAction = CPacketUseEntity.Action.ATTACK
//            NetworkUtil.sendPacketInstantly(attackPacket)
//        }
    }
}
