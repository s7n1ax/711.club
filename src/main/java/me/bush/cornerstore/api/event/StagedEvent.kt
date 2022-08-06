package me.bush.cornerstore.api.event

import me.bush.eventbuskotlin.Event

/**
 * @author bush
 * @since 1/31/2022
 */
abstract class StagedEvent(val stage: Stage) : Event() {
    override val cancellable = stage == Stage.PRE

    enum class Stage {
        PRE, POST
    }
}
