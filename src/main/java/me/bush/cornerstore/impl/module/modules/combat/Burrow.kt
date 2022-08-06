package me.bush.cornerstore.impl.module.modules.combat

import me.bush.cornerstore.impl.module.Module

/**
 * @author bush
 * @since fall 2021
 */
object Burrow : Module("Place block on feet.") {
    // test to see if this works in configs and etc
    val setting by me.bush.cornerstore.api.setting.settings.BooleanSetting("Setting", false)
}
