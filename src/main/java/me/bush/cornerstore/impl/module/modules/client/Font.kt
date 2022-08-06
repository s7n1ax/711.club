package me.bush.cornerstore.impl.module.modules.client

import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.api.setting.settings.ColorSetting
import me.bush.cornerstore.impl.module.Module

/**
 * @author bush
 * @since fall 2021
 */
object Font : Module("Quickly change Minecraft's font") {
    val primaryText = ColorSetting("Prim. Text", 255, 255, 255, 255)
    val secondaryText = ColorSetting("Sec. Text", 200, 200, 200, 255)
    val shadow = BooleanSetting("Text Shadow", true)
}
