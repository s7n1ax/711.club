package me.bush.cornerstore.mixin

import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.StagedEvent.Stage.POST
import me.bush.cornerstore.api.event.StagedEvent.Stage.PRE
import me.bush.cornerstore.api.event.events.MoveEvent
import me.bush.cornerstore.api.event.events.PlayerEvent.Walking
import me.bush.cornerstore.util.lang.*
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.MoverType
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.*
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

/**
 * @author bush
 * @since jan 2022
 */
@Mixin(EntityPlayerSP::class)
abstract class MixinEntityPlayerSP : AbstractClientPlayer(FUCK, OFF) {

    @Inject(method = ["onUpdateWalkingPlayer"], at = [At("HEAD")], cancellable = true)
    fun onUpdateWalkingPlayerPre(callback: CallbackInfo) {
        if (EventBus.post(Walking(PRE))) callback()
    }

    @Inject(method = ["onUpdateWalkingPlayer"], at = [At("TAIL")])
    fun onUpdateWalkingPlayerPost(callback: CallbackInfo) {
        EventBus.post(Walking(POST))
    }

    // Redirects the call to super.move() in EntityPlayerSP#move()
    @Redirect(method = ["move"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    fun move(instance: AbstractClientPlayer, moverType: MoverType, x: Double, y: Double, z: Double) {
        MoveEvent(moverType, x, y, z).let {
            if (!EventBus.post(it)) super.move(it.mover, it.x, it.y, it.z)
        }
    }
}
