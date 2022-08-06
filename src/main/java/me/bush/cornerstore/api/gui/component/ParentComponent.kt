package me.bush.cornerstore.api.gui.component

import me.bush.cornerstore.api.gui.Component
import me.bush.cornerstore.util.math.MathUtil
import me.bush.cornerstore.util.math.Vector

/**
 * A component that can be opened and closed. when open, all child components will be drawn, each one below the other.
 * Child components will be rendered `previouscomponent.vec.h` pixels below the previous.
 *
 * @author bush
 * @since 12/23/2021
 */
abstract class ParentComponent(
    name: String = "Component",
    visibility: () -> Boolean = { true }
) : AbstractComponent(visibility, name) {
    var open = false
    val subComponents = mutableListOf<Component>()

    open val headerHeight get() = theme.defaultHeaderHeight
    open val footerHeight get() = theme.defaultFooterHeight
    open val borderWidth get() = theme.defaultBorderWidth

    protected open fun drawComponent() {
        theme.drawParentComponent(this)
    }

    override fun drawScreen(mouse: Vector?) {
        vec.h = headerHeight
        if (open) {
            subComponents.forEach {
                if (it.visible) {
                    it.vec.run {
                        x = vec.x + borderWidth
                        w = vec.w - borderWidth * 2
                        y = vec.y + vec.h
                    }
                    it.drawScreen(mouse)
                    vec.h += it.vec.h
                }
            }
            vec.h += footerHeight
        }
        drawComponent()
    }

    override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        if (isMouseOverHeader(mouse) && clicked && button == 1) {
            open = !open
            playClick()
        }
        if (open) subComponents.forEach { it.mouseClicked(mouse, button, clicked) }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (open) subComponents.forEach { it.keyTyped(typedChar, keyCode) }
    }

    fun isMouseOverHeader(mouse: Vector) = MathUtil.isPointInside(vec with { h = headerHeight }, mouse)
}
