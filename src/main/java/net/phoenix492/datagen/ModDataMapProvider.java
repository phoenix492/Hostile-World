package net.phoenix492.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.phoenix492.data.BiomeFungalSpreadData;
import net.phoenix492.data.BlockInfectionBuildupData;
import net.phoenix492.data.EnvironmentalInfectionBuildupData;
import net.phoenix492.data.FungalTransformationData;
import net.phoenix492.registration.ModBlocks;
import net.phoenix492.registration.ModDataMaps;
import net.phoenix492.util.TagKeys;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(ModDataMaps.ENVIRONMENTAL_INFECTION_BUILDUP)
            .add(TagKeys.Biomes.BUILDS_FUNGAL_INFECTION_SLOW, new EnvironmentalInfectionBuildupData(2), false)
            .add(TagKeys.Biomes.BUILDS_FUNGAL_INFECTION_STANDARD, new EnvironmentalInfectionBuildupData(5), false)
            .add(TagKeys.Biomes.BUILDS_FUNGAL_INFECTION_FAST, new EnvironmentalInfectionBuildupData(10), false)
            .add(TagKeys.Biomes.IS_MAGICAL, new EnvironmentalInfectionBuildupData(-1), false);

        this.builder(ModDataMaps.BIOME_FUNGAL_SPREAD)
            .add(Biomes.DARK_FOREST, new BiomeFungalSpreadData(0.001f, 0), false)
            .add(Biomes.MUSHROOM_FIELDS, new BiomeFungalSpreadData(1f, 0.0001f), false)
            .add(TagKeys.Biomes.IS_MAGICAL, new BiomeFungalSpreadData(0, 0), false);

        this.builder(ModDataMaps.BLOCK_INFECTION_BUILDUP)
            .add(TagKeys.Blocks.SPREADS_FUNGUS, new BlockInfectionBuildupData(true, 50, true, 10), false);

        this.builder(ModDataMaps.FUNGAL_SPREAD_TRANSFORM)
            .add(TagKeys.Blocks.BECOMES_MYCELIUM, new FungalTransformationData(Blocks.MYCELIUM, 0, 0, false, true, false), false)
            .add(TagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK, new FungalTransformationData(Blocks.RED_MUSHROOM_BLOCK), false)
            .add(TagKeys.Blocks.BECOMES_BROWN_MUSHROOM_BLOCK, new FungalTransformationData(Blocks.BROWN_MUSHROOM_BLOCK), false)
            .add(TagKeys.Blocks.BECOMES_MUSHROOM_STEM, new FungalTransformationData(Blocks.MUSHROOM_STEM), false)
            .add(TagKeys.Blocks.BECOMES_RED_MUSHROOM, new FungalTransformationData(Blocks.RED_MUSHROOM, 0, 0.25f, true, false, false), false)
            .add(TagKeys.Blocks.BECOMES_BROWN_MUSHROOM, new FungalTransformationData(Blocks.BROWN_MUSHROOM, 0, 0.75f, true, false, false), false)
            .add(TagKeys.Blocks.BECOMES_RED_MYCOSTONE, new FungalTransformationData(ModBlocks.RED_MYCOSTONE.get(), 0, 0, false, false, true), false)
            .add(TagKeys.Blocks.BECOMES_BROWN_MYCOSTONE, new FungalTransformationData(ModBlocks.BROWN_MYCOSTONE.get(), 0, 0, false, false, true), false)
            .add(TagKeys.Blocks.BECOMES_MIXED_MYCOSTONE, new FungalTransformationData(ModBlocks.MIXED_MYCOSTONE.get(), 0, 0, false, false, true), false)
            .add(TagKeys.Blocks.CONSUMED_BY_FUNGUS, new FungalTransformationData(Blocks.AIR), false);

    }
}
