package net.phoenix492.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * Fires before and after fungal infection drop-off is applied to an entity. <br>
 * {@link Pre} is cancellable, in which case fungal infection drop-off will not be applied to the entity the event was posted for.
 */
public abstract class FungalInfectionDropoffEvent extends LivingEvent {

    public FungalInfectionDropoffEvent(LivingEntity entity) {
        super(entity);
    }

    /**
     * Cancellable, in which case this entity's environmental fungal infection buildup won't increase.
     */
    public static class Pre extends FungalInfectionDropoffEvent implements ICancellableEvent {
        public Pre(LivingEntity entity) {
            super(entity);
        }
    }

    public static class Post extends FungalInfectionDropoffEvent {
        public Post(LivingEntity entity) {
            super(entity);
        }
    }

}
