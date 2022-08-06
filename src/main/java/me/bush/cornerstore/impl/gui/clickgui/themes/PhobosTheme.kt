package me.bush.cornerstore.impl.gui.clickgui.themes

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
 * @author bush
 * @since 12/21/2021
 */
class PhobosTheme : Theme {
    override val name = "Phobos"

    override val windowHeaderHeight = 18f
    override val windowFooterHeight = 1f
    override val windowBorderWidth = 1f

    // TODO: 3/7/2022 unfuck
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
        val header = component.vec with { h = windowHeaderHeight }
        drawText(component.name, header.x + windowTextOffset, header.y + 5, true, false)
        // Outline behind window header
        Render2DUtil.outlineRect(header, 0.53f, 0f, ClickGui.outline.rgba)
        Render2DUtil.fillRect(header, -1f, ClickGui.color.rgba)
        // Background behind all features
        if (component.open) Render2DUtil.fillRect(component.vec, -4f, ClickGui.bgColor.rgba)
    }

    override fun drawFeatureComponent(component: FeatureComponent) {
        val outline = component.vec with { y += 1f; h -= 1f }
        val name = trimValue(component, "+", component.name)
        // Name and +/-
        drawText(name, outline.x + featureTextOffset, outline.y + 3, component.enabled, false)
        drawText(if (component.open) "-" else "+", outline.x + outline.w - featureTextOffset, outline.y + 3.5f, component.enabled, true)
        // Outline around entire feature
        Render2DUtil.outlineRect(outline, 0.53f, 0f, ClickGui.outline.rgba)
        // Drawing the inner outline thing underneath the module name
        if (component.open) {
            // So top of inner header is at the same height as bottom of closed module
            val height = featureHeaderHeight - 3.53f
            // Get vec that excludes header
            val inner = outline with { y += height; h -= height }
            // Draw inner feature outline
            Render2DUtil.outlineRect(inner with { shrink(2f) }, 0.53f, 0f, ClickGui.outline.rgba)
            // Draw fill between both outlines
            if (component.enabled) Render2DUtil.outlineRect(inner, 2f, -1f, ClickGui.color.rgba)
        }
        // Fill for header
        if (component.enabled) Render2DUtil.fillRect(outline with { h = featureHeaderHeight - 1f }, -2f, ClickGui.color.rgba)
    }

    override fun drawParentComponent(component: ParentComponent) {
        // Name and +/-
        drawSettingText(component, component.name, true, false)
        drawText(if (component.open) "-" else "+", component.vec.x + component.vec.w - 2, component.vec.y + 3, true, true)
        // Inner outline
        if (component.open) drawParentOutline(component)
    }

    override fun drawTextComponent(component: ParentComponent, value: String) {
        val text = trimValue(component, component.name, value)
        drawSettingText(component, component.name, true, false)
        // Draw "-" at different pos than text, looks cleaner
        if (value == "-") drawText(text, component.vec.x + component.vec.w - 2, component.vec.y + 3, true, true)
        else drawSettingText(component, text, false, true)
        if (component.open) drawParentOutline(component)
    }

    override fun drawColorComponent(component: ParentComponent, value: Color) {
        drawSettingText(component, component.name, true, false)
        drawCheckBox(component, value.rgba, false)
        if (component.open) drawParentOutline(component)
    }

    override fun drawActionComponent(component: Component) {
        drawSettingText(component, component.name, true, false)
    }

    override fun drawToggleComponent(component: Component, value: Boolean) {
        drawSettingText(component, component.name, value, false)
        val color = if (value) ClickGui.color.rgba else ClickGui.bgColor.rgba
        drawCheckBox(component, color, value)
    }

    override fun drawValueComponent(component: Component, value: String) {
        drawSettingText(component, component.name, true, false)
        drawSettingText(component, value, false, true)
    }

    override fun drawSliderComponent(component: Component, value: String, fill: Float) {
        drawSettingText(component, component.name, true, false)
        drawSettingText(component, value, false, true)
        val width = (component.vec.w) * fill
        val slider = component.vec with { y += 10f; w = width; h = 2.03f }
        Render2DUtil.outlineRect(slider, 0.53f, -1f, ClickGui.outline.rgba)
        Render2DUtil.fillRect(slider, -2f, ClickGui.color.rgba)
    }

    override fun drawDualSliderComponent(component: Component, vertical: Float, horizontal: Float) {
        drawSettingText(component, component.name, true, false)
        val height = (component.vec.w) * vertical
        val width = (component.vec.w) * horizontal
        val offset = component.vec.y + component.vec.h - height
        val slider = component.vec with { y = offset; w = width; h = height }
        Render2DUtil.outlineRect(slider, 0.47f, -1f, ClickGui.outline.rgba)
        Render2DUtil.fillRect(slider, -2f, ClickGui.color.rgba)
    }

    override fun drawTextAreaComponent(component: TextAreaComponent) {
        var height = component.vec.y + 3
        // todo FontRenderer.drawSplitString()
        component.getTextLines((component.vec.w - defaultTextOffset * 2).toInt()).forEach {
            drawText(it, component.vec.x + defaultTextOffset, height, true, false)
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

    override fun drawColorPickerRect(component: Component, vertical: Float, horizontal: Float, renderer: (Vector) -> Unit) {
        val picker = component.vec with { y -= 0.53f; h += 1.03f }
        Render2DUtil.outlineRect(picker, 0.53f, 0f, ClickGui.outline.rgba)
        val pointX = component.vec.x + horizontal * (component.vec.w)
        val pointY = component.vec.y + component.vec.h - vertical * component.vec.h
        Render2DUtil.outlineRect((pointX - 1f), (pointY - 1f), 2f, 2f, 0.47f, 0f, Font.primaryText.rgba)
        renderer(picker)
    }

    private fun drawParentOutline(component: ParentComponent) {
        val vec = component.vec with { h -= 15.47f; y += 13.47f }
        Render2DUtil.outlineRect(vec, 0.53f, 0f, ClickGui.outline.rgba)
    }

    private fun drawSettingText(component: Component, value: String, primaryColor: Boolean, rightAlign: Boolean) {
        val text = trimValue(component, if (rightAlign) component.name else "", value)
        val align = if (rightAlign) component.vec.w - defaultTextOffset else defaultTextOffset
        drawText(text, component.vec.x + align, component.vec.y + 3, primaryColor, rightAlign)
    }

    private fun drawCheckBox(component: Component, color: Int, check: Boolean) {
        val x = (component.vec.x + component.vec.w)
        val box = Vector(x - 11, component.vec.y + 1.47f, 11f, 10.53f)
        Render2DUtil.outlineRect(box, 0.53f, 0f, ClickGui.outline.rgba)
        Render2DUtil.fillRect(box, -1f, color)
        if (check) drawText("✔", x - 9, component.vec.y + 2, true, false)
    }

    private fun trimValue(component: Component, otherText: String, value: String): String {
        val otherWidth = FontRenderer.getStringWidth(otherText)
        val maxWidth = (component.vec.w - otherWidth).toInt() - 6
        return if (FontRenderer.getStringWidth(value) < maxWidth) value else {
            val dotsWidth = FontRenderer.getStringWidth("...")
            FontRenderer.trimStringToWidth(value, maxWidth - dotsWidth) + "..."
        }
    }

    // TODO: 3/10/2022 finish this
    //private fun drawPrimaryColor(vec: Vector, z: Float, outline: Boolean, )
}
