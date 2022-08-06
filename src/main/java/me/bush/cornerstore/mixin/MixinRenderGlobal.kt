package me.bush.cornerstore.mixin

import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.events.HighlightEvent
import net.minecraft.block.material.Material
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.RayTraceResult
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

/**
 * @author bush
 * @since 3/8/2022
 */
@Mixin(RenderGlobal::class)
abstract class MixinRenderGlobal {

    @Shadow
    private lateinit var world: WorldClient

    @Inject(method = ["drawSelectionBox"], at = [At("HEAD")], cancellable = true)
    fun drawSelectionBox(player: EntityPlayer, result: RayTraceResult, execute: Int, partialTicks: Float, callback: CallbackInfo) {
        val event = HighlightEvent()
        EventBus.post(event)

        // TODO: 3/8/2022 redo this later, mixin based is just proof of concept for the vanilla style
        // Kinda bad to copy the code here, but we don't have to deal with redirect fuckery or
        // an override that would fuck over other clients, so it works well enough.
        if (event.cancelled) callback.cancel() else if (event.changed) {
            if (execute == 0 && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                GlStateManager.enableBlend()
                GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
                )
                GlStateManager.glLineWidth(event.width)
                GlStateManager.disableTexture2D()
                GlStateManager.depthMask(false)
                val blockpos = result.blockPos
                val iblockstate = world.getBlockState(blockpos)
                if (iblockstate.material !== Material.AIR && world.worldBorder.contains(blockpos)) {
                    val x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks
                    val y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks
                    val z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks
                    val bb = iblockstate.getSelectedBoundingBox(world, blockpos).grow(event.grow).offset(-x, -y, -z)
                    //drawSelectionBoundingBox(bb, event.red, event.green, event.blue, event.alpha)
                }
                GlStateManager.depthMask(true)
                GlStateManager.enableTexture2D()
                GlStateManager.disableBlend()
            }
            callback.cancel()
        }
    }

    // TODO: 4/24/2022

//    companion object {
//
//        @Shadow
//        @JvmStatic
//        external fun drawSelectionBoundingBox(box: AxisAlignedBB?, red: Float, green: Float, blue: Float, alpha: Float)
//    }
}
