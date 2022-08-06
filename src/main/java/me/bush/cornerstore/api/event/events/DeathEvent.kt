package me.bush.cornerstore.api.event.events

import me.bush.eventbuskotlin.Event
import net.minecraft.entity.Entity

/**
 * @author bush
 * @since 2/3/2022
 */
class DeathEvent(val entity: Entity) : Event() {
    override val cancellable = false
}
