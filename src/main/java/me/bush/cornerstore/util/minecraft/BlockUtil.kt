package me.bush.cornerstore.util.minecraft

import me.bush.cornerstore.api.common.mc
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import kotlin.math.floor

/*
 * @author bush
 * @since 3/2/2022
 */

val Item.block: Block get() = Block.getBlockFromItem(this)

val BlockPos.state: IBlockState get() = mc.world.getBlockState(this)

val BlockPos.floored: BlockPos
    get() = BlockPos(floor(this.x.toDouble()), floor(this.y.toDouble()), floor(this.z.toDouble()))

val BlockPos.isReplaceable
    get() = mc.world.getBlockState(this).material.isReplaceable

val BlockPos.isAirBlock
    get() = mc.world.isAirBlock(this)
