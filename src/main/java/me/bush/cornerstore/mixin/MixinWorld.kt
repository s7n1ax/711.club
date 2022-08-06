package me.bush.cornerstore.mixin

import me.bush.cornerstore.impl.module.modules.render.NoRender
import me.bush.cornerstore.util.lang.invoke
import net.minecraft.world.World
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

/**
 * @author bush
 * @since 3/1/2022
 */
@Mixin(World::class)
class MixinWorld {

    @Inject(method = ["getThunderStrength"], at = [At("HEAD")], cancellable = true)
    private fun getThunderStrengthHead(strength: Float, callback: CallbackInfoReturnable<Float>) {
        if (NoRender.enabled && NoRender.noweather.value) callback(0f)
    }

    @Inject(method = ["getRainStrength"], at = [At("HEAD")], cancellable = true)
    private fun getRainStrengthHead(strength: Float, callback: CallbackInfoReturnable<Float>) {
        if (NoRender.enabled && NoRender.noweather.value) callback(0f)
    }
}
