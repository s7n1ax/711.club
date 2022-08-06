package me.bush.cornerstore.util.minecraft

import me.bush.cornerstore.api.common.initialized
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.impl.module.modules.client.Settings
import me.bush.cornerstore.util.minecraft.Format.*
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextComponentString

fun Minecraft.rawClientMessage(message: String, id: Int = 0) {
    if (running && player != null) {
        ingameGUI?.chatGUI?.printChatMessageWithOptionalDeletion(TextComponentString(message), id)
    }
}

/**
 * @author bush
 * @since fall 2021
 */
object ChatUtil {

    fun message(message: String, id: Int = 0) {
        // Other threads can still send chat messages, so we check that mc is actually running
        if (mc.running && mc.player != null) {
            val text = Settings.clientPrefix + message
            mc.ingameGUI?.chatGUI?.printChatMessageWithOptionalDeletion(TextComponentString(text), id)
        }
    }

    fun info(info: String, value: Any? = null) {
        if (initialized && Settings.info.value) {
            message("$DARKAQUA[${AQUA}INFO$DARKAQUA] ${format(info, value.toString())}")
        }
    }

    fun warn(warn: String) {
        if (initialized && Settings.warn.value) {
            message("$ORANGE[${YELLOW}WARN$ORANGE] ${format(warn)}")
        }
    }

    fun error(error: String) {
        if (initialized && Settings.errors.value) {
            message("$DARKRED[${RED}ERROR$DARKRED] ${format(error)}")
        }
    }

    // I don't think theres a more "elegant" way to do the interpolation
    fun format(message: String, value: String? = null): String {
        value ?: return Settings.textColor.format + message
        val bold = if (Settings.boldValue.value) BOLD else ""
        val coloredValue = Settings.valueColor.format + bold + value + Settings.textColor.format
        return Settings.textColor.format + message.replace("{}", coloredValue)
    }
}
