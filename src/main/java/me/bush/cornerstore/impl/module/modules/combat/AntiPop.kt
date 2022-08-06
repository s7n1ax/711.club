package me.bush.cornerstore.impl.module.modules.combat

import me.bush.cornerstore.impl.module.Module

/**
 * Keeping this might turn it into anticrystal or sumn
 *
 * @author bush
 * @since 12/24/2021
 */
object AntiPop : Module("does what it says it does") {

    override fun onEnable() {
        OffHand.enabled = false
        toggle()
    }
}
