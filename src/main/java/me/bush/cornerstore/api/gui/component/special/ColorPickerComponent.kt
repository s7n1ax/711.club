package me.bush.cornerstore.api.gui.component.special

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.bush.cornerstore.api.gui.component.ParentComponent
import me.bush.cornerstore.api.gui.component.slider.DualSliderComponent
import me.bush.cornerstore.api.gui.component.slider.SliderComponent
import me.bush.cornerstore.api.gui.component.slider.SliderHandler.Companion.simpleHandler
import me.bush.cornerstore.api.setting.onChange
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.impl.module.modules.client.ClickGui
import me.bush.cornerstore.util.lang.Background
import me.bush.cornerstore.util.minecraft.ChatUtil
import me.bush.cornerstore.util.render.Color
import me.bush.cornerstore.util.render.Render2DUtil
import me.bush.cornerstore.util.system.Clipboard
import kotlin.math.abs

/**
 * @author bush
 * @since jan 2022
 */
class ColorPickerComponent(name: String, visibility: () -> Boolean, val color: Color) : ParentComponent(name, visibility) {

    init {
        setupColorPicker()
        setupSubSettings()
    }

    override fun drawComponent() {
        theme.drawColorComponent(this, color)
    }

    private fun setupColorPicker() {
        val pickerMode = ClickGui.pickerMode
        // Color picker handlers
        val vertical = simpleHandler(
            { if (pickerMode.isValue("Hue/Sat")) color[1] = it else color[2] = it },
            { if (pickerMode.isValue("Hue/Sat")) color[1] else color[2] })
        val horizontal = simpleHandler(
            { if (pickerMode.isValue("Sat/Bright")) color[1] = it else color[0] = it },
            { if (pickerMode.isValue("Sat/Bright")) color[1] else color[0] })
        // Color picker component
        subComponents += object : DualSliderComponent(vertical, horizontal) {

            override fun drawSlider(vertical: Float, horizontal: Float) {
                theme.drawColorPickerRect(this, vertical, horizontal) {
                    when (pickerMode.value) {
                        "Hue/Sat" -> {
                            Render2DUtil.hueRect(it, -1f, 1f, 1f, false)
                            Render2DUtil.gradientFillRect(it, -1f, 0x00ffffff, 0xffffffff.toInt(), true)
                        }
                        "Hue/Bright" -> {
                            Render2DUtil.hueRect(it, -1f, 1f, 1f, false)
                            Render2DUtil.gradientFillRect(it, -1f, 0x00000000, 0xff000000.toInt(), true)
                        }
                        "Sat/Bright" -> {
                            val desaturated = color.with { saturation = 0f; brightness = 1f; alpha = 255 }.rgba
                            val saturated = color.with { saturation = 1f; brightness = 1f; alpha = 255 }.rgba
                            Render2DUtil.gradientFillRect(it, -1f, desaturated, saturated, false)
                            Render2DUtil.gradientFillRect(it, -1f, 0x00000000, 0xff000000.toInt(), true)
                        }
                    }
                }
            }
        }
        // Aux slider handler
        val auxHandler = simpleHandler({
            when (pickerMode.value) {
                "Hue/Sat" -> color[2] = it
                "Hue/Bright" -> color[1] = it
                "Sat/Bright" -> color[0] = it
            }
        }, {
            when (pickerMode.value) {
                "Hue/Sat" -> color[2]
                "Hue/Bright" -> color[1]
                else -> color[0]
            }
        })
        // Aux slider component
        subComponents += object : SliderComponent(auxHandler) {
            override val height get() = theme.defaultHeaderHeight / 2

            override fun drawSlider(value: String, fill: Float) {
                theme.drawColorPickerSlider(this, fill) {
                    when (pickerMode.value) {
                        "Hue/Sat" -> {
                            val dark = color.with { brightness = 0f; alpha = 255 }.rgba
                            val light = color.with { brightness = 1f; alpha = 255 }.rgba
                            Render2DUtil.gradientFillRect(it, -2f, dark, light, false)
                        }
                        "Hue/Bright" -> {
                            val desaturated = color.with { saturation = 0f; alpha = 255 }.rgba
                            val saturated = color.with { saturation = 1f; alpha = 255 }.rgba
                            Render2DUtil.gradientFillRect(it, -2f, desaturated, saturated, false)
                        }
                        "Sat/Bright" -> Render2DUtil.hueRect(it, -2f, 1f, 1f, false)
                    }
                }
            }
        }
        // Alpha slider handler + component
        val alphaHandler = simpleHandler({ color[3] = it }) { color[3] }
        subComponents += object : SliderComponent(alphaHandler) {
            override val height get() = theme.defaultHeaderHeight / 2

            override fun drawSlider(value: String, fill: Float) {
                theme.drawColorPickerSlider(this, fill) {
                    Render2DUtil.checkeredRect(it, -3f, 2f, -0x1, -0x7f7f80)
                    Render2DUtil.gradientFillRect(it, -2f, color.with { alpha = 50 }.rgba, color.with { alpha = 255 }.rgba, false)
                }
            }
        }
    }

