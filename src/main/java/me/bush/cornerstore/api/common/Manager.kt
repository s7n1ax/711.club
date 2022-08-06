package me.bush.cornerstore.api.common

import me.bush.cornerstore.api.load.LoadHandler
import me.bush.cornerstore.api.load.LoadHandler.awaitPrimary
import me.bush.cornerstore.api.load.LoadHandler.awaitSecondary

/**
 * Interface for client managers. Do most loading in [primaryLoad], and use
 * [secondaryLoad] if you need something to load last. Use [LoadHandler.awaitPrimary]
 * and [LoadHandler.awaitSecondary] functions to pause the current coroutine until the other
 * manager is done loading. Code in the `init { }` blocks will run on the main
 * thread, so avoid computationally expensive operations.
 *
 * @author bush
 * @since 2/10/21
 */
interface Manager {

    suspend fun primaryLoad() {}

    suspend fun secondaryLoad() {}
}
