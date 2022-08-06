package me.bush.cornerstore.api.gui

import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.api.font.FontRenderer
import me.bush.cornerstore.api.gui.component.*
import me.bush.cornerstore.api.gui.component.special.TextAreaComponent
import me.bush.cornerstore.api.load.LoadHandler.awaitPrimary
import me.bush.cornerstore.impl.module.ModuleManager
import me.bush.cornerstore.impl.module.modules.client.ClickGui
import me.bush.cornerstore.impl.module.modules.client.Font
import me.bush.cornerstore.util.lang.logExceptions
import me.bush.cornerstore.util.lang.logLoadTime
import me.bush.cornerstore.util.math.Vector
import me.bush.cornerstore.util.render.Color
import me.bush.cornerstore.util.system.subclasses
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * @author bush
 * @since 12/21/2021
 */
interface Theme {

    val name: String

    val windowHeaderHeight: Float
    val windowFooterHeight: Float
    val windowBorderWidth: Float

    val featureHeaderHeight: Float
    val featureFooterHeight: Float
    val featureBorderWidth: Float

    val defaultHeaderHeight: Float
    val defaultFooterHeight: Float
    val defaultBorderWidth: Float

    val windowTextOffset: Float
    val featureTextOffset: Float
    val defaultTextOffset: Float

    // TODO: 3/5/2022 mouseover in param
    fun drawWindowComponent(component: WindowComponent)
    fun drawFeatureComponent(component: FeatureComponent)

    fun drawParentComponent(component: ParentComponent)
    fun drawTextComponent(component: ParentComponent, value: String)
    fun drawColorComponent(component: ParentComponent, value: Color)

    fun drawActionComponent(component: Component)
    fun drawToggleComponent(component: Component, value: Boolean)
    fun drawValueComponent(component: Component, value: String)
    fun drawSliderComponent(component: Component, value: String, fill: Float)

    fun drawDualSliderComponent(component: Component, vertical: Float, horizontal: Float)
    fun drawTextAreaComponent(component: TextAreaComponent)
    fun drawColorPickerSlider(component: Component, fill: Float, renderer: (Vector) -> Unit)
    fun drawColorPickerRect(component: Component, vertical: Float, horizontal: Float, renderer: (Vector) -> Unit)

    /**
     * Renders text to the screen.
     *
     * @param text         The text to draw.
     * @param x            The x position to start at.
     * @param y            The y position to start at (top corner).
     * @param primaryColor True to use the primary text color, false to use the secondary color.
     * @param rightAlign   Whether the text should go left or right from the x start position.
     */
    fun drawText(text: String, x: Float, y: Float, primaryColor: Boolean, rightAlign: Boolean) {
        val color = if (primaryColor) Font.primaryText.value else Font.secondaryText.value
        val align = if (rightAlign) x - FontRenderer.getStringWidth(text) else x
        FontRenderer.drawString(text, align, y, color.rgba, Font.shadow.value)
    }

    companion object : Manager {
        lateinit var current: Theme private set
        var themes = mutableListOf<KClass<out Theme>>()

        fun setTheme(theme: KClass<out Theme>) {
            logExceptions("Could not instantiate theme $theme") {
                current = theme.createInstance()
            }
        }

        override suspend fun primaryLoad() {
            logLoadTime("themes") {
                themes += Theme::class.subclasses
                themes.size
            }
            ModuleManager.awaitPrimary()
            setTheme(ClickGui.theme.value)
        }
    }
}
