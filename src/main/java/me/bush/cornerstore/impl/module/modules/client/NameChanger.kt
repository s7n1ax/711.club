package me.bush.cornerstore.impl.module.modules.client

import me.bush.cornerstore.api.common.initialized
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.setting.settings.StringSetting
import me.bush.cornerstore.impl.module.Module

/**
 * MixinFontRenderer ;)
 *
 * @author bush
 * @since 2/19/2022
 */
object NameChanger : Module("Lets you change your name client side.") {
    val alias = StringSetting("Alias", "bush")
    // TODO: 4/24/2022 toggleable parent settings or sub-feature settings

    fun changeString(string: String) = if (initialized) {
        val username = mc.session.username
        val alias = alias.value
        if (enabled && username != alias && username in string) {
            string.replace(username, alias)
        } else null
    } else null
}
