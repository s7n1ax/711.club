package me.bush.cornerstore.api.event

import me.bush.cornerstore.CornerStore
import me.bush.cornerstore.util.lang.Background
import me.bush.eventbuskotlin.Config
import org.apache.logging.log4j.LogManager
import me.bush.eventbuskotlin.EventBus as BushBus

/**
 * Wrapper object for the eventboos
 *
 * @author bush
 * @since 2/26/2022
 */
object EventBus : BushBus(
    Config(
        logger = LogManager.getLogger(CornerStore.NAME),
        parallelScope = Background,
        thirdPartyCompatibility = true,
        annotationRequired = true
    )
)
