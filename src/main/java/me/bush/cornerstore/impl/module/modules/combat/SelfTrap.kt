package me.bush.cornerstore.impl.module.modules.combat

import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.impl.module.Module

/**
 * @author bush
 * @since fall 2021
 */
object SelfTrap : Module("Places obsidian over your head") {
    // TODO: 2/19/2022 one module that handles placement ranges and etc?
    private val rotate = BooleanSetting("Rotate", false)
}
