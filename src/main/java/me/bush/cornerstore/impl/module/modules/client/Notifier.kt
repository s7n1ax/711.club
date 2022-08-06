package me.bush.cornerstore.impl.module.modules.client

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.event.events.PacketEvent
import me.bush.cornerstore.api.event.events.PopEvent
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.impl.managers.PopManager
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.minecraft.*
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.item.ItemArmor
import net.minecraft.network.play.server.SPacketBlockBreakAnim
import net.minecraftforge.event.entity.living.LivingDamageEvent


/**
 * @author bush, noat
 * @since 3/7/2022
 */
object Notifier : Module("ayo the pizza here") {
    private val totems = BooleanSetting("Totem Pop", true)
    private val city = BooleanSetting("City", true)
    private val armor = BooleanSetting("Armor", true)
    private val armorThreshold = IntSetting("Armor %", 36, 0, 100)
    private val showPercent = BooleanSetting("Show Percent", true)
    private val armorCategory = ParentSetting("Armor", armor, armorThreshold, showPercent)

    private val lastDura = intArrayOf(100, 100, 100, 100)

    @EventListener
    fun onTotemPop() = listener<PopEvent> { event ->
        if (totems.value) {
            // TODO: 3/3/2022 friends
            val name = Format.RED.toString() + event.entity.name + Settings.textColor.format
            // Entity hashcode so each player has a unique message id
            message("$name has popped {} totems", PopManager.getPopsForPlayer(event.entity).toString(), event.entity.hashCode())
        }
    }

    @EventListener
    fun onPacketReceived() = listener<PacketEvent.Receive> { event ->
        if (city.value && event.packet is SPacketBlockBreakAnim) {
            if (event.packet.position.floored in PlayerUtil.getSurroundingBlocks()) {
                message("Your city block is being broken!")
            }
        }
    }

    @EventListener
    fun onDamage() = listener<LivingDamageEvent>(parallel = true) { event ->
        if (armor.value && event.entity == mc.player) mc.player.armorInventoryList.forEach {
            val item = it.item
            if (item is ItemArmor) {
                val durability = it.damagePercent
                val slot = item.equipmentSlot
                if (armorThreshold.value in durability until lastDura[slot.slotIndex - 1]) {
                    val name = slot.stackName
                    val verb = if (name.endsWith("s")) "are" else "is"
                    val state = if (showPercent.value) "at {} percent" else "low"
                    message("Your $name $verb $state!", durability)
                }
                lastDura[slot.slotIndex - 1] = durability
            }
        }
    }
}
