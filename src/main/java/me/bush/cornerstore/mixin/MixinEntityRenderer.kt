package me.bush.cornerstore.mixin

import me.bush.cornerstore.impl.module.modules.render.NoRender
import me.bush.cornerstore.util.lang.invoke
import net.minecraft.client.renderer.EntityRenderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

/**
 * @author perry.
 * Started 11/19/2021.
 */
@Mixin(EntityRenderer::class)
class MixinEntityRenderer {

    @Inject(method = ["hurtCameraEffect"], at = [At("HEAD")], cancellable = true)
    fun hurtCameraEffect(partialTicks: Float, callback: CallbackInfo) {
        if (NoRender.enabled && NoRender.hurtcam.value) callback()
    }
}
