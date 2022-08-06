package me.bush.cornerstore.util.render

import net.minecraft.client.renderer.GlStateManager

object Render3DUtil {

    fun drawBox() {
    }

    fun rotateEuler(x: Float, y: Float, z: Float) {
        GlStateManager.rotate(y, 0f, 1f, 0f)
        GlStateManager.rotate(x, 1f, 0f, 0f)
        GlStateManager.rotate(z, 0f, 0f, 1f)
    }
}
