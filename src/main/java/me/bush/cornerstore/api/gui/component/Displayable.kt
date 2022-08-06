package me.bush.cornerstore.api.gui.component

import me.bush.cornerstore.api.common.Nameable
import me.bush.cornerstore.api.gui.Component

/**
 * Implemented by classes that are displayed in the gui (modules, settings, etc).
 *
 * @author bush
 * @since 12/23/2021
 */
interface Displayable : Nameable {

    /**
     * Should contain a subclass or anonymous class that represents the implementing object.
     */
    val component: Component
}
