package me.bush.cornerstore.api.gui.component.slider

import me.bush.cornerstore.api.gui.component.AbstractComponent
import me.bush.cornerstore.util.math.Vector

/**
 * Base class for the color picker rect.
 *
 * @author bush
 * @since jan 2022
 */
open class DualSliderComponent(
    private val verticalHandler: SliderHandler<*>,
    private val horizontalHandler: SliderHandler<*>,
    visibility: () -> Boolean = { true }
) : AbstractComponent(visibility) {
    private var dragging = false

    protected open fun drawSlider(vertical: Float, horizontal: Float) {
        theme.drawDualSliderComponent(this, vertical, horizontal)
    }

    override fun drawScreen(mouse: Vector?) {
        val border = theme.defaultFooterHeight
        vec.run { h = w; y += border }
        if (dragging && mouse != null) {
            val vertical = (vec.y + vec.h - mouse.y) / vec.h
            val horizontal = (mouse.x - vec.x) / vec.w
            verticalHandler.handleDrag(vertical)
            horizontalHandler.handleDrag(horizontal)
        }
        drawSlider(verticalHandler.percent, horizontalHandler.percent)
        vec.h += border * 2
    }

    override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        val mouseOver = vec.with { h -= 4f; }.isPointInside(mouse)
        dragging = mouseOver && clicked && button == 0
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}
