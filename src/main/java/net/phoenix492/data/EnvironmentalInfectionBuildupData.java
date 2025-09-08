package net.phoenix492.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Object containing information on a biome's fungal infection buildup to entities within it.
 */
public record EnvironmentalInfectionBuildupData(int buildupQuantity) {
    public static final Codec<EnvironmentalInfectionBuildupData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.optionalFieldOf("buildupQuantity", 0).forGetter(EnvironmentalInfectionBuildupData::buildupQuantity)
    ).apply(instance, EnvironmentalInfectionBuildupData::new));
}
