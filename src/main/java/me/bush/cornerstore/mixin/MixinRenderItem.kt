package me.bush.cornerstore.mixin

import me.bush.cornerstore.impl.module.modules.render.ViewModel
import me.bush.cornerstore.util.lang.invoke
import net.minecraft.client.renderer.RenderItem
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo


/**
 * @author bush
 * @since 5/10/2022
 */
@Mixin(RenderItem::class)
abstract class MixinRenderItem {

    @Inject(
        at = [At("HEAD")], cancellable = true, method = ["renderItem(Lnet/minecraft/item/ItemStack;" +
            "Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms\$TransformType;Z)V"]
    )
    fun renderItem(
        stack: ItemStack,
        entity: EntityLivingBase,
        transform: ItemCameraTransforms.TransformType,
        left: Boolean,
        callback: CallbackInfo
    ) {
        if (ViewModel.enabled && ViewModel.alpha != 255) {
            ViewModel.renderItem(stack, entity, transform, left)
            callback()
        }
    }
}
