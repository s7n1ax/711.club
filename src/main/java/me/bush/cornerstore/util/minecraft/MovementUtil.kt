package me.bush.cornerstore.util.minecraft

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.init.MobEffects
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author bush
 * @since 2/1/2022
 */
val EntityPlayerSP.movementSpeed: Double
    get() {
        // Credit phobos for the 0.2873 part
        var max = 0.2873f
        val speed = getActivePotionEffect(MobEffects.SPEED)
        if (speed != null) {
            max *= 1 + 0.2f * speed.amplifier
        }
        val slowness = getActivePotionEffect(MobEffects.SLOWNESS)
        if (slowness != null) {
            max *= 1 - 0.15f * slowness.amplifier
        }
        if (isSneaking) {
            max *= 0.3f
        }
        return max.toDouble()
    }

fun EntityPlayerSP.getDirectionComponents(speed: Double): DoubleArray {
    var forward = movementInput.moveForward
    var strafe = movementInput.moveStrafe
    // Credit seppuku
    var yaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * mc.renderPartialTicks
    if (forward != 0f) {
        if (strafe > 0) yaw += (if (forward > 0) -45 else 45).toFloat() else if (strafe < 0) yaw += (if (forward > 0) 45 else -45).toFloat()
        strafe = 0f
        if (forward > 0) forward = 1f else if (forward < 0) forward = -1f
    }
    if (strafe > 0) strafe = 1f else if (strafe < 0) strafe = -1f
    val sin = sin(Math.toRadians((yaw + 90).toDouble()))
    val cos = cos(Math.toRadians((yaw + 90).toDouble()))
    val posX = forward * speed * cos + strafe * speed * sin
    val posZ = forward * speed * sin - strafe * speed * cos
    return doubleArrayOf(posX, posZ)
}

fun EntityPlayerSP.shouldMovementHack(liquidCheck: Boolean = true): Boolean {
    return !(isInLiquid && liquidCheck) && !capabilities.isFlying && !isElytraFlying && !isSpectator && !isOnLadder && !noClip
}
