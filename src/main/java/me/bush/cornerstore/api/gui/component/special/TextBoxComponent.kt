package me.bush.cornerstore.api.gui.component.special

import me.bush.cornerstore.api.gui.component.ParentComponent
import me.bush.cornerstore.util.math.Vector

/**
 * "Wrapper" Component for [TextAreaComponent]. Can be opened and closed, and shows a preview when closed.
 *
 * @author bush
 * @since 2/5/2022
 */
class TextBoxComponent(
    name: String,
    visibility: () -> Boolean,
    private val input: () -> String,
    output: (String) -> Unit
) : ParentComponent(name, visibility) {
    private val textArea = TextAreaComponent(input, output).also { subComponents.add(it) }

    override fun drawComponent() {
        theme.drawTextComponent(this, if (open) "-" else input())
    }

    override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {
        super.mouseClicked(mouse, button, clicked)
        if (!isMouseOver(mouse) && clicked) open = false
        textArea.focused = open
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)
        if (textArea.focused != open) open = false
    }
}
