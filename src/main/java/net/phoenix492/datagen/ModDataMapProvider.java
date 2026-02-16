package net.phoenix492.datagen;

import net.phoenix492.data.map.BiomeFungalSpreadData;
import net.phoenix492.data.map.BlockInfectionBuildupData;
import net.phoenix492.data.map.EnvironmentalInfectionBuildupData;
import net.phoenix492.data.map.FungalTransformationData;
import net.phoenix492.registration.ModBlocks;
import net.phoenix492.registration.ModDataMaps;
import net.phoenix492.util.ModTagKeys;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {

        this.builder(ModDataMaps.ENVIRONMENTAL_INFECTION_BUILDUP)
            .add(ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_SLOW, new EnvironmentalInfectionBuildupData(2), false)
            .add(ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_STANDARD, new EnvironmentalInfectionBuildupData(5), false)
            .add(ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_FAST, new EnvironmentalInfectionBuildupData(10), false)
            .add(ModTagKeys.Biomes.IS_MAGICAL, new EnvironmentalInfectionBuildupData(-1), false);

        this.builder(ModDataMaps.BIOME_FUNGAL_SPREAD)
            .add(Biomes.DARK_FOREST, new BiomeFungalSpreadData(0.001f, 0), false)
            .add(Biomes.MUSHROOM_FIELDS, new BiomeFungalSpreadData(1f, 0.0001f), false)
            .add(ModTagKeys.Biomes.IS_MAGICAL, new BiomeFungalSpreadData(0, 0), false);

        this.builder(ModDataMaps.BLOCK_INFECTION_BUILDUP)
            .add(ModTagKeys.Blocks.SPREADS_FUNGUS, new BlockInfectionBuildupData(true, 50, true, 10), false);

        this.builder(ModDataMaps.FUNGAL_SPREAD_TRANSFORM)
            .add(ModTagKeys.Blocks.BECOMES_MYCELIUM, FungalTransformationData.builder().addTarget(Blocks.MYCELIUM).propagationCheck().build(), false)
            .add(ModTagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK, FungalTransformationData.builder().addTarget(Blocks.RED_MUSHROOM_BLOCK).build(), false)
            .add(ModTagKeys.Blocks.BECOMES_BROWN_MUSHROOM_BLOCK, FungalTransformationData.builder().addTarget(Blocks.BROWN_MUSHROOM_BLOCK).build(), false)
            .add(ModTagKeys.Blocks.BECOMES_MUSHROOM_STEM, FungalTransformationData.builder().addTarget(Blocks.MUSHROOM_STEM).build(), false)
            .add(ModTagKeys.Blocks.BECOMES_RED_MUSHROOM, FungalTransformationData.builder().addTarget(Blocks.RED_MUSHROOM).consumeChance(0.25f).myceliumCheck().build(), false)
            .add(ModTagKeys.Blocks.BECOMES_BROWN_MUSHROOM, FungalTransformationData.builder().addTarget(Blocks.BROWN_MUSHROOM).consumeChance(0.75f).myceliumCheck().build(), false)
            .add(ModTagKeys.Blocks.BECOMES_MYCOSTONE, FungalTransformationData.builder().addTarget(ModBlocks.RED_MYCOSTONE.get()).mycostoneVariantCheck().airExposureCheck().build(), false)
            .add(ModTagKeys.Blocks.BECOMES_MYCOTURF, FungalTransformationData.builder().addTarget(ModBlocks.MYCOTURF.get()).build(), false)
            .add(ResourceLocation.parse("minecraft:andesite"), FungalTransformationData.builder().addTarget(ModBlocks.MIXED_MYCOSTONE.get()).airExposureCheck().build(), false)
            .add(ResourceLocation.parse("minecraft:granite"), FungalTransformationData.builder().addTarget(ModBlocks.RED_MYCOSTONE.get()).airExposureCheck().build(), false)
            .add(ResourceLocation.parse("minecraft:diorite"), FungalTransformationData.builder().addTarget(ModBlocks.BROWN_MYCOSTONE.get()).airExposureCheck().build(), false)
            .add(ResourceLocation.parse("minecraft:tuff"), FungalTransformationData.builder().addTarget(ModBlocks.BROWN_MYCOSTONE.get()).airExposureCheck().build(), false)
            .add(ResourceLocation.parse("minecraft:deepslate"), FungalTransformationData.builder().addTarget(ModBlocks.MYCORESTONE.get()).airExposureCheck().spreadAlikeCheck().failChance(.50f).build(), false)
            .add(ModTagKeys.Blocks.CONSUMED_BY_FUNGUS, FungalTransformationData.builder().addTarget(Blocks.AIR).build(), false);

    }
}
