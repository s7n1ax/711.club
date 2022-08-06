package me.bush.cornerstore.api.event.events

import me.bush.eventbuskotlin.Event

/**
 * @author bush
 * @since 2/26/2022
 */
class ShutdownEvent : Event() {
    override val cancellable = false
}
