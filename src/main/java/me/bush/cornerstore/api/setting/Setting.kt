package me.bush.cornerstore.api.setting

import com.google.gson.JsonObject
import me.bush.cornerstore.api.common.Nameable
import me.bush.cornerstore.api.config.entry.SubConfig
import me.bush.cornerstore.api.gui.component.Displayable
import me.bush.cornerstore.api.setting.settings.ParentSetting
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author bush
 * @since 12/16/2021
 */
abstract class Setting<T>(
    override val name: String,
    val default: T
) : Displayable, SubConfig, Nameable, ReadWriteProperty<Any, T> {
    val observers = mutableListOf<(T) -> Unit>()
    var visibility: () -> Boolean = { true }
    val visible get() = visibility()
    var transient = false
    var info: String? = null
    var hasParent = false
    val order = orders
    open var value = default
        set(value) {
            if (field == value) return
            field = value
            observers.forEach { it(value) }
        }

    fun reset() {
        value = default
    }

    fun isValue(value: T) = value == this.value

    override fun getValue(thisRef: Any, property: KProperty<*>) = value

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }

    final override fun fromJson(config: JsonObject) {
        if (!transient) fromConfig(config)
    }

    final override fun toJson(config: JsonObject) {
        if (!transient) toConfig(config)
    }

    abstract fun fromConfig(config: JsonObject)

    abstract fun toConfig(config: JsonObject)

    companion object {
        // Order only matters within modules,
        // this doesn't have to be atomic
        private var orders = 0
            get() = field++
    }
}

/*
 * Need to use extension functions here because you can't return a subclass from a superclass method.
 * For example, if Setting.kt had an onChange method, it would return Setting, not BooleanSetting.
 * Generics here allow us to return the subclass.
 */

fun <S : Setting<T>, T> S.onChange(observer: (T) -> Unit): S {
    observers += observer
    return this
}

fun <S : Setting<T>, T> S.withVis(visibility: () -> Boolean): S {
    this.visibility = visibility
    return this
}

fun <S : Setting<T>, T> S.withInfo(info: String): S {
    this.info = info
    return this
}

fun <T, S : Setting<T>> S.withParent(parent: ParentSetting): S {
    parent.addChildren(this)
    return this
}

fun <S : Setting<T>, T> S.setTransient(): S {
    transient = true
    return this
}
