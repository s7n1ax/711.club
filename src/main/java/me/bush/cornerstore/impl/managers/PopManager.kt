package me.bush.cornerstore.impl.managers

import me.bush.cornerstore.api.common.Manager
import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.events.DeathEvent
import me.bush.cornerstore.api.event.events.PopEvent
import me.bush.eventbuskotlin.listener
import net.minecraft.entity.Entity

/**
 * @author bush
 * @since 2/6/2022
 */
object PopManager : Manager {
    private val popMap = mutableMapOf<Entity, Int>()

    override suspend fun primaryLoad() {
        EventBus.register(listener<PopEvent> {
            popMap[it.entity] = (popMap[it.entity] ?: 0) + 1
        })
        EventBus.register(listener<DeathEvent> {
            popMap[it.entity] = 0
        })
    }
    // TODO: 4/24/2022 reset when they die out of range


    fun getPopsForPlayer(player: Entity) = popMap[player] ?: 0
}
