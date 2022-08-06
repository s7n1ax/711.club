package me.bush.cornerstore.api.font

import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.impl.module.modules.client.Font
import me.bush.cornerstore.util.math.Vector
import net.minecraft.client.resources.SimpleReloadableResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraft.client.gui.FontRenderer as MinecraftFontRenderer

/**
 * @author bush
 * @since fall 2021
 */
object FontRenderer : MinecraftFontRenderer(mc.gameSettings, ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false), Manager {

    fun drawString(text: String, vec: Vector, color: Int, align: Int) {
        var x = vec.x
        val w = getStringWidth(text)
        if (align > 0) {
            var i = vec.w - w
            if (align == 1) i /= 2f
            x += i
        }
        drawString(text, x, vec.y, color, Font.shadow.value)
    }

    override suspend fun primaryLoad() {
        (mc.resourceManager as SimpleReloadableResourceManager).registerReloadListener(this)
    }
}
