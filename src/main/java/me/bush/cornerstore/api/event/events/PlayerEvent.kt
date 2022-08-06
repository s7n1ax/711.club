package me.bush.cornerstore.api.event.events

import me.bush.cornerstore.api.event.StagedEvent

/**
 * Wrappers for some forge entity events that are called on our clientplayer.
 * Only called for us, no other players.
 *
 * @author bush
 * @since 3/5/2022
 */
open class PlayerEvent private constructor(stage: Stage) : StagedEvent(stage) {
    class Walking(stage: Stage) : PlayerEvent(stage)
}
