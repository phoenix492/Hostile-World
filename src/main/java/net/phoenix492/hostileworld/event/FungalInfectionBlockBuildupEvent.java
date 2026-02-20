package net.phoenix492.hostileworld.event;

import net.phoenix492.hostileworld.data.map.BlockInfectionBuildupData;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * Fires before and after the fungal infection buildup from block events is applied to an entity.<br>
 * {@link Pre} is cancellable, in which case the buildup won't be applied to the entity the event was posted for.
 */
public abstract class FungalInfectionBlockBuildupEvent extends LivingEvent {
    private BlockInfectionBuildupData buildupData;
    private final BlockState blockstate;

    public FungalInfectionBlockBuildupEvent(LivingEntity entity, BlockState blockstate, BlockInfectionBuildupData buildupData) {
        super(entity);
        this.blockstate = blockstate;
        this.buildupData = buildupData;
    }

    /**
     * Cancellable, in which case this block's fungal infection buildup won't be applied to the entity.
     */
    public static class Pre extends FungalInfectionBlockBuildupEvent implements ICancellableEvent {
        public Pre(LivingEntity entity, BlockState blockstate, BlockInfectionBuildupData buildupData) {
            super(entity, blockstate, buildupData);
        }
    }

    public static class Post extends FungalInfectionBlockBuildupEvent {
        public Post(LivingEntity entity, BlockState blockstate, BlockInfectionBuildupData buildupData) {
            super(entity, blockstate, buildupData);
        }
    }

    public BlockInfectionBuildupData getBuildupData() {
        return buildupData;
    }

    public void setBuildupData(BlockInfectionBuildupData buildupData) {
        this.buildupData = buildupData;
    }

    public BlockState getBlockState() {
        return blockstate;
    }

}
