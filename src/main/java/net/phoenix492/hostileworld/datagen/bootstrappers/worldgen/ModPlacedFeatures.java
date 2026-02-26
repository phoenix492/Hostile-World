package net.phoenix492.hostileworld.datagen.bootstrappers.worldgen;

import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> FUNGAL_CAVERNS_RED_MUSHROOM_PLACED_KEY = registerKey("fungal_caverns_red_mushroom_placed");
    public static final ResourceKey<PlacedFeature> FUNGAL_CAVERNS_BROWN_MUSHROOM_PLACED_KEY = registerKey("fungal_caverns_brown_mushroom_placed");
    public static final ResourceKey<PlacedFeature> FUNGAL_CAVERNS_BIG_RED_MUSHROOM_PLACED_KEY = registerKey("fungal_caverns_big_red_mushroom_placed");
    public static final ResourceKey<PlacedFeature> FUNGAL_CAVERNS_BIG_BROWN_MUSHROOM_PLACED_KEY = registerKey("fungal_caverns_big_brown_mushroom_placed");
    public static final ResourceKey<PlacedFeature> FUNGAL_CAVERNS_BOOSTED_GLOW_LICHEN_PLACED_KEY = registerKey("fungal_caverns_boosted_glow_lichen_placed");
    public static final ResourceKey<PlacedFeature> FUNGAL_CAVERNS_BROWN_MYCOSTONE_BLOB_PLACED_KEY = registerKey("fungal_caverns_brown_mycostone_blob_placed");
    public static final ResourceKey<PlacedFeature> FUNGAL_CAVERNS_RED_MYCOSTONE_BLOB_PLACED_KEY = registerKey("fungal_caverns_red_mycostone_blob_placed");
    public static final ResourceKey<PlacedFeature> FUNGAL_CAVERNS_MATURE_AUTOIMMUNE_CLUSTER_PLACED_KEY = registerKey("fungal_caverns_mature_autoimmune_cluster_placed");
    public static final ResourceKey<PlacedFeature> FUNGAL_CAVERNS_BROWN_SHELF_FUNGUS_PLACED_KEY = registerKey("fungal_caverns_brown_shelf_fungus_placed");


    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(
            context,
            FUNGAL_CAVERNS_RED_MUSHROOM_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.FUNGAL_CAVERNS_RED_MUSHROOMS_KEY),
            List.of(
                CountOnEveryLayerPlacement.of(4),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            FUNGAL_CAVERNS_BROWN_MUSHROOM_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.FUNGAL_CAVERNS_BROWN_MUSHROOMS_KEY),
            List.of(
                CountOnEveryLayerPlacement.of(5),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            FUNGAL_CAVERNS_BIG_RED_MUSHROOM_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.FUNGAL_CAVERNS_BIG_RED_MUSHROOM_KEY),
            List.of(
                CountOnEveryLayerPlacement.of(1),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            FUNGAL_CAVERNS_BIG_BROWN_MUSHROOM_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.FUNGAL_CAVERNS_BIG_BROWN_MUSHROOM_KEY),
            List.of(
                CountOnEveryLayerPlacement.of(1),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            FUNGAL_CAVERNS_BOOSTED_GLOW_LICHEN_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.FUNGAL_CAVERNS_GLOW_LICHEN_KEY),
            List.of(
                CountPlacement.of(UniformInt.of(128, 256)),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(63))),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            FUNGAL_CAVERNS_RED_MYCOSTONE_BLOB_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.RED_MYCOSTONE_BLOB_KEY),
            List.of(
                CountPlacement.of(UniformInt.of(3, 5)),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(-30), VerticalAnchor.absolute(60))),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            FUNGAL_CAVERNS_BROWN_MYCOSTONE_BLOB_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.BROWN_MYCOSTONE_BLOB_KEY),
            List.of(
                CountPlacement.of(UniformInt.of(3, 5)),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(-30), VerticalAnchor.absolute(60))),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        );
        
        register(
            context,
            FUNGAL_CAVERNS_MATURE_AUTOIMMUNE_CLUSTER_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.MATURE_AUTOIMMUNE_CLUSTER_ORE_KEY),
            List.of(
                CountPlacement.of(15),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(-30), VerticalAnchor.absolute(60))),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        );

        register(
            context,
            FUNGAL_CAVERNS_BROWN_SHELF_FUNGUS_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.BROWN_SHELF_FUNGUS_KEY),
            List.of(
                CountPlacement.of(10),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.BOTTOM, VerticalAnchor.absolute(60))),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        );
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, name));
    }

    private static void register(
    BootstrapContext<PlacedFeature> context,
    ResourceKey<PlacedFeature> key,
    Holder<ConfiguredFeature<?, ?>> configuration,
    List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
