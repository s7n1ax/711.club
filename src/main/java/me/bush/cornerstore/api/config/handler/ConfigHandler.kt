package me.bush.cornerstore.api.config.handler

import me.bush.cornerstore.api.config.ConfigManager

/**
 * Used by [ConfigManager] to save/load various assets.
 *
 * @author bush
 * @since 2/26/2022
 */
interface ConfigHandler {

    fun load()

    fun save()
}
