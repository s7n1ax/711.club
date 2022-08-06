package me.bush.cornerstore

import me.bush.cornerstore.CornerStore.Companion.GUIHOOK
import me.bush.cornerstore.CornerStore.Companion.MODID
import me.bush.cornerstore.CornerStore.Companion.NAME
import me.bush.cornerstore.CornerStore.Companion.VERSION
import me.bush.cornerstore.api.load.LoadHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.*

/**
 * Started 9/4/21
 *
 * @author bush & perry.
 */
@Suppress("UNUSED", "UNUSED_PARAMETER") // i dont like warnings!!!! fuck you nigga!!!
@Mod(version = VERSION, modid = MODID, name = NAME, guiFactory = GUIHOOK)
class CornerStore {

    companion object {
        const val VERSION = "0.2.2"
        const val MODID = "cornerstore"
        const val NAME = "711.CLUB"
        const val GUIHOOK = "me.bush.cornerstore.impl.gui.clickgui.ForgeConfigHook"
        const val ALIAS = "$NAME $VERSION"

        @Mod.Instance
        lateinit var INSTANCE: CornerStore
    }

    @Mod.EventHandler
    fun earlyLoad(event: FMLPreInitializationEvent) = LoadHandler.earlyLoad()

    @Mod.EventHandler
    fun primaryLoad(event: FMLInitializationEvent) = LoadHandler.primaryLoad()

    @Mod.EventHandler
    fun secondaryLoad(event: FMLPostInitializationEvent) = LoadHandler.secondaryLoad()
}
