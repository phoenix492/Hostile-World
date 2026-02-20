package net.phoenix492.data.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Record containing information about how fungus spreads through biomes.
 * @param spreadSuccessChance Percentage chance as a decimal for fungal spread to succeed in this biome. Useful for slowing down or disabling fungal spread in biomes that should have a "resistance" to it.
 * @param germinationChance Percentage chance as a decimal that a ticking grass block will turn into a mycelium block in this biome if spontaenous germination is enabled in the config.
 */
public record BiomeFungalSpreadData(float spreadSuccessChance, float germinationChance) {
    public static final Codec<BiomeFungalSpreadData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.optionalFieldOf("spreadSuccessChance", 1f).forGetter(BiomeFungalSpreadData::spreadSuccessChance),
        Codec.FLOAT.optionalFieldOf("germinationChance", 0f).forGetter(BiomeFungalSpreadData::germinationChance)
    ).apply(instance, BiomeFungalSpreadData::new));
}
