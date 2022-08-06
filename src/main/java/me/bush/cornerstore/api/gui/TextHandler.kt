package me.bush.cornerstore.api.gui

import me.bush.cornerstore.api.font.FontRenderer
import me.bush.cornerstore.util.math.Vector
import net.minecraft.client.gui.GuiTextField

/**
 * This isn't used to render text, just an easy way to input keys and get the string that is produced.
 * You can copy/paste, use arrow keys, and other stuff
 *
 * @author bush
 * @since fall 2021
 */
class TextHandler : GuiTextField(711, FontRenderer, 711, 711, 711, 711) {
    private var fallback = ""

    init {
        // Unimportant values
        maxStringLength = 711
        setEnabled(true)
        isFocused = true
    }

    override fun setText(text: String) {
        super.setText(text)
        fallback = text
    }

    // Reset text to old value
    fun reset() {
        text = fallback
    }

    fun append(text: String) {
        setText(this.text + text)
    }

    fun updateText(typedChar: Char, keyCode: Int) {
        textboxKeyTyped(typedChar, keyCode)
    }
    // If i ever wanted to do cursor clicking or selection dragging
    fun updateCursor(mouse: Vector?, mouseDown: Boolean) {}

    fun getTextLines(width: Int): List<String> {
        return FontRenderer.listFormattedStringToWidth(text, width)
    }
}
