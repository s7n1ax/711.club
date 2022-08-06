package me.bush.cornerstore.impl.gui.hud

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.bush.cornerstore.api.common.Feature
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.font.FontRenderer
import me.bush.cornerstore.api.gui.Component
import me.bush.cornerstore.api.gui.DragHandler
import me.bush.cornerstore.impl.module.modules.client.ClickGui
import me.bush.cornerstore.impl.module.modules.client.Font
import me.bush.cornerstore.util.math.Vector
import me.bush.cornerstore.util.render.Render2DUtil
import net.minecraft.client.gui.ScaledResolution
import java.text.DecimalFormat

/**
 * @author bush
 * @since 3/5/2022
 */
abstract class HudModule(
    description: String,
    // Set these to a negative number to
    // align them from the right/bottom
    val defaultX: Int,
    val defaultY: Int
) : Feature(description), Component {
    final override val category get() = "Display"
    final override val vec = Vector()
    private val dragHandler = DragHandler(vec, ::isMouseOver)
    final override val visible = true
    final override val tooltip = description
    protected val textHeight get() = FontRenderer.FONT_HEIGHT
    private var lastW = 0f
    private var lastH = 0f
    protected val alignV
        get() = if (vec.y + vec.h / 2 < SR.scaledHeight / 2) 0 else 2
    protected val alignH: Int
        get() {
            val half = SR.scaledWidth / 2
            return if (vec.x + vec.w < half) 0
            else if (vec.x > half) 2
            else 1
        }

    abstract val width: Int
    abstract val height: Int

    protected abstract fun drawScreen()

    protected fun textWidth(string: String) = FontRenderer.getStringWidth(string)

    protected fun drawString(string: String) {
        // TODO: 3/6/2022 color code thing
        FontRenderer.drawString(string, vec, Font.primaryText.rgba, alignH)
    }

    fun resetPos() {
        defaultX.toFloat().let {
            vec.x = if (it < 0) SR.scaledWidth + it - width else it
        }
        defaultY.toFloat().let {
            vec.y = if (it < 0) SR.scaledHeight + it - height else it
        }
    }

    final override fun drawScreen(mouse: Vector?) {
        if (!enabled) return
        vec.w = width.toFloat()
        vec.h = height.toFloat()
        if (lastW != 0f && lastW != vec.w) {
            val offset = when (alignH) {
                2 -> vec.w - lastW
                1 -> (vec.w - lastW) / 2
                else -> 0f
            }
            vec.x -= offset
        }
        if (lastH != 0f && lastH != vec.h) {
            val offset = if (alignV == 2) vec.h - lastH else 0f
            vec.y -= offset
        }
        // Mouse is only nonnull in hudeditor
        mouse?.let {
            dragHandler.handleDrawScreen(it)
            drawScreen()
            val outline = vec with { expand(1f) }
            if (isMouseOver(it)) {
                Render2DUtil.outlineRect(outline, 0.47f, 0f, Font.primaryText.rgba)
            }
            Render2DUtil.fillRect(vec with { expand(1f) }, -1f, ClickGui.bgColor.rgba)
        } ?: drawScreen()
        lastW = vec.w
        lastH = vec.h
    }

    final override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        if (!enabled) return
        dragHandler.handleMouseClicked(mouse, button, clicked)
    }

    final override fun keyTyped(typedChar: Char, keyCode: Int) {}

    final override fun fromConfig(config: JsonObject) {
        config.get("Position")?.asJsonArray?.let {
            vec.x = it[0].asFloat
            vec.y = it[1].asFloat
            lastW = it[2].asFloat
            lastH = it[3].asFloat
        }
    }

    final override fun toConfig(config: JsonObject) {
        config.add("Position", JsonArray().apply {
            add(vec.x)
            add(vec.y)
            add(lastW)
            add(lastH)
        })
    }

    companion object {
        val DF = DecimalFormat("0.0")
        val SR get() = ScaledResolution(mc)
    }
}
