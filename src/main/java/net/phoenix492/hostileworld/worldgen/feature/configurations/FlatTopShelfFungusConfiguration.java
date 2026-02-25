package net.phoenix492.hostileworld.worldgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import com.google.common.collect.ImmutableList;

public record FlatTopShelfFungusConfiguration(
    int width,
    int inwardCapLength,
    int outwardCapLength,
    int minStemHeight,
    int maxStemHeight,
    int minStemLength,
    int maxStemLength,
    int rimDepth,
    boolean generateRim,
    boolean generateRimCorners,
    Block capBlock,
    Block stemBlock,
    List<Block> validWallTargets
    ) implements FeatureConfiguration {
    public static Codec<FlatTopShelfFungusConfiguration> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.optionalFieldOf("capWidth", 2).forGetter(FlatTopShelfFungusConfiguration::width),
            Codec.INT.optionalFieldOf("inwardCapLength", 2).forGetter(FlatTopShelfFungusConfiguration::inwardCapLength),
            Codec.INT.optionalFieldOf("outwardCapLength", 1).forGetter(FlatTopShelfFungusConfiguration::outwardCapLength),
            Codec.INT.optionalFieldOf("minStemHeight", 2).forGetter(FlatTopShelfFungusConfiguration::minStemHeight),
            Codec.INT.optionalFieldOf("maxStemHeight", 3).forGetter(FlatTopShelfFungusConfiguration::maxStemHeight),
            Codec.INT.optionalFieldOf("minStemLength", 2).forGetter(FlatTopShelfFungusConfiguration::minStemLength),
            Codec.INT.optionalFieldOf("maxStemLength", 3).forGetter(FlatTopShelfFungusConfiguration::maxStemLength),
            Codec.INT.optionalFieldOf("rimDepth", 1).forGetter(FlatTopShelfFungusConfiguration::rimDepth),
            Codec.BOOL.optionalFieldOf("generateRim", false).forGetter(FlatTopShelfFungusConfiguration::generateRim),
            Codec.BOOL.optionalFieldOf("generateRimCorners", false).forGetter(FlatTopShelfFungusConfiguration::generateRimCorners),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("capBlock").forGetter(FlatTopShelfFungusConfiguration::capBlock),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("stemBlock").forGetter(FlatTopShelfFungusConfiguration::stemBlock),
            Codec.list(BuiltInRegistries.BLOCK.byNameCodec()).fieldOf("validWallTargets").forGetter(FlatTopShelfFungusConfiguration::validWallTargets)
            ).apply(instance, FlatTopShelfFungusConfiguration::new)
    );

    public static FlatTopShelfFungusConfigurationBuilder builder() {
        return new FlatTopShelfFungusConfigurationBuilder();
    }

    public static class FlatTopShelfFungusConfigurationBuilder {
        private int width = 2;
        private int inwardCapLength = 2;
        private int outwardCapLength = 1;
        private int minStemHeight = 2;
        private int maxStemHeight = 3;
        private int minStemLength = 2;
        private int maxStemLength = 3;
        private boolean generateRim = false;
        private boolean generateRimCorners = false;
        private int rimDepth = 1;
        private Block capBlock = null;
        private Block stemBlock = null;
        private final ImmutableList.Builder<Block> validWallTargets = ImmutableList.builder();

        public FlatTopShelfFungusConfigurationBuilder width(int width) {
            this.width = width;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder inwardLength(int inwardLength) {
            this.inwardCapLength = inwardLength;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder outwardCapLength(int outwardCapLength) {
            this.outwardCapLength = outwardCapLength;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder addValidWallTarget(Block block) {
            validWallTargets.add(block);
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder minStemHeight(int minStemHeight) {
            this.minStemHeight = minStemHeight;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder maxStemHeight(int maxStemHeight) {
            this.maxStemHeight = maxStemHeight;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder minStemLength(int minStemLength) {
            this.minStemLength = minStemLength;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder maxStemLength(int maxStemLength) {
            this.maxStemLength = maxStemLength;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder generateRim() {
            this.generateRim = true;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder generateRimCorners() {
            this.generateRimCorners = true;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder capBlock(Block block) {
            this.capBlock = block;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder stemBlock(Block block) {
            this.stemBlock = block;
            return this;
        }

        public FlatTopShelfFungusConfigurationBuilder rimDepth(int depth) {
            this.rimDepth = depth;
            return this;
        }


        public FlatTopShelfFungusConfiguration build() {
            validate();
            return new FlatTopShelfFungusConfiguration(
                width,
                inwardCapLength,
                outwardCapLength,
                minStemHeight,
                maxStemHeight,
                minStemLength,
                maxStemLength,
                rimDepth,
                generateRim,
                generateRimCorners,
                capBlock,
                stemBlock,
                validWallTargets.build()
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
            if (capBlock == null) {
                throw new IllegalStateException("FlatShelfFungus must have valid capBlock set!");
            }
            if (stemBlock == null) {
                throw new IllegalStateException("FlatShelfFungus must have valid stemBlock set!");
            }
            if (generateRim && rimDepth < 1) {
                throw new IllegalStateException("FlatShelfFungus must have rimDepth >= 1 if generateRim enabled!");
            }

            if (rimDepth > minStemHeight) {
                throw new IllegalStateException("FlatShelfFungus rimDepth greater than minStemHeight! Rim would intersect stem under some valid configurations.");
            }
        }
    }
}
