package me.bush.cornerstore.api.event.events

import me.bush.eventbuskotlin.Event

/**
 * @author bush
 * @since 2/3/2022
 */
class PushEvent(val type: Type) : Event() {
    override val cancellable = true

    enum class Type {
        PLAYER, LIQUID
    }
}
