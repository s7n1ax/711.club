package me.bush.cornerstore.util.lang

import kotlinx.coroutines.*
import me.bush.cornerstore.CornerStore
import me.bush.cornerstore.api.common.mc
import net.minecraft.crash.CrashReport
import kotlin.coroutines.CoroutineContext

val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
    mc.crashed(
        CrashReport(
            """
            ${CornerStore.NAME}: An uncaught exception was thrown from a coroutine. This means something 
            bad happened that would probably make the game unplayable if it wasn't shut down.
            
            Context: $context
            
            DM bush and tell him to fix his shitcode! (also please send him this whole log)
            
            I don't know what else to put here, so heres a big 711.CLUB thing:
            
             /$$$$$$$$  /$$     /$$        /$$$$$$  /$$       /$$   /$$ /$$$$$$$ 
            |_____ $$//$$$$   /$$$$       /$${"$"}__  $$| $$      | $$  | $$| $${"$"}__  $$
                 /$$/|_  $$  |_  $$      | $$  \__/| $$      | $$  | $$| $$  \ $$
                /$$/   | $$    | $$      | $$      | $$      | $$  | $$| $$$$$$$ 
               /$$/    | $$    | $$      | $$      | $$      | $$  | $$| $${"$"}__  $$
              /$$/     | $$    | $$      | $$    $$| $$      | $$  | $$| $$  \ $$
             /$$/     /$$$$$$ /$$$$$$ /$$|  $$$$$$/| $$$$$$$$|  $$$$$$/| $$$$$$$/
            |__/     |______/|______/|__/ \______/ |________/ \______/ |_______/   
            """.trimIndent(), throwable
        )
    )
}

val defaultContext = Dispatchers.Default + exceptionHandler

object Background : CoroutineScope by CoroutineScope(defaultContext), CoroutineContext by defaultContext

// big
fun mainThread(block: () -> Unit) {
    mc.addScheduledTask(block)
}

// BIG
fun backgroundThread(block: suspend CoroutineScope.() -> Unit) {
    Background.launch(block = block)
}
