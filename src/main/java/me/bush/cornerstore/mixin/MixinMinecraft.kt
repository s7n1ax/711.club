package me.bush.cornerstore.mixin

import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.events.ShutdownEvent
import me.bush.cornerstore.impl.module.modules.misc.MultiTask
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.multiplayer.PlayerControllerMP
import net.minecraft.client.settings.GameSettings
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

/**
 * @author bush kami blue
 * @since 3/2/2022
 */
@Mixin(Minecraft::class)
abstract class MixinMinecraft {

    @Shadow
    protected abstract fun clickMouse()

    @Shadow
    lateinit var player: EntityPlayerSP

    @Shadow
    lateinit var playerController: PlayerControllerMP

    @Shadow
    lateinit var gameSettings: GameSettings

    private var isHittingBlock = false
    private var isHandActive = false

    // This is called when minecraft crashes, not when it exits normally
    @Inject(method = ["displayCrashReport"], at = [At("HEAD")])
    fun displayCrashReport(callback: CallbackInfo) {
        EventBus.post(ShutdownEvent())
    }

    // This is called when minecraft exits normally
    @Inject(method = ["shutdown"], at = [At("HEAD")])
    fun shutdown(callback: CallbackInfo) {
        EventBus.post(ShutdownEvent())
    }

    // Lets u attack entities while eating
    @Inject(method = ["processKeyBinds"], at = [At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z", shift = At.Shift.BEFORE, ordinal = 2)])
    fun processKeyBindsPost(callback: CallbackInfo) {
        if (player.isHandActive && MultiTask.enabled && MultiTask.attackAndEat) {
            while (gameSettings.keyBindAttack.isPressed) clickMouse()
        }
    }

    // Following 2 methods allow right clicking while mining (eating, etc)
    @Inject(method = ["rightClickMouse"], at = [At("HEAD")])
    fun rightClickMousePre(callback: CallbackInfo) {
        isHittingBlock = playerController.isHittingBlock
        if (MultiTask.enabled && MultiTask.mineWhileEating) playerController.isHittingBlock = false
    }

    @Inject(method = ["rightClickMouse"], at = [At("RETURN")])
    fun rightClickMousePost(callback: CallbackInfo) {
        if (MultiTask.enabled && MultiTask.mineWhileEating) playerController.isHittingBlock = isHittingBlock
    }

    // Following 2 methods allow mining while hand is active (eating, etc)
    @Inject(method = ["sendClickBlockToController"], at = [At("HEAD")])
    fun sendClickBlockToControllerPre(click: Boolean, callback: CallbackInfo) {
        isHandActive = player.handActive
        if (MultiTask.enabled && MultiTask.mineWhileEating) player.handActive = false
    }

    @Inject(method = ["sendClickBlockToController"], at = [At("RETURN")])
    fun sendClickBlockToControllerPost(click: Boolean, callback: CallbackInfo) {
        if (MultiTask.enabled && MultiTask.mineWhileEating) player.handActive = isHandActive
    }
}
