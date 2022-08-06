package me.bush.cornerstore.util.render

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.util.math.Vector
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.GlStateManager.*
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormat
import org.lwjgl.opengl.GL11.*
import java.awt.Color

/**
 * @author bush
 * @since fall 2021
 */
object Render2DUtil {

    /*

    "Below/Above" refers to the z value relative to another object

    "Before/After" refers to the time an object was rendered in the current frame

    +-------+----------------+-----------------------+
    |       | Before         | After                 |
    +-------+----------------+-----------------------+
    | Below | Blends behind  | Isn't visible behind  |
    |       | other object   | other object          |
    +-------+----------------+-----------------------+
    | Above | Other object   | Other object          |
    |       | isn't visible  | blends through        |
    +-------+----------------+-----------------------+

    Anything on the same level will blend

     */

    // Overloaded methods
    fun fillRect(v: Vector, z: Float, c: Int) {
        fillRect(v.x, v.y, v.w, v.h, z, c)
    }

    fun outlineRect(v: Vector, lw: Float, z: Float, c: Int) {
        outlineRect(v.x, v.y, v.w, v.h, lw, z, c)
    }

    fun gradientFillRect(v: Vector, z: Float, start: Int, end: Int, vertical: Boolean) {
        gradientFillRect(v.x, v.y, v.w, v.h, z, start, end, vertical)
    }

    fun gradientOutlineRect(v: Vector, lw: Float, z: Float, start: Int, end: Int, vertical: Boolean) {
        gradientOutlineRect(v.x, v.y, v.w, v.h, lw, z, start, end, vertical)
    }

    fun hueRect(v: Vector, z: Float, s: Float, b: Float, vertical: Boolean) {
        hueRect(v.x, v.y, v.w, v.h, z, s, b, vertical)
    }

    fun checkeredRect(v: Vector, z: Float, size: Float, color1: Int, color2: Int) {
        checkeredRect(v.x, v.y, v.w, v.h, z, size, color1, color2)
    }

