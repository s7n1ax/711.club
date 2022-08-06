package me.bush.cornerstore.impl.module.modules.misc

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.minecraft.isHoldingBlock
import me.bush.cornerstore.util.minecraft.isHoldingItem
import me.bush.eventbuskotlin.EventListener
import me.bush.eventbuskotlin.listener
import net.minecraft.block.BlockObsidian
import net.minecraft.item.*
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

/**
 * @author bush
 * @since 3/2/2022
 */
object FastUse : Module("Use stuff faster") {
    private val all = BooleanSetting("All", false)
    private val exp = BooleanSetting("EXP Bottles", true)
    private val obsidian = BooleanSetting("Obsidian", false)
    private val blocks = BooleanSetting("All Blocks", false)
    private val crystals = BooleanSetting("Crystals", false)

    @EventListener
    fun onClientTick() = listener<ClientTickEvent> {
        if (it.phase == TickEvent.Phase.END && shouldFastPlace()) {
            mc.rightClickDelayTimer = 1
        }
    }

    private fun shouldFastPlace() = mc.player?.run {
        all.value ||
            (exp.value && isHoldingItem<ItemExpBottle>()) ||
            (obsidian.value && isHoldingBlock<BlockObsidian>()) ||
            (blocks.value && isHoldingItem<ItemBlock>()) ||
            (crystals.value && isHoldingItem<ItemEndCrystal>())
    } ?: false
}
