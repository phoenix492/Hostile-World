package net.phoenix492.hostileworld.event;

import net.phoenix492.hostileworld.handler.FungalInfectionHandler;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * Fires before and after fungal infection handling is ticked for an entity. <br>
 * {@link Pre} is cancellable, in which case fungal infection handling will be entirely skipped for the entity the event was posted for. <br>
 * Subscribing at highest priority will produce undefined behavior, since {@link FungalInfectionHandler#fungalInfectionRateLimiter(Pre)} does so to rate limit ticking.
 */
public abstract class FungalInfectionTickEvent extends LivingEvent {

    public FungalInfectionTickEvent(LivingEntity entity) {
        super(entity);
    }

    /**
     * Cancellable, in which case this entity will entirely skip infection handling this tick.
     */
    public static class Pre extends FungalInfectionTickEvent implements ICancellableEvent {
        public Pre(LivingEntity entity) {
            super(entity);
        }
    }

    public static class Post extends FungalInfectionTickEvent {

        public Post(LivingEntity entity) {
            super(entity);
        }
    }

}