    // Actual methods
    fun fillRect(x: Float, y: Float, w: Float, h: Float, z: Float, c: Int) {
        val (a, r, g, b) = c
        enableBlend()
        disableTexture2D()
        tryBlendFuncSeparate(
            SourceFactor.SRC_ALPHA,
            DestFactor.ONE_MINUS_SRC_ALPHA,
            SourceFactor.ONE,
            DestFactor.ZERO
        )
        draw(GL_QUADS, DefaultVertexFormats.POSITION_COLOR) {
            pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        enableTexture2D()
        disableBlend()
    }

    fun outlineRect(x: Float, y: Float, w: Float, h: Float, lw: Float, z: Float, c: Int) {
        // Just draws rectangles for lines lol
        fillRect(x, y, w - lw, lw, z, c)
        fillRect(x, y + lw, lw, h - lw, z, c)
        fillRect(x + lw, y + h - lw, w - lw, lw, z, c)
        fillRect(x + w - lw, y, lw, h - lw, z, c)
    }

    fun gradientFillRect(x: Float, y: Float, w: Float, h: Float, z: Float, start: Int, end: Int, vertical: Boolean) {
        val (a1, r1, g1, b1) = start
        val (a2, r2, g2, b2) = end
        disableTexture2D()
        enableBlend()
        disableAlpha()
        tryBlendFuncSeparate(
            SourceFactor.SRC_ALPHA,
            DestFactor.ONE_MINUS_SRC_ALPHA,
            SourceFactor.ONE,
            DestFactor.ZERO
        )
        shadeModel(GL_SMOOTH)
        draw(GL_QUADS, DefaultVertexFormats.POSITION_COLOR) {
            if (vertical) {
                pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r1, g1, b1, a1).endVertex()
                pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r1, g1, b1, a1).endVertex()
                pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r2, g2, b2, a2).endVertex()
                pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r2, g2, b2, a2).endVertex()
            } else {
                pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r1, g1, b1, a1).endVertex()
                pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r1, g1, b1, a1).endVertex()
                pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r2, g2, b2, a2).endVertex()
                pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r2, g2, b2, a2).endVertex()
            }
        }
        shadeModel(GL_FLAT)
        disableBlend()
        enableAlpha()
        enableTexture2D()
    }

    fun gradientOutlineRect(x: Float, y: Float, w: Float, h: Float, lw: Float, z: Float, start: Int, end: Int, vertical: Boolean) {
        withStencil(false, { outlineRect(x, y, w, h, lw, z, -0x1) }) {
            gradientFillRect(x, y, w, h, z, start, end, vertical)
        }
    }


    /**
     * Draws what was rendered in the second block only behind anything drawn in the first block,
     * or only where nothing was drawn in the first block if `invert` is enabled. The stencil can
     * be drawn with any color.
     *
     * `https://open.gl/depthstencils`
     */
    inline fun withStencil(invert: Boolean, stencil: () -> Unit, draw: () -> Unit) {
        // Not a performance problem to check every frame, isStencilEnabled() just gets a boolean
        mc.framebuffer.run { if (!isStencilEnabled) enableStencil() }
        // Enable stencil test
        glEnable(GL_STENCIL_TEST)
        // Clear previous buffer data
        glClear(GL_STENCIL_BUFFER_BIT)
        // Setup writing to the buffer
        glStencilMask(0xFF)
        glStencilFunc(GL_ALWAYS, 1, 0xFF)
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE)
        // Don't write to the screen
        glDepthMask(false)
        glColorMask(false, false, false, false)
        // Draw stencil shape
        stencil()
        // Disable writing to stencil buffer
        glStencilMask(0x00)
        // Write to screen
        glDepthMask(true)
        glColorMask(true, true, true, true)
        // Only render where the previous outline was drawn
        glStencilFunc(if (invert) GL_EQUAL else GL_NOTEQUAL, 0, 0xFF)
        // Draw stuff, will only work within where the stencil was
        draw()
        // Disable stencil test
        glDisable(GL_STENCIL_TEST)
    }

    fun hueRect(x: Float, y: Float, w: Float, h: Float, z: Float, s: Float, b: Float, vertical: Boolean) {
        val quality = 6 // RED orange YELLOW green BLUE purple
        val factor = if (vertical) h / quality else w / quality
        for (step in 0 until 6) {
            val previous = Color.HSBtoRGB((step / quality.toFloat()), s, b)
            val next = Color.HSBtoRGB(((step + 1) / quality.toFloat()), s, b)
            if (vertical) gradientFillRect(x, y + step * factor, w, factor, z, previous, next, true)
            else gradientFillRect(x + step * factor, y, factor, h, z, previous, next, false)
        }
    }

    fun checkeredRect(x: Float, y: Float, w: Float, h: Float, z: Float, size: Float, color1: Int, color2: Int) {
        fillRect(x, y, w, h, z, color1)
        var checker: Boolean
        var currentY = y
        while (currentY < y + h) {
            // Starts with checker on every other level
            checker = (currentY - y) / size % 2 == 1f
            var currentX = x
            while (currentX < x + w) {
                if (checker) {
                    // If checker would go over our width/height, trim it
                    val currentW = if (currentX - x + size >= w) {
                        w - (currentX - x)
                    } else size
                    val currentH = if (currentY - y + size > h) {
                        h - (currentY - y)
                    } else size
                    fillRect(currentX, currentY, currentW, currentH, z, color2)
                }
                checker = !checker
                currentX += size
            }
            currentY += size
        }
    }

    private inline fun draw(mode: Int, format: VertexFormat, block: BufferBuilder.() -> Unit) {
        Tessellator.getInstance().let {
            it.buffer.begin(mode, format)
            it.buffer.block()
            it.draw()
        }
    }

    private operator fun Int.component1() = (this shr 24 and 255) / 255.0f
    private operator fun Int.component2() = (this shr 16 and 255) / 255.0f
    private operator fun Int.component3() = (this shr 8 and 255) / 255.0f
    private operator fun Int.component4() = (this and 255) / 255.0f
}
