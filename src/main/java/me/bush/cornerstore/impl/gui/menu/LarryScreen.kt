package me.bush.cornerstore.impl.gui.menu

import me.bush.cornerstore.api.font.FontRenderer
import me.bush.cornerstore.util.render.Color
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager

class LarryScreen : GuiScreen() {
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        // todo: make a rectangle under it so it can have cool hover effects!
        FontRenderer.drawString("singleplayer", 30, 400, Color(255, 255, 255).rgba)
        FontRenderer.drawString("multiplayer", 100, 400, Color(255, 255, 255).rgba)
        FontRenderer.drawString("settings", 200, 400, Color(255, 255, 255).rgba)
        FontRenderer.drawString("quit", 300, 400, Color(255, 0, 0).rgba)

        // scale or smth idfk
        GlStateManager.scale(4.0f, 4f, 4f)
        FontRenderer.drawString("larry.club", 10, 25, Color(255, 255, 255).rgba)
        GlStateManager.popMatrix()
    }
}
