package me.bush.cornerstore.api.gui.component

import me.bush.cornerstore.api.gui.DragHandler
import me.bush.cornerstore.util.math.Vector

/**
 * A [ParentComponent] that can be dragged, opened and closed.
 * If special is true, width will not be adjusted by the slider in the clickgui module.
 *
 * @author bush
 * @since 12/22/2021
 */
class WindowComponent(name: String, val special: Boolean) : ParentComponent(name) {
    private val dragHandler = DragHandler(vec, ::isMouseOverHeader)

    init {
        open = true
    }

    override val headerHeight get() = theme.windowHeaderHeight
    override val footerHeight get() = theme.windowFooterHeight
    override val borderWidth get() = theme.windowBorderWidth

    override fun drawComponent() {
        theme.drawWindowComponent(this)
    }

    override fun drawScreen(mouse: Vector?) {
        mouse?.let(dragHandler::handleDrawScreen)
        super.drawScreen(mouse)
    }

    override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        dragHandler.handleMouseClicked(mouse, button, clicked)
        super.mouseClicked(mouse, button, clicked)
    }
}
