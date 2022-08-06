package me.bush.cornerstore.api.event.events

import me.bush.eventbuskotlin.Event

/**
 * @author bush
 * @since 3/8/2022
 */
class HighlightEvent : Event() {
    var width = 2f
    var red = 0f
    var green = 0f
    var blue = 0f
    var alpha = 0.4f
    var grow = 0.002

    val changed get() = width != 2f || red != 0f || green != 0f || blue != 0f || alpha != 0.4f || grow != 0.002

    override val cancellable = true
}
