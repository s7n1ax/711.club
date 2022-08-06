package me.bush.cornerstore.impl.gui.clickgui.themes

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.font.FontRenderer
import me.bush.cornerstore.api.gui.Component
import me.bush.cornerstore.api.gui.Theme
import me.bush.cornerstore.api.gui.component.*
import me.bush.cornerstore.api.gui.component.special.TextAreaComponent
import me.bush.cornerstore.impl.module.modules.client.ClickGui
import me.bush.cornerstore.impl.module.modules.client.Font
import me.bush.cornerstore.util.math.Vector
import me.bush.cornerstore.util.render.Color
import me.bush.cornerstore.util.render.Render2DUtil

/**
 * somewhat skidded from PhobosTheme.kt
 *
 * last updated - 2/22/2022 12:44PM (GMT+ 7)
 * @author noat
 * @since 2/22/2022 9:39AM (GMT+ 7)
 */
class SexHackTheme : Theme {
    override val name = "SexHack"

    override val windowHeaderHeight = 18f
    override val windowFooterHeight = 1f
    override val windowBorderWidth = 1f

    override val featureHeaderHeight = 15f
    override val featureFooterHeight = 2f
    override val featureBorderWidth = 4f

    override val defaultHeaderHeight = 14f
    override val defaultFooterHeight = 2f
    override val defaultBorderWidth = 2f

    override val windowTextOffset = 4f
    override val featureTextOffset = 3f
    override val defaultTextOffset = 1f

    override fun drawWindowComponent(component: WindowComponent) {
        // outline
        Render2DUtil.fillRect(
            component.vec.x - 1,
            component.vec.y - 1,
            +component.vec.w + 2,
            component.vec.h + 2,
            -5f,
            Color(30, 30, 30).rgba
        )
        // base from PhobosTheme.drawWindowComponent
        val header = component.vec with { h = windowHeaderHeight }
        Render2DUtil.fillRect(header, 0f, Color(30, 30, 30).rgba)
        mc.fontRenderer.drawString(component.name, header.x + 3, header.y + 5, Color(255, 255, 255).rgba, false)
    }

    override fun drawFeatureComponent(component: FeatureComponent) {
        val header = component.vec with { h = featureHeaderHeight }
        Render2DUtil.fillRect(header, 0f, if (component.enabled) Color(75, 75, 75).rgba else Color(44, 44, 44).rgba)
        mc.fontRenderer.drawString(
            component.name,
            header.x + 3,
            header.y + 5,
            if (component.enabled) Color(255, 255, 255).rgba else Color(177, 177, 177).rgba,
            false
        )
        mc.fontRenderer.drawString(
            if (component.open) "-" else "+",
            (header.x + header.w) - 7,
            header.y + 5,
            Color(255, 255, 255).rgba,
            false
        )
    }

    override fun drawParentComponent(component: ParentComponent) {
        val header = component.vec with { h = defaultHeaderHeight }
        Render2DUtil.fillRect(header, 0f, Color(30, 30, 30).rgba)
        mc.fontRenderer.drawString(component.name, header.x - 2, header.y + 5, Color(255, 255, 255).rgba, false)
        mc.fontRenderer.drawString(
            if (component.open) "-" else "+",
            (header.x + component.vec.w) - 3,
            header.y + 5,
            Color(255, 255, 255).rgba,
            false
        )
    }

    override fun drawTextComponent(component: ParentComponent, value: String) {
        val header = component.vec with { h = defaultHeaderHeight }
        mc.fontRenderer.drawString(
            component.name,
            (header.x - 2).toInt(),
            (header.y + 5).toInt(),
            Color(255, 255, 255).rgba
        )
        mc.fontRenderer.drawString(
            value,
            (header.x + header.w) - (mc.fontRenderer.getStringWidth(value)),
            header.y + 5,
            Color(177, 177, 177).rgba,
            false
        )

    }

    override fun drawColorComponent(component: ParentComponent, value: Color) {
        val header = component.vec with { h = defaultHeaderHeight }
        mc.fontRenderer.drawString(
            component.name,
            (header.x - 2).toInt(),
            (header.y + 5).toInt(),
            Color(255, 255, 255).rgba
        )
        Render2DUtil.outlineRect((header.x + header.w) - 3, header.y + 5, 5f, 5f, 1.06f, -1f, value.rgba)
        Render2DUtil.fillRect((header.x + header.w) - 3, header.y + 5, 5f, 5f, -1f, value.rgba)
    }

