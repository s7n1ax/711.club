package me.bush.cornerstore.impl.module.modules.render

import me.bush.cornerstore.api.setting.settings.ColorSetting
import me.bush.cornerstore.api.setting.settings.ParentSetting
import me.bush.cornerstore.impl.module.Module

/**
 * @author bush
 * @since fall 2021
 */
object Tracers : Module("Draws a line between your crosshair and enemies") {
    val color = ColorSetting("Enemy Color", 0, 0, 0, 0)
    val color3 = ColorSetting("Color", 0, 0, 0, 0)
    val parentTest2 = ParentSetting("Parendft", color3)
    val parentTest1 = ParentSetting("Parsent", parentTest2)
    val parentTest0 = ParentSetting("Pafsrent", parentTest1)
}
