package net.phoenix492.hostileworld.worldgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

public record FloorSnappingVegetationConfiguration(BlockStateProvider block, HolderSet<Block> validFloorTargets) implements FeatureConfiguration {
    public static Codec<FloorSnappingVegetationConfiguration> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            BlockStateProvider.CODEC.fieldOf("block").forGetter(FloorSnappingVegetationConfiguration::block),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("validFloorTargets").forGetter(FloorSnappingVegetationConfiguration::validFloorTargets)
        ).apply(instance, FloorSnappingVegetationConfiguration::new)
    );

    public static FloorSnappingVegetationConfigurationBuilder builder() {
        return new FloorSnappingVegetationConfigurationBuilder();
    }

    public static class FloorSnappingVegetationConfigurationBuilder {
        private HolderSet<Block> validFloorTargets;
        private final SimpleWeightedRandomList.Builder<BlockState> states = SimpleWeightedRandomList.builder();

        public FloorSnappingVegetationConfigurationBuilder() {}

        public FloorSnappingVegetationConfigurationBuilder validFloorTargets(HolderSet<Block> targets) {
            this.validFloorTargets = targets;
            return this;
        }

        public FloorSnappingVegetationConfigurationBuilder addPlacedState(BlockState state) {
            this.states.add(state);
            return this;
        }

        public FloorSnappingVegetationConfigurationBuilder addPlacedState(BlockState state, int weight) {
            this.states.add(state, weight);
            return this;
        }

        public FloorSnappingVegetationConfiguration build() {
            validate();
            return new FloorSnappingVegetationConfiguration(
                new WeightedStateProvider(states),
                validFloorTargets
            );
        }

        private void validate() {
            if (validFloorTargets == null) {
                throw new IllegalStateException("FloorSnappingVegetation must have valid target block set!");
            }

            if (states.build().isEmpty()) {
                throw new IllegalStateException("FloorSnappingVegetation must have valid block to place!");
            }
        }
    }
}
