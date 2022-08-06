package me.bush.cornerstore.impl.module

import me.bush.cornerstore.util.lang.properNoun

/**
 * Only really used to get the ordinal or a list of strings lol
 *
 * @author bush
 * @since 11/5/2021
 */
enum class Category {
    COMBAT, MISC, RENDER, MOVEMENT, PLAYER, CLIENT;

    companion object {
        val names get() = values().map { it.name.properNoun() }
    }
}
