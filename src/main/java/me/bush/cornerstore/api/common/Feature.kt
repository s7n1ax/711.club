package me.bush.cornerstore.api.common

import com.google.gson.JsonObject
import me.bush.cornerstore.api.config.ConfigManager
import me.bush.cornerstore.api.config.entry.ConfigEntry
import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.gui.Component
import me.bush.cornerstore.api.gui.component.*
import me.bush.cornerstore.api.setting.*
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.util.lang.*
import me.bush.cornerstore.util.system.*
import me.bush.eventbuskotlin.Listener
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.reflect.full.*

/**
 * @author bush
 * @since 3/4/2022
 */
abstract class Feature(private val description: String) : Displayable, ConfigEntry, Nameable {
    private val staticName = this::class.simpleName!!
    // List of all settings, used for gui and config stuff
    private val settings by unsafeLazy {
        // Get all settings through reflection, and put the default settings at the bottom
        getPropertiesOfType<Setting<*>>().sortedBy { it.order }.toList().rotate(-(preferences.children + 1)).also {
            // Check for duplicate names
            if (it.filterNot(Setting<*>::transient).hasDuplicates(Setting<*>::name)) {
                me.bush.cornerstore.api.common.Logger.warn("$name has settings with duplicate names. This may cause config errors.")
            }
        }
    }
    // Default settings, under the preferences tab
    private val nameSetting = StringSetting("Feature Name", staticName)
    private val globalConfig = BoolRefSetting("Global Config", ::global).withInfo("Config for this feature saves across all profiles")
    protected val preferences = ParentSetting("Preferences", nameSetting, globalConfig)
    // Whether or not to subscribe the feature to the event bus
    private val eventListening = this::class.hasMethodsOfType<Listener>()
    // Path to this feature's config
    final override val path: Path get() = Path("${category.lowercase()}/$staticName.json")
    final override val name get() = nameSetting.value
    // Category this feature is in
    abstract val category: String
    // If this is false, you cannot toggle the module in the ui.
    // You can still enable it in code, however.
    open val toggleable = true
    // If this is true, module enabled state will not be saved.
    open val transient = false

    override val component: Component
        get() {
            // Only take click input if toggleable
            return FeatureComponent(name, { enabled }, { if (toggleable) enabled = it }).apply {
                subComponents.addAll(settings.filter { !it.hasParent }.map { it.component })
                tooltip = description
                nameSetting.onChange { name = it }
            }
        }

    private var global
        get() = ConfigManager.isGlobal(this)
        set(value) = ConfigManager.setGlobal(this, value)

    var enabled = false
        set(value) {
            if (value == field) return
            field = value
            if (eventListening) {
                if (value) EventBus.subscribe(this)
                else EventBus.unsubscribe(this)
            }
            if (value) onEnable() else onDisable()
            onToggle()
        }

    open fun onEnable() {}

    open fun onDisable() {}

    open fun onToggle() {}

    fun toggle() {
        enabled = !enabled
    }

    final override fun fromJson(config: JsonObject) {
        settings.forEach {
            // Not catching exceptions in settings, and deliberately doing it here so when they fail
            // we can log the feature name for easier debugging.
            logExceptions("There was an error while loading setting ${it.name} for feature $name") {
                it.fromJson(config)
            }
        }
        // Additional stuff
        logExceptions("There was an error while loading feature $name") {
            fromConfig(config)
            if (!transient) enabled = config.get("Enabled").asBoolean
        }
    }

    final override fun toJson() = JsonObject().apply {
        settings.forEach { it.toJson(this) }
        // Additional feature specific configs
        toConfig(this)
        if (!transient) addProperty("Enabled", enabled)
    }

    open fun fromConfig(config: JsonObject) {}

    open fun toConfig(config: JsonObject) {}
}
