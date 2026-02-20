package net.phoenix492.data.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Record containing information on how interacting with a given block infects entities.
 * @param onBreak Whether to build infection on entities who break this block. Blocks with this set to "true" will <i>poof</i> with mushroom particles.
 * @param breakQuantity Quantity of infection to build on entities who break this block.
 * @param onStand Whether to build infection on entities standing on this block.
 * @param standQuantity Quantity of infection to build on entities standing on this block.
 */
public record BlockInfectionBuildupData(boolean onBreak, int breakQuantity, boolean onStand, int standQuantity) {
    public static final Codec<BlockInfectionBuildupData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("onBreak", false).forGetter(BlockInfectionBuildupData::onBreak),
        Codec.INT.optionalFieldOf("breakQuantity", 0).forGetter(BlockInfectionBuildupData::breakQuantity),
        Codec.BOOL.optionalFieldOf("onStand", false).forGetter(BlockInfectionBuildupData::onStand),
        Codec.INT.optionalFieldOf("standQuantity", 0).forGetter(BlockInfectionBuildupData::standQuantity)
    ).apply(instance, BlockInfectionBuildupData::new));
}
