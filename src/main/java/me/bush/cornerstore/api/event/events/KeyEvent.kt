package me.bush.cornerstore.api.event.events

import me.bush.eventbuskotlin.Event

/**
 * @author bush
 * @since 9/15/2021
 */
open class KeyEvent private constructor(val key: Int, val pressed: Boolean, val repeat: Boolean) : Event() {
    override val cancellable = true

    class InGame(key: Int, pressed: Boolean, repeat: Boolean) : KeyEvent(key, pressed, repeat)
    class InGui(key: Int, pressed: Boolean, repeat: Boolean) : KeyEvent(key, pressed, repeat)
}
