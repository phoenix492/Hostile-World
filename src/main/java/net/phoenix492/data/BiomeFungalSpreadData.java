package net.phoenix492.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Record containing information about how fungus spreads through biomes.
 * @param spreadSuccessChance Percentage chance as a decimal for fungal spread to succeed in this biome. Useful for slowing down or disabling fungal spread in biomes that should have a "resistance" to it.
 */
public record BiomeFungalSpreadData(float spreadSuccessChance) {
    public static final Codec<BiomeFungalSpreadData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.optionalFieldOf("spreadSuccessChance", 1f).forGetter(BiomeFungalSpreadData::spreadSuccessChance)
    ).apply(instance, BiomeFungalSpreadData::new));
}
