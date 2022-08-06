package me.bush.cornerstore.impl.module.modules.client

import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.api.setting.withInfo
import me.bush.cornerstore.api.setting.withVis
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.minecraft.ChatUtil
import me.bush.cornerstore.util.minecraft.Format

/**
 * @author bush
 * @since fall 2021
 */
object Settings : Module("Manage misc client settings") {
    // Logging
    val info = BooleanSetting("Info Log", true).withInfo("Log client info in chat")
    val warn = BooleanSetting("Warn Log", true).withInfo("Log client warnings in chat")
    val errors = BooleanSetting("Error Log", true).withInfo("Log client errors in chat")
    private val logging = ParentSetting("Logging", info, warn, errors)
    // Chat Prefix
    private val custom = BooleanSetting("Custom", false).withInfo("Use custom prefix (can use format codes like &l&k&c)")
    private val noPrefix = BooleanSetting("No Prefix", false)
    private val customPrefix = StringSetting("Prefix", "&c[&km&r&5&lBUSHMOD&r&c&km&r&c]&r").withVis { custom.value }
    private val bold = BooleanSetting("Bold", false).withVis { !custom.value }
    private val brackets = FormatSetting("[ . ]", Format.GRAY).withVis { !custom.value }
    private val first = FormatSetting("711", Format.LIGHTPURPLE).withVis { !custom.value }
    private val second = FormatSetting("CLUB", Format.RED).withVis { !custom.value }
    private val prefix = ParentSetting("Client Prefix", custom, noPrefix, customPrefix, bold, brackets, first, second)
    // Info style
    val moduleColor = FormatSetting("Module Msgs", Format.WHITE)
    val textColor = FormatSetting("Text Color", Format.GRAY)
    val valueColor = FormatSetting("Value Color", Format.AQUA)
    val boldValue = BooleanSetting("Bold Value", true)
    private val chatColors = ParentSetting("Chat Colors", moduleColor, textColor, valueColor, boldValue)
    // Cmd prefix
    val commandPrefix = StringSetting("Cmd Prefix", ".")


    val testFormat = FormatSettingTest("test thing")
    val dot = ActionSetting("Do") { ChatUtil.message(testFormat.format + "test") }

    override val toggleable = false

    val clientPrefix
        get() = if (noPrefix.value) ""
        else if (custom.value) customPrefix.value.replace("&", "§") + " "
        else {
            val bold = if (bold.value) "${Format.BOLD}" else ""
            val bracket = brackets.format + bold
            val first = first.format + bold
            val second = second.format + bold
            "$bracket[${first}711$bracket.${second}CLUB$bracket]${Format.RESET} "
        }
}
