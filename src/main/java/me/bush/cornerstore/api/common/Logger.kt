package me.bush.cornerstore.api.common

import me.bush.cornerstore.CornerStore
import me.bush.cornerstore.util.minecraft.ChatUtil
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger as ApacheLogger

/**
 * Object instance for logger, will add to this later.
 *
 * @author bush
 * @since 2/26/2022
 */
object Logger {
    private val logger: ApacheLogger = LogManager.getLogger(CornerStore.NAME)

    fun info(info: String, value: Any? = null) {
        ChatUtil.info(info, value)
        value?.let {
            logger.info(info, value)
        } ?: logger.info(info)
    }

    fun warn(warn: String) {
        logger.warn(warn)
        ChatUtil.warn(warn)
    }

    fun error(error: String) {
        logger.error(error)
        ChatUtil.error(error)
    }

    fun error(message: String, throwable: Throwable) {
        logger.error(message, throwable)
        ChatUtil.error(message)
    }
}
