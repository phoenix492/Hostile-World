package net.phoenix492.hostileworld.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * Fires before and after the <i>fungal_infection</i> effect is applied to a player.<br>
 * {@link Pre} is cancellable, in which case the effect won't be applied to the entity the event was posted for.
 */
public abstract class FungalInfectionApplyEffectEvent extends LivingEvent {
    protected final MobEffectInstance effect;

    public FungalInfectionApplyEffectEvent(LivingEntity entity, MobEffectInstance effect) {
        super(entity);
        this.effect = effect;
    }

    /**
     * Cancellable, in which the fungal infection effect won't be applied for this entity.
     */
    public static class Pre extends FungalInfectionApplyEffectEvent implements ICancellableEvent {
        public Pre(LivingEntity entity, MobEffectInstance effect) {
            super(entity, effect);
        }
    }

    public static class Post extends FungalInfectionApplyEffectEvent{
        public Post(LivingEntity entity, MobEffectInstance effect) {
            super(entity, effect);
        }
    }

    public MobEffectInstance getEffect() {
        return this.effect;
    }

}
