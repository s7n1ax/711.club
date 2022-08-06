package me.bush.cornerstore.mixin

import me.bush.cornerstore.impl.module.modules.render.ViewModel
import me.bush.cornerstore.util.lang.invoke
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHandSide
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

/**
 * @author bush
 * @since 5/9/2022
 */
@Mixin(ItemRenderer::class)
class MixinItemRenderer {

    @Inject(method = ["rotateArm"], at = [At("HEAD")], cancellable = true)
    fun rotateArm(partialTicks: Float, callback: CallbackInfo) {
        if (ViewModel.enabled && ViewModel.noSway) callback()
    }

    @Inject(method = ["renderItemInFirstPerson"], at = [At("HEAD")])
    fun renderItemInFirstPerson(partialTicks: Float, callback: CallbackInfo) {
        if (ViewModel.enabled) ViewModel.renderItemInFirstPerson()
    }

    @Inject(method = ["transformSideFirstPerson"], at = [At("TAIL")])
    fun transformSideFirstPerson(hand: EnumHandSide, equipProgress: Float, callback: CallbackInfo) {
        if (ViewModel.enabled) ViewModel.transformSideFirstPerson(hand, equipProgress)
    }

    @Inject(method = ["transformEatFirstPerson"], at = [At("HEAD")], cancellable = true)
    fun transformEatFirstPerson(partialTicks: Float, hand: EnumHandSide, stack: ItemStack, callback: CallbackInfo) {
        if (ViewModel.enabled) {
            ViewModel.transformEatFirstPerson(partialTicks, hand, stack)
            callback()
        }
    }
}