    private fun setupSubSettings() {
        // RGB sliders
        val red = IntSetting("Red", color.red, 0, 255).onChange(::updateRed)
        val green = IntSetting("Green", color.green, 0, 255).onChange(::updateGreen)
        val blue = IntSetting("Blue", color.blue, 0, 255).onChange(::updateBlue)
        val rgb = ParentSetting("RGB", red, green, blue)
        subComponents += rgb.component
        // Special settings
        val rainbow = BooleanSetting("Rainbow", false).onChange { if (it != color.rainbow) color.rainbow = it }
        val variate = BooleanSetting("Variate", false).onChange { if (it != color.variate) color.variate = it }
        val speed = FloatSetting("Speed", 1.0f, 0.1f, 2.0f).onChange { if (it != color.speed) color.speed = it }
        val range = FloatSetting("Range", 1.0f, 0.1f, 2.0f).onChange { if (it != color.speed) color.range = it }
        val special = ParentSetting("Special", rainbow, variate, speed, range)
        subComponents += special.component
        // Misc stuff
        val copy = ActionSetting("Copy") { copy() }
        val paste = ActionSetting("Paste") { paste() }
        val alpha = IntSetting("Alpha", color.alpha, 0, 255).onChange { color[3] = it / 255f }
        val misc = ParentSetting("Misc", copy, paste, alpha)
        subComponents += misc.component
        color.observers += {
            red.value = it.red
            green.value = it.green
            blue.value = it.blue
            alpha.value = it.alpha
            rainbow.value = it.rainbow
            variate.value = it.variate
            speed.value = it.speed
            range.value = it.range
        }
    }

    private fun copy() {
        Color.syncing = color
        // Nullify after a while so we
        // can still paste from hex
        Background.launch {
            delay(5000)
            Color.syncing = null
        }
        val hex = color.toHexString()
        Clipboard = hex
        ChatUtil.info("Color {} copied!", hex)
    }

    private fun paste() {
        // If a color was recently copied,
        // set from it instead of clipboard
        Color.syncing?.let {
            color.setFrom(it)
            ChatUtil.info("Color pasted!")
        } ?: Clipboard?.let {
            if (color.fromHexString(it)) ChatUtil.info("Hex color {} pasted!", it)
        }
    }

    // So our observer loop from picker > setting > picker doesn't
    // act retarded because of a rounding error.
    private fun updateRed(red: Int) {
        if (abs(color.red - red) < 1) return
        color.red = red
    }

    private fun updateGreen(green: Int) {
        if (abs(color.green - green) < 1) return
        color.green = green
    }

    private fun updateBlue(blue: Int) {
        if (abs(color.blue - blue) < 1) return
        color.blue = blue
    }
}
