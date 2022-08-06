package me.bush.cornerstore.api.gui.component.special

import me.bush.cornerstore.api.gui.component.AbstractComponent
import me.bush.cornerstore.util.math.Vector

/**
 * @author bush
 * @since jan 2022
 */
class ActionComponent(name: String, visibility: () -> Boolean = { true }, private val action: Runnable) : AbstractComponent(visibility, name) {

    override fun drawScreen(mouse: Vector?) {
        vec.h = theme.defaultHeaderHeight
        theme.drawActionComponent(this)
    }

    override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        if (isMouseOver(mouse) && clicked && button == 0) {
            playClick()
            action.run()
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}
