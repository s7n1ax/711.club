package me.bush.cornerstore.util.minecraft

import me.bush.cornerstore.util.lang.flipped

/**
 * @author bush
 * @since 2/15/2022
 */
enum class Format(code: Char) {

    // Colors
    BLACK('0'),
    DARKBLUE('1'),
    DARKGREEN('2'),
    DARKAQUA('3'),
    DARKRED('4'),
    DARKPURPLE('5'),
    ORANGE('6'),
    GRAY('7'),
    DARKGRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHTPURPLE('d'),
    YELLOW('e'),
    WHITE('f'),

    // Styles
    OBFUSCATED('k'),
    BOLD('l'),
    STRIKETHROUGH('m'),
    UNDERLINE('n'),
    ITALIC('o'),
    RESET('r');

    private var format = "§$code"

    override fun toString(): String {
        return format
    }

    companion object {
        val colors get() = values().copyOf(16).requireNoNulls()
    }
}

const val sect = '§'

enum class ChatColor(code: Char) {
    BLACK('0'),
    DARKBLUE('1'),
    DARKGREEN('2'),
    DARKAQUA('3'),
    DARKRED('4'),
    DARKPURPLE('5'),
    ORANGE('6'),
    GRAY('7'),
    DARKGRAY('8'),
    BLUE('9'),
    GREEN('a'),
    AQUA('b'),
    RED('c'),
    LIGHTPURPLE('d'),
    YELLOW('e'),
    WHITE('f');

    private val format = "§$code"

    override fun toString() = format
}

object ChatFormat {
    const val BLACK = "§0"
    const val DARKBLUE = "§1"
    const val DARKGREEN = "§2"
    const val DARKAQUA = "§3"
    const val DARKRED = "§4"
    const val DARKPURPLE = "§5"
    const val ORANGE = "§6"
    const val GRAY = "§7"
    const val DARKGRAY = "§8"
    const val BLUE = "§9"
    const val GREEN = "§a"
    const val AQUA = "§b"
    const val RED = "§c"
    const val LIGHTPURPLE = "§d"
    const val YELLOW = "§e"
    const val WHITE = "§e"

    const val CURSED = "§k"
    const val BOLD = "§l"
    const val STRIKETHROUGH = "§m"
    const val UNDERLINE = "§n"
    const val ITALIC = "§o"

    const val RESET = "§r"

    val COLORS = arrayOf(
        BLACK,
        DARKBLUE,
        DARKGREEN,
        DARKAQUA,
        DARKRED,
        DARKPURPLE,
        ORANGE,
        GRAY,
        DARKGRAY,
        BLUE,
        GREEN,
        AQUA,
        RED,
        LIGHTPURPLE,
        YELLOW,
        WHITE,
    )

    fun nameOf(value: String) = names[value]!!

    fun valueOf(name: String) = values[name]!!

    private val names = mapOf(
        BLACK to "Black",
        DARKBLUE to "Dark Blue",
        DARKGREEN to "Dark Green",
        DARKAQUA to "Dark Aqua",
        DARKRED to "Dark Red",
        DARKPURPLE to "Dark Purple",
        ORANGE to "Orange",
        GRAY to "Gray",
        DARKGRAY to "Dark Gray",
        BLUE to "Blue",
        GREEN to "Green",
        AQUA to "Aqua",
        RED to "Red",
        LIGHTPURPLE to "Light Purple",
        YELLOW to "Yellow",
        WHITE to "White",

        CURSED to "Cursed",
        BOLD to "Bold",
        STRIKETHROUGH to "Strikethrough",
        UNDERLINE to "Underline",
        ITALIC to "Italic",

        RESET to "Reset"
    )

    private val values = names.flipped
}
