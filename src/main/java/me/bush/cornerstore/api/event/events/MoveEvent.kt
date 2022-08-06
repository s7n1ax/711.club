package me.bush.cornerstore.api.event.events

import me.bush.eventbuskotlin.Event
import net.minecraft.entity.MoverType
import kotlin.math.abs

/**
 * @author bush
 * @since 1/31/2022
 */
class MoveEvent(var mover: MoverType, var x: Double, var y: Double, var z: Double) : Event() {

    val moving get() = x != 0.0 || abs(y) > 0.1 || z != 0.0

    override val cancellable = true
}
