package me.bush.cornerstore.util.minecraft

import me.bush.cornerstore.util.math.MathUtil
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import kotlin.random.Random

/**
 * @author bush
 * @since 2/1/2022
 */

// i swear i didn't fail grade 2 math
val ItemStack.damagePercent get() = (MathUtil.getPercent(0f, itemDamage.toFloat(), maxDamage.toFloat()) * -100).toInt() + 100

val EntityEquipmentSlot.stackName
    get() = when (slotIndex) {
        0 -> "mainhand"
        1 -> "boots"
        2 -> "leggings"
        3 -> "chestplate"
        4 -> "helmet"
        else /* 5 */ -> "offhand"
    }

fun InventoryPlayer.randomizeHotbar() {
    currentItem = Random.nextInt(9)
}

object InventoryUtil
