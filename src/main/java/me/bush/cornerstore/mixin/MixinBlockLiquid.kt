package me.bush.cornerstore.mixin

import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.events.PushEvent
import me.bush.cornerstore.api.event.events.PushEvent.Type.LIQUID
import me.bush.cornerstore.util.lang.invoke
import net.minecraft.block.BlockLiquid
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

/**
 * @author bush
 * @since 2/3/2022
 */
@Mixin(BlockLiquid::class)
class MixinBlockLiquid {

    @Inject(method = ["modifyAcceleration"], at = [At("HEAD")], cancellable = true)
    fun modifyAcceleration(worldIn: World, pos: BlockPos, entityIn: Entity, motion: Vec3d, callback: CallbackInfoReturnable<Vec3d>) {
        if (entityIn == Minecraft.getMinecraft().player && EventBus.post(PushEvent(LIQUID))) callback(motion)
    }
}
