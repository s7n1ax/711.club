package me.bush.cornerstore.util.minecraft

import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.util.lang.filterNotEmpty
import net.minecraft.util.ResourceLocation
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.Month

object SplashUtil {
    private val splashes = ResourceLocation("texts/splashes.txt")
    private val additional = listOf(
        "Bush is so cool!",
        "Powered by github.com/therealbush/eventbus-kotlin!",
        "This shit cat is bitch!",
        "if you read this ur gay",
        "custom splash wowowowowow",
        "Larry approves!",
        ":larryautism:",
        "Also try SexHack!",
        "Also try Perry Phobos!",
        "oh my god please get me out of here",
        "I am slowly losing my mind!",
        "I am hallucinating spiders!",
        "5343 Weke Rd, Hanalei, HI 96714",
        "We off the grid grid grid!",
        "Ball so hard mothafuckas wanna fine me, first niggas gotta find me",
        "Nobody loves you!",
        "You are loved!",
        "Couldn't afford a car so she named her daughter Alexis!",
        "You should be doing homework!",
        "Slightly scratched your Corolla",
        "Okay I smashed your Corolla",
        "Mayonaise colored Benz, I push miracle whips",
        "Also try DMT!"
    )

    fun getRandomSplash() = LocalDate.now().let { date ->
        when {
            date.month == Month.APRIL && date.dayOfMonth == 20 -> "Haha! Weed number!"
            date.month == Month.OCTOBER && date.dayOfMonth == 31 -> "OOoooOOOoooo! Spooky!"
            date.month == Month.DECEMBER && date.dayOfMonth == 25 -> "Merry X-mas!"
            date.month == Month.DECEMBER && date.dayOfMonth == 31 -> "Happy new year!"
            else -> runCatching {
                mc.resourceManager.getResource(splashes).use {
                    (it.inputStream.bufferedReader(StandardCharsets.UTF_8)
                        .readLines().filterNotEmpty() + additional).random().trim()
                }
            }.getOrElse { "Ratted bozo" }
        }
    }
}
