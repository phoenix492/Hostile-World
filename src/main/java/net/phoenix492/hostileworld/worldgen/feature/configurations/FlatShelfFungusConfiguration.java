package net.phoenix492.hostileworld.worldgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import com.google.common.collect.ImmutableList;

public record FlatShelfFungusConfiguration(int width, int inwardCapLength, int outwardCapLength, List<Block> validWallTargets, int minStemHeight, int maxStemHeight, int minStemLength, int maxStemLength) implements FeatureConfiguration {
    private static final List<Block> DEFAULT_BLOCK_LIST = List.of(Blocks.STONE);

    public static Codec<FlatShelfFungusConfiguration> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.optionalFieldOf("capWidth", 2).forGetter(FlatShelfFungusConfiguration::width),
            Codec.INT.optionalFieldOf("inwardCapLength", 2).forGetter(FlatShelfFungusConfiguration::inwardCapLength),
            Codec.INT.optionalFieldOf("outwardCapLength", 1).forGetter(FlatShelfFungusConfiguration::outwardCapLength),
            Codec.list(BuiltInRegistries.BLOCK.byNameCodec()).optionalFieldOf("validWallTargets", DEFAULT_BLOCK_LIST).forGetter(FlatShelfFungusConfiguration::validWallTargets),
            Codec.INT.optionalFieldOf("minStemHeight", 2).forGetter(FlatShelfFungusConfiguration::minStemHeight),
            Codec.INT.optionalFieldOf("maxStemHeight", 3).forGetter(FlatShelfFungusConfiguration::maxStemHeight),
            Codec.INT.optionalFieldOf("minStemLength", 2).forGetter(FlatShelfFungusConfiguration::minStemLength),
            Codec.INT.optionalFieldOf("maxStemLength", 3).forGetter(FlatShelfFungusConfiguration::maxStemLength)
        ).apply(instance, FlatShelfFungusConfiguration::new)
    );

    public static FlatShelfFungus builder() {
        return new FlatShelfFungus();
    }

    public static class FlatShelfFungus {
        private int width = 2;
        private int inwardCapLength = 2;
        private int outwardCapLength = 1;
        private int minStemHeight = 2;
        private int maxStemHeight = 3;
        private int minStemLength = 2;
        private int maxStemLength = 3;
        private final ImmutableList.Builder<Block> validWallTargets = ImmutableList.builder();

        public FlatShelfFungus width(int width) {
            this.width = width;
            return this;
        }

        public FlatShelfFungus inwardLength(int inwardLength) {
            this.inwardCapLength = inwardLength;
            return this;
        }

        public FlatShelfFungus outwardCapLength(int outwardCapLength) {
            this.outwardCapLength = outwardCapLength;
            return this;
        }

        public FlatShelfFungus addValidWallTarget(Block block) {
            validWallTargets.add(block);
            return this;
        }

        public FlatShelfFungus minStemHeight(int minStemHeight) {
            this.minStemHeight = minStemHeight;
            return this;
        }

        public FlatShelfFungus maxStemHeight(int maxStemHeight) {
            this.maxStemHeight = maxStemHeight;
            return this;
        }

        public FlatShelfFungus minStemLength(int minStemLength) {
            this.minStemLength = minStemLength;
            return this;
        }

        public FlatShelfFungus maxStemLength(int maxStemLength) {
            this.maxStemLength = maxStemLength;
            return this;
        }


        public FlatShelfFungusConfiguration build() {
            validate();
            return new FlatShelfFungusConfiguration(
                width,
                inwardCapLength,
                outwardCapLength,
                validWallTargets.build(),
                minStemHeight,
                maxStemHeight,
                minStemLength,
                maxStemLength
            );
        }

        private void validate() {
            if (validWallTargets.build().isEmpty()) {
                throw new IllegalStateException("FlatShelfFungus constructed without valid wall block!");
            }
            if (minStemHeight < 1) {
                throw new IllegalStateException("FlatShelfFungus must have minStemHeight a of at least 1!");
            }
            if (minStemLength < 2) {
                throw new IllegalStateException("FlatShelfFungus must have minStemLength of at least 2!");
            }
            if (minStemHeight > maxStemHeight) {
                throw new IllegalStateException("FlatShelfFungus minStemHeight must be greater or equal to maxStemHeight!");
            }
            if (minStemLength > maxStemLength) {
                throw new IllegalStateException("FlatShelfFungus minStemLength must be greater than or equal to maxStemLength!");
            }
        }
    }
}
