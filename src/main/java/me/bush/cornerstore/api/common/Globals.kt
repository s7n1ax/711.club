package me.bush.cornerstore.api.common

import net.minecraft.client.Minecraft

inline val mc: Minecraft get() = Minecraft.getMinecraft()

// This is read every time text is rendered to the screen (10+ times per frame!) so
// it makes sense to make it static instead of calling Cornerstore.Companion.getInitialized()
// This is turned on in ManagerLoader
var initialized = false
