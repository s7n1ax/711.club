package me.bush.cornerstore.api.gui.component.special

import me.bush.cornerstore.api.font.FontRenderer
import me.bush.cornerstore.api.gui.TextHandler
import me.bush.cornerstore.api.gui.component.AbstractComponent
import me.bush.cornerstore.util.math.Vector
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard

/**
 * Simple text area that can be enabled and disabled.
 * Only listens for keys when keytyped is called, and focused is set to true.
 * When focus is set, it will update from the input supplier, or output to the output consumer, based on focus state.
 *
 * @author bush
 * @since 12/23/2021
 */
class TextAreaComponent(private val input: () -> String, private val output: (String) -> Unit) : AbstractComponent() {
    private val textHandler = TextHandler()
    var focused = false
        set(value) {
            if (field == value) return
            field = value
            if (value) textHandler.text = input()
            else output(textHandler.text)
        }

    fun getTextLines(width: Int): List<String> {
        return textHandler.getTextLines(width)
    }

    override fun drawScreen(mouse: Vector?) {
        val width = vec.w - theme.defaultTextOffset * 2
        val height = FontRenderer.getWordWrappedHeight(textHandler.text, width.toInt())
        // TODO: 3/6/2022 wordwrappedheeight
        vec.h = height.toFloat() + theme.defaultBorderWidth * 3
        theme.drawTextAreaComponent(this)
    }

    override fun mouseClicked(mouse: Vector, button: Int, clicked: Boolean) {}

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (!focused) return
        if (keyCode == Keyboard.KEY_RETURN) {
            if (GuiScreen.isShiftKeyDown()) {
                textHandler.append("\n")
            } else focused = false
            return
        }
        if (keyCode == Keyboard.KEY_ESCAPE) {
            textHandler.reset()
            focused = false
            return
        }
        textHandler.updateText(typedChar, keyCode)
    }
}
