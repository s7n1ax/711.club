package me.bush.cornerstore.api.load

import me.bush.cornerstore.util.system.NetworkUtil

/**
 * @author bush
 * @since 3/4/2022
 */


class Installer {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            println("")
            NetworkUtil.getAsciiText("711.CLUB")
                .filter { it.isNotBlank() }
                .forEach { println(it) }
            println("")
            println("Currently not implemented! come back soon!")
        }
    }
}
