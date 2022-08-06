package me.bush.cornerstore.impl.module.modules.misc

import me.bush.cornerstore.api.setting.settings.BooleanSetting
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.mixin.MixinMinecraft

/**
 * @author bush
 * @since 5/9/2022
 *
 * @see MixinMinecraft
 */
object MultiTask : Module("") {
    val mineWhileEating by BooleanSetting("Mine/Eat", true)
    val attackAndEat by BooleanSetting("Attack/Eat", true)
}
