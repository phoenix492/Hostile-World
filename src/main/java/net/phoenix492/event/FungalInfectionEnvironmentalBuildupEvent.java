package net.phoenix492.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * Fires before and after the fungal infection buildup from the environment effect is applied to an entity.<br>
 * {@link Pre} is cancellable, in which case the buildup won't be applied to the entity the event was posted for.
 */
public abstract class FungalInfectionEnvironmentalBuildupEvent extends LivingEvent {

    public FungalInfectionEnvironmentalBuildupEvent(LivingEntity entity) {
        super(entity);
    }

    /**
     * Cancellable, in which case this player's environmental fungal infection buildup won't be applied to the entity.
     */
    public static class Pre extends FungalInfectionEnvironmentalBuildupEvent implements ICancellableEvent {
        public Pre(LivingEntity entity) {
            super(entity);
        }
    }

    public static class Post extends FungalInfectionEnvironmentalBuildupEvent {
        public Post(LivingEntity entity) {
            super(entity);
        }
    }


}
