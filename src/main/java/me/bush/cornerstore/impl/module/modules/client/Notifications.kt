package me.bush.cornerstore.impl.module.modules.client

import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.minecraft.Format

/**
 * @author bush
 * @since 3/25/2022
 */
object Notifications : Module("Manage notification settings.") {
    val mode = ModeSetting("Mode", "Chat", "Popup") // todo
    val info = BooleanSetting("Info", true)
    val alert = BooleanSetting("Alert", true)
    val warn = BooleanSetting("Warning", true)
    val error = BooleanSetting("Error", true)
    val types = ParentSetting("Types", info, alert, warn, error)
    val senderColor = FormatSetting("Sender Color", Format.WHITE)
    val textColor = FormatSetting("Text Color", Format.GRAY)
    val valueColor = FormatSetting("Value Color", Format.AQUA).withInfo("The color of notification values, like \"... has popped VALUE totems\"")
    val chatStyle = ParentSetting("Style")
    val popupStyle = ParentSetting("Style")
}
