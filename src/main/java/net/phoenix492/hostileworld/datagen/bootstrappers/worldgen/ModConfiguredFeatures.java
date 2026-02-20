package net.phoenix492.hostileworld.datagen.bootstrappers.worldgen;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.registration.ModBlocks;
import net.phoenix492.hostileworld.util.ModTagKeys;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHROOM_CAVES_RED_MUSHROOMS_KEY = registerKey("shroom_caves_red_mushrooms");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHROOM_CAVES_BROWN_MUSHROOMS_KEY = registerKey("shroom_caves_brown_mushrooms");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHROOM_CAVES_BIG_RED_MUSHROOM_KEY =  registerKey("shroom_caves_big_red_mushroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHROOM_CAVES_BIG_BROWN_MUSHROOM_KEY =  registerKey("shroom_caves_big_brown_mushroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SHROOM_CAVES_GLOW_LICHEN_KEY = registerKey("shroom_caves_boosted_glow_lichen");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BROWN_MYCOSTONE_BLOB_KEY = registerKey("brown_mycostone_blob");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_MYCOSTONE_BLOB_KEY = registerKey("red_mycostone_blob");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MIXED_MYCOSTONE_BLOB_KEY = registerKey("mixed_mycostone_blob");

    public static final RuleTest MYCOSTONE_BLOB_REPLACEABLES = new TagMatchTest(ModTagKeys.Blocks.MYCOSTONE_BLOB_REPLACEABLES);

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        register(
            context,
            SHROOM_CAVES_RED_MUSHROOMS_KEY,
            Feature.RANDOM_PATCH,
            FeatureUtils.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.RED_MUSHROOM.defaultBlockState())),
                List.of(Blocks.MYCELIUM, ModBlocks.MYCOTURF.get()),
                6
            )
        );

        register(
            context,
            SHROOM_CAVES_BROWN_MUSHROOMS_KEY,
            Feature.RANDOM_PATCH,
            FeatureUtils.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM.defaultBlockState())),
                List.of(Blocks.MYCELIUM, ModBlocks.MYCOTURF.get()),
                6
            )
        );

        register(
          context,
          SHROOM_CAVES_BIG_RED_MUSHROOM_KEY,
          Feature.HUGE_RED_MUSHROOM,
        new HugeMushroomFeatureConfiguration(
            BlockStateProvider.simple(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)),
            BlockStateProvider.simple(
                Blocks.MUSHROOM_STEM
                    .defaultBlockState()
                    .setValue(HugeMushroomBlock.UP, false)
                    .setValue(HugeMushroomBlock.DOWN, false)
            ),
            2
        )
        );
        register(
            context,
            SHROOM_CAVES_BIG_BROWN_MUSHROOM_KEY,
            Feature.HUGE_BROWN_MUSHROOM,
            new HugeMushroomFeatureConfiguration(
                BlockStateProvider.simple(
                    Blocks.BROWN_MUSHROOM_BLOCK
                        .defaultBlockState()
                        .setValue(HugeMushroomBlock.UP, true)
                        .setValue(HugeMushroomBlock.DOWN, false)
                ),
                BlockStateProvider.simple(
                    Blocks.MUSHROOM_STEM
                        .defaultBlockState()
                        .setValue(HugeMushroomBlock.UP, false)
                        .setValue(HugeMushroomBlock.DOWN, false)
                ),
                3
            )
        );
        register(
            context,
            SHROOM_CAVES_GLOW_LICHEN_KEY,
            Feature.MULTIFACE_GROWTH,
            new MultifaceGrowthConfiguration(
                (MultifaceBlock) Blocks.GLOW_LICHEN,
                20,
                false,
                true,
                true,
                0.5f,
                HolderSet.direct(
                    Block::builtInRegistryHolder,
                    ModBlocks.BROWN_MYCOSTONE.get(),
                    ModBlocks.RED_MYCOSTONE.get(),
                    ModBlocks.MIXED_MYCOSTONE.get(),
                    Blocks.DIRT
                )
            )
        );
        register(
            context,
            RED_MYCOSTONE_BLOB_KEY,
            Feature.ORE,
            new OreConfiguration(
                MYCOSTONE_BLOB_REPLACEABLES,
                ModBlocks.RED_MYCOSTONE.get().defaultBlockState(),
                64,
                0f
            )
        );
        register(
            context,
            BROWN_MYCOSTONE_BLOB_KEY,
            Feature.ORE,
            new OreConfiguration(
                MYCOSTONE_BLOB_REPLACEABLES,
                ModBlocks.BROWN_MYCOSTONE.get().defaultBlockState(),
                64,
                0f
            )
        );
        register(
            context,
            MIXED_MYCOSTONE_BLOB_KEY,
            Feature.ORE,
            new OreConfiguration(
                MYCOSTONE_BLOB_REPLACEABLES,
                ModBlocks.MIXED_MYCOSTONE.get().defaultBlockState(),
                64,
                0f
            )
        );
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
    BootstrapContext<ConfiguredFeature<?, ?>> context,
    ResourceKey<ConfiguredFeature<?,?>> key,
    F feature,
    FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
