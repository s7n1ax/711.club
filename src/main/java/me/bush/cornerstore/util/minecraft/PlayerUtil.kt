package me.bush.cornerstore.util.minecraft

import me.bush.cornerstore.api.common.mc
import net.minecraft.block.Block
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import kotlin.math.floor

/*
 * @author bush
 * @since 3/2/2022
 */

val EntityPlayerSP.isInLiquid get() = isInLava || isInWater

val EntityPlayerSP.isAboveLiquid: Boolean
    get() {
        var pos = position.down()
        while (pos.y > 0 && world.isAirBlock(pos)) {
            pos = pos.down()
            if (pos.state.material.isLiquid) return true
        }
        return false
    }

/**
 * Different from mc.player.fallHeight, this calculates how many air blocks are below the player
 */
val EntityPlayerSP.heightAboveGround: Int
    get() {
        var height = 0
        while (world.isAirBlock(position.down(height + 1))) height++
        return height
    }

val EntityPlayerSP.formattedCoords: String get() = "X: ${position.x}, Y: ${position.y}, Z: ${position.z}"

val EntityPlayerSP.heldItemName: String
    get() {
        return if (heldItemMainhand.item == Items.AIR) "nothing"
        else "${heldItemMainhand.count} ${heldItemMainhand.displayName}${if (heldItemMainhand.count == 1) "" else "s"}"
    }

inline fun <reified T : Item> EntityPlayerSP.isHoldingItem() = heldItemOffhand.item is T || heldItemMainhand.item is T

inline fun <reified T : Block> EntityPlayerSP.isHoldingBlock() = heldItemOffhand.item.block is T || heldItemMainhand.item.block is T

object PlayerUtil {
    private fun getFlooredPos(): BlockPos {
        return BlockPos(
            floor(mc.player.posX), floor(mc.player.posY),
            floor(mc.player.posZ)
        )
    }

    fun getSurroundingBlocks(): MutableList<BlockPos> {
        val list: MutableList<BlockPos> = mutableListOf()
        list.add(getFlooredPos().north())
        list.add(getFlooredPos().east())
        list.add(getFlooredPos().west())
        list.add(getFlooredPos().south())
        return list
    }
}
