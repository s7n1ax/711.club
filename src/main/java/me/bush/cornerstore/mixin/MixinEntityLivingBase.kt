package me.bush.cornerstore.mixin

import me.bush.cornerstore.api.event.EventBus
import me.bush.cornerstore.api.event.events.PushEvent
import me.bush.cornerstore.api.event.events.PushEvent.Type.PLAYER
import me.bush.cornerstore.util.lang.invoke
import net.minecraft.entity.EntityLivingBase
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

/**
 * @author bush
 * @since 2/3/2022
 */
@Mixin(EntityLivingBase::class)
class MixinEntityLivingBase {

    // This will make entities in singleplayer not collide either, but it is much more compatible with other clients
    @Inject(method = ["collideWithNearbyEntities"], at = [At("HEAD")], cancellable = true)
    fun collideWithNearbyEntities(callback: CallbackInfo) {
        if (EventBus.post(PushEvent(PLAYER))) callback()
    }
}