    override fun drawActionComponent(component: Component) {
        val header = component.vec with { h = defaultHeaderHeight }
        mc.fontRenderer.drawString(
            component.name,
            (header.x - 2).toInt(),
            (header.y + 5).toInt(),
            Color(255, 255, 255).rgba
        )
    }

    override fun drawToggleComponent(component: Component, value: Boolean) {
        val header = component.vec with { h = defaultHeaderHeight }
        mc.fontRenderer.drawString(
            component.name,
            (header.x - 2).toInt(),
            (header.y + 5).toInt(),
            Color(255, 255, 255).rgba
        )
        Render2DUtil.outlineRect((header.x + header.w) - 3, header.y + 5, 5f, 5f, 1.06f, -1f, Color(0, 0, 0).rgba)
        Render2DUtil.fillRect((header.x + header.w) - 3, header.y + 5, 5f, 5f, -1f, if (value) Color(0, 255, 0).rgba else Color(255, 0, 0).rgba)

    }

    override fun drawValueComponent(component: Component, value: String) {
        val header = component.vec with { h = defaultHeaderHeight }
        mc.fontRenderer.drawString(
            component.name,
            (header.x - 2).toInt(),
            (header.y + 5).toInt(),
            Color(255, 255, 255).rgba
        )
        mc.fontRenderer.drawString(
            value,
            (header.x + header.w) - (mc.fontRenderer.getStringWidth(value)),
            header.y + 5,
            Color(177, 177, 177).rgba,
            false
        )
    }

    override fun drawSliderComponent(component: Component, value: String, fill: Float) {
        // skidded from phobostheme :3
        val header = component.vec with { h = defaultHeaderHeight }
        mc.fontRenderer.drawString(
            component.name,
            (header.x - 2).toInt(),
            (header.y + 3).toInt(),
            Color(255, 255, 255).rgba
        )
        mc.fontRenderer.drawString(
            value,
            (header.x + header.w) - (mc.fontRenderer.getStringWidth(value)),
            header.y + 3,
            Color(177, 177, 177).rgba,
            false
        )
        Render2DUtil.outlineRect(header.x - 2, (header.y + header.h) - 3, header.w, 3f, 0.5f, -2f, Color(0, 0, 0).rgba)
        Render2DUtil.fillRect(header.x - 2, (header.y + header.h) - 3, fill * header.w, 3f, -3f, Color(255, 255, 255).rgba)
    }

    override fun drawDualSliderComponent(component: Component, vertical: Float, horizontal: Float) {
        // pasted lol
        val height = (component.vec.w) * vertical
        val width = (component.vec.w) * horizontal
        val offset = component.vec.y + component.vec.h - height
        val slider = component.vec with { y = offset; w = width; h = height }
        Render2DUtil.outlineRect(slider, 0.47f, -1f, ClickGui.outline.rgba)
        Render2DUtil.fillRect(slider, -2f, ClickGui.color.rgba)
    }

    override fun drawTextAreaComponent(component: TextAreaComponent) {
        var height = component.vec.y + 3
        component.getTextLines((component.vec.w - defaultTextOffset * 2).toInt()).forEach {
            mc.fontRenderer.drawString(it, component.vec.x + defaultTextOffset, height, Color(255, 255, 255).rgba, false)
            height += FontRenderer.FONT_HEIGHT
        }
    }

    override fun drawColorPickerSlider(component: Component, fill: Float, renderer: (Vector) -> Unit) {
        val slider = component.vec with { h -= 1.47f }
        val pos = (slider.w - 1) * fill
        val caret = slider with { x += pos; w = 1f; }
        Render2DUtil.fillRect(caret, 0f, Font.primaryText.rgba)
        Render2DUtil.outlineRect(slider, 0.53f, -1f, ClickGui.outline.rgba)
        renderer(slider)
    }

    override fun drawColorPickerRect(
        component: Component,
        vertical: Float,
        horizontal: Float,
        renderer: (Vector) -> Unit
    ) {
        val picker = component.vec with { y -= 0.53f; h += 1.03f }
        Render2DUtil.outlineRect(picker, 0.53f, 0f, ClickGui.outline.rgba)
        val pointX = component.vec.x + horizontal * (component.vec.w)
        val pointY = component.vec.y + component.vec.h - vertical * component.vec.h
        Render2DUtil.outlineRect((pointX - 1f), (pointY - 1f), 2f, 2f, 0.47f, 0f, Font.primaryText.rgba)
        renderer(picker)
    }
}
