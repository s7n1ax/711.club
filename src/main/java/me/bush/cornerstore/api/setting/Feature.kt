package me.bush.cornerstore.api.setting

import me.bush.cornerstore.api.common.Nameable
import me.bush.cornerstore.api.config.entry.ConfigEntry
import me.bush.cornerstore.api.gui.component.Displayable
import me.bush.cornerstore.api.gui.component.FeatureComponent
import me.bush.cornerstore.impl.module.Category
import me.bush.cornerstore.util.system.getPropertiesOfType

/**
 * @author bush
 * @since 5/1/2022
 */
abstract class Feature(val category: Category, val description: String) : Nameable, Displayable, ConfigEntry {
    private val staticName = this::class.simpleName ?: "Anonymous Feature"
    override val name: String
        get() = TODO("Not yet implemented")
    val settings by lazy { this.getPropertiesOfType<Setting<*>>().sortedBy { it.order }.toList() }

    init {

    }

    override val component
        get() = FeatureComponent(name, { true }, {}).apply {
            subComponents += settings.filter { !it.hasParent }.map { it.component }


            //
        }

    // how to register things to other things?
    // register directly to hud and configs?
    // do it in a manager class??\
    // reflection?
    //

    // <clinit> all classes in a package, and have register code inside them? so they load themselves

}
