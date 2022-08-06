package me.bush.cornerstore.util.lang

import me.bush.cornerstore.api.event.StagedEvent
import me.bush.cornerstore.api.event.StagedEvent.Stage
import me.bush.eventbuskotlin.EventBus
import net.minecraft.client.Minecraft
import org.apache.commons.lang3.StringUtils
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.text.DecimalFormat
import java.util.*
import net.minecraftforge.fml.common.eventhandler.Event as ForgeEvent

/**
 * Rotate elements by the specified amount.
 */
fun <T : List<E>, E> T.rotate(distance: Int) = also { Collections.rotate(it, distance) }

/**
 * Checks if a given list has duplicates of the value specified in the map function.
 */
inline fun <T : List<E>, E> T.hasDuplicates(duplicateMap: (E) -> Any) = map(duplicateMap).toSet().size != size

/**
 * Calls the given [StagedEvent] with [Stage.PRE], executes the block if
 * the event wasn't cancelled, and then sends the event with [Stage.POST]
 */
inline fun EventBus.postStaged(event: (Stage) -> StagedEvent, block: () -> Unit) {
    if (!post(event(Stage.PRE))) {
        block()
    }
    post(event(Stage.POST))
}

fun ForgeEvent.cancel() {
    isCanceled = true
}

fun <T> List<(T) -> Unit>.invokeAll(arg: T) = forEach { it(arg) }

/**
 * Lazy delegate without thread safety.
 */
fun <T> unsafeLazy(block: () -> T) = lazy(LazyThreadSafetyMode.NONE, block)

fun List<String>.filterNotEmpty() = filter { it.isNotEmpty() }

inline val Map<*, *>.flipped get() = entries.associate { it.value to it.key }

private val GUI_FORMAT = DecimalFormat("0.##")

/**
 * Returns the string with first char capital, all others lowercase.
 */
fun String.properNoun() = lowercase().replaceFirstChar(Char::uppercase)

/**
 * Splits a string on whitespace characters.
 */
fun String.split(): Array<String> = StringUtils.split(this)

/**
 * Tries to format the string as a number, returns itself if it cannot parse to double.
 */
fun String.tryFormat(): String = runCatching { GUI_FORMAT.format(toDouble()) }.getOrElse { this }

/**
 * callback(someValue)
 */
operator fun <T> CallbackInfoReturnable<T>.invoke(value: T) {
    returnValue = value
}

/**
 * callback()
 */
operator fun CallbackInfo.invoke() = cancel()

inline val Minecraft.anynull get() = player == null || world == null
