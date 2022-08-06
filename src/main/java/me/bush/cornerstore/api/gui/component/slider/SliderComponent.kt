package me.bush.cornerstore.api.gui.component.slider

import me.bush.cornerstore.api.gui.TextHandler
import me.bush.cornerstore.api.gui.component.AbstractComponent
import me.bush.cornerstore.util.lang.tryFormat
import me.bush.cornerstore.util.math.Vector
import org.lwjgl.input.Keyboard

/**
 * @author bush
 * @since jan 2022
 */
open class SliderComponent(
    private val handler: SliderHandler<*>,
    visibility: () -> Boolean = { true },
    name: String = "SliderComponent"
) : AbstractComponent(visibility, name) {
    private val textHandler = TextHandler()
    private var listening = false
    private var dragging = false

    protected open val height get() = theme.defaultHeaderHeight

    protected open fun drawSlider(value: String, fill: Float) {
        theme.drawSliderComponent(this, value, fill)
    }

    override fun drawScreen(mouse: Vector?) {
        vec.h = height
        if (dragging && mouse != null) {
            val click = (mouse.x - vec.x) / vec.w
            handler.handleDrag(click)
        }
        // If we are listening for text, render the input text, else the current handler value
        val text = if (listening) textHandler.text else handler.value.toString()
        drawSlider(text.tryFormat(), handler.percent)
    }

    override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        if (isMouseOver(mouse) && clicked) {
            if (button == 0) {
                dragging = true
                if (listening) {
                    listening = false
                    textHandler.reset()
                }
            } else if (button == 1) {
                listening = true
                textHandler.text = ""
                playClick()
            }
        } else {
            dragging = false
            if (clicked && listening) {
                listening = false
                textHandler.reset()
            }
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (listening) {
            // Exit typing, keep current value
            if (keyCode == Keyboard.KEY_RETURN) {
                listening = false
                return
            }
            // Reset to value before clicked
            if (keyCode == Keyboard.KEY_ESCAPE) {
                textHandler.reset()
                listening = false
                return
            }
            // Update text handler
            textHandler.updateText(typedChar, keyCode)
            // Try parsing string to set value
            handler.handleText(textHandler.text)
        }
    }
}
