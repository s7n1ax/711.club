package me.bush.cornerstore.api.gui.component.special

import me.bush.cornerstore.api.gui.component.AbstractComponent
import me.bush.cornerstore.util.math.Vector
import kotlin.reflect.KMutableProperty0

/**
 * @author bush
 * @since 2/28/2022
 */
class ToggleComponent(
    name: String,
    visibility: () -> Boolean = { true },
    private val property: KMutableProperty0<Boolean> // Implement this fully if it works well (or not?)
) : AbstractComponent(visibility, name) {

    override fun drawScreen(mouse: Vector?) {
        theme.let {
            vec.h = it.defaultHeaderHeight
            it.drawToggleComponent(this, property.get())
        }
    }

    override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        if (isMouseOver(mouse) && clicked && button == 0) {
            playClick()
            property.set(!property.get())
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}
