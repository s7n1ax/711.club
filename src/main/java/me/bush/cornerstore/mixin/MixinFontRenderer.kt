package me.bush.cornerstore.mixin

import me.bush.cornerstore.impl.module.modules.client.NameChanger
import me.bush.cornerstore.util.lang.invoke
import net.minecraft.client.gui.FontRenderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

/**
 * @author bush
 * @since 2/19/2022
 */
@Mixin(FontRenderer::class)
abstract class MixinFontRenderer {

    @Shadow
    protected abstract fun renderString(text: String, x: Float, y: Float, color: Int, dropShadow: Boolean): Int

    @Shadow
    abstract fun getStringWidth(text: String): Int

    @Shadow
    abstract fun trimStringToWidth(text: String, width: Int, reverse: Boolean): String

    @Inject(method = ["renderString"], at = [At("HEAD")], cancellable = true)
    private fun renderString(text: String, x: Float, y: Float, color: Int, dropShadow: Boolean, callback: CallbackInfoReturnable<Int>) {
        callback(renderString(NameChanger.changeString(text) ?: return, x, y, color, dropShadow))
    }

    @Inject(method = ["getStringWidth"], at = [At("HEAD")], cancellable = true)
    private fun getStringWidth(text: String, callback: CallbackInfoReturnable<Int>) {
        callback(getStringWidth(NameChanger.changeString(text) ?: return))
    }

    @Inject(method = ["trimStringToWidth(Ljava/lang/String;IZ)Ljava/lang/String;"], at = [At("HEAD")], cancellable = true)
    private fun trimStringToWidth(text: String, width: Int, reverse: Boolean, callback: CallbackInfoReturnable<String>) {
        callback(trimStringToWidth(NameChanger.changeString(text) ?: return, width, reverse))
    }
}
