package me.bush.cornerstore.api.load

import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineStart.LAZY
import me.bush.cornerstore.CornerStore
import me.bush.cornerstore.api.common.*
import me.bush.cornerstore.util.lang.*
import me.bush.cornerstore.util.system.ReflectUtil
import me.bush.cornerstore.util.system.subclasses
import org.lwjgl.opengl.Display
import kotlin.collections.set

object LoadHandler {
    private val primaryMap = mutableMapOf<Manager, Job>()
    private val secondaryMap = mutableMapOf<Manager, Job>()
    private val managers = mutableListOf<Manager>()
    private const val logo = """
 /$$$$$$$$  /$$     /$$        /$$$$$$  /$$       /$$   /$$ /$$$$$$$ 
|_____ $$//$$$$   /$$$$       /$${"$"}__  $$| $$      | $$  | $$| $${"$"}__  $$
     /$$/|_  $$  |_  $$      | $$  \__/| $$      | $$  | $$| $$  \ $$
    /$$/   | $$    | $$      | $$      | $$      | $$  | $$| $$$$$$$ 
   /$$/    | $$    | $$      | $$      | $$      | $$  | $$| $${"$"}__  $$
  /$$/     | $$    | $$      | $$    $$| $$      | $$  | $$| $$  \ $$
 /$$/     /$$$$$$ /$$$$$$ /$$|  $$$$$$/| $$$$$$$$|  $$$$$$/| $$$$$$$/
|__/     |______/|______/|__/ \______/ |________/ \______/ |_______/        
    """

    /**
     * Starts some stuff that can be loaded before Minecraft is
     * initialized, so we do not have to wait for it later on.
     */
    fun earlyLoad() {
        ReflectUtil.warmup()
    }

    /**
     * This is used to start the [Manager.primaryLoad] method, and keep the job
     * in a map, so [awaitPrimary] can wait on that job. Managers will [Manager.secondaryLoad]
     * only after all managers have completed [Manager.primaryLoad].
     */
    fun primaryLoad() {
        logo.lines().filterNotEmpty().forEach(Logger::info)
        Logger.info("${CornerStore.ALIAS} primary loading...")
        logLoadTime("managers") {
            Manager::class.subclasses
                .mapNotNull { it.objectInstance }
                .toCollection(managers)
            managers.size
        }
        Background.launch {
            managers.forEach {
                primaryMap[it] = launch(start = LAZY) {
                    it.primaryLoad()
                }
            }
            // Only start when all jobs are launched
            primaryMap.values.forEach(Job::start)
        }
    }

    /**
     * This waits for all managers to finish [Manager.primaryLoad], launches
     * [Manager.secondaryLoad] on seperate coroutines, and awaits completion.
     */
    fun secondaryLoad() {
        Logger.info("${CornerStore.ALIAS} secondary loading...")
        runBlocking(Background) {
            // Wait for all the managers to complete primary load
            primaryMap.values.joinAll()
            managers.forEach {
                secondaryMap[it] = launch(start = LAZY) {
                    it.secondaryLoad()
                }
            }
            // Only start when all jobs are launched
            secondaryMap.values.forEach(Job::start)
        }
        Background.launch {
            delay(5000)
            mainThread {
                // Guerilla marketing
                Display.setTitle(CornerStore.ALIAS)
            }
        }
        ReflectUtil.cleanup()
        ReflectUtil.antiFeather()
        initialized = true
        Logger.info("${CornerStore.ALIAS} initialized.")
    }

    /**
     * This is used to suspend the current coroutine until
     * the given manager has completed [Manager.primaryLoad].
     */
    suspend fun <T : Manager> T.awaitPrimary(): T {
        // todo noat test thing
        println("${this::class} pre .join()")
        primaryMap[this]!!.join()
        println("${this::class} post .join()")
        return this
    }

    /**
     * This is used to suspend the current coroutine until
     * the given manager has completed [Manager.secondaryLoad].
     */
    suspend fun <T : Manager> T.awaitSecondary(): T {
        secondaryMap[this]!!.join()
        return this
    }
}
