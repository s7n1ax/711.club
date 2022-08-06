package me.bush.cornerstore.api.setting.settings

import me.bush.cornerstore.util.lang.invokeAll
import kotlin.reflect.KMutableProperty0

/**
 * Gets value from a property reference, and inverts that reference when toggled.
 * You can also set value with onChange
 *
 * ```
 * BoolRefSetting("Name", ::someBoolean)/* optional */.onChange { someOtherBoolean = it }
 * ```
 *
 * @author bush
 * @since 2/26/2022
 */
class BoolRefSetting(name: String, val reference: KMutableProperty0<Boolean>) : BooleanSetting(name, reference()) {

    init {
        transient = true
    }

    override var value
        get() = reference.get()
        set(value) {
            reference.set(value)
            observers.invokeAll(value)
        }
}
