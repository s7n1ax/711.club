package me.bush.cornerstore.api.gui

import me.bush.cornerstore.util.math.Vector

/**
 * @author bush
 * @since 3/5/2022
 */
class DragHandler(val vec: Vector, val mouseOver: (Vector) -> Boolean) {
    private var clickedVec: Vector? = null
    private var dragging = false

    fun handleDrawScreen(mouse: Vector) {
        if (dragging) vec.run {
            (clickedVec!! + mouse).let {
                x = it.x
                y = it.y
            }
        }
    }

    fun handleMouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        dragging = false
        if (mouseOver(mouse) && clicked && button == 0) {
            dragging = true
            // Record click pos relative to window
            clickedVec = vec - mouse
        }
    }
}
