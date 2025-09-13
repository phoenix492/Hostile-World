package net.phoenix492.datagen.bootstrappers.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.phoenix492.hostileworld.HostileWorld;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> SHROOM_CAVES_RED_MUSHROOM_PLACED_KEY = registerKey("shroom_caves_red_mushroom_placed");
    public static final ResourceKey<PlacedFeature> SHROOM_CAVES_BROWN_MUSHROOM_PLACED_KEY = registerKey("shroom_caves_brown_mushroom_placed");
    public static final ResourceKey<PlacedFeature> SHROOM_CAVES_BIG_RED_MUSHROOM_PLACED_KEY = registerKey("shroom_caves_big_red_mushroom_placed");
    public static final ResourceKey<PlacedFeature> SHROOM_CAVES_BIG_BROWN_MUSHROOM_PLACED_KEY = registerKey("shroom_caves_big_brown_mushroom_placed");
    public static final ResourceKey<PlacedFeature> SHROOM_CAVES_BOOSTED_GLOW_LICHEN_PLACED_KEY = registerKey("shroom_caves_boosted_glow_lichen_placed");
    public static final ResourceKey<PlacedFeature> SHROOM_CAVES_BROWN_MYCOSTONE_BLOB_PLACED_KEY = registerKey("shroom_caves_brown_mycostone_blob_placed");
    public static final ResourceKey<PlacedFeature> SHROOM_CAVES_RED_MYCOSTONE_BLOB_PLACED_KEY = registerKey("shroom_caves_red_mycostone_blob_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(
            context,
            SHROOM_CAVES_RED_MUSHROOM_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.SHROOM_CAVES_RED_MUSHROOMS_KEY),
            List.of(
                CountOnEveryLayerPlacement.of(4),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            SHROOM_CAVES_BROWN_MUSHROOM_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.SHROOM_CAVES_BROWN_MUSHROOMS_KEY),
            List.of(
                CountOnEveryLayerPlacement.of(5),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            SHROOM_CAVES_BIG_RED_MUSHROOM_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.SHROOM_CAVES_BIG_RED_MUSHROOM_KEY),
            List.of(
                CountOnEveryLayerPlacement.of(1),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            SHROOM_CAVES_BIG_BROWN_MUSHROOM_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.SHROOM_CAVES_BIG_BROWN_MUSHROOM_KEY),
            List.of(
                CountOnEveryLayerPlacement.of(1),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            SHROOM_CAVES_BOOSTED_GLOW_LICHEN_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.SHROOM_CAVES_GLOW_LICHEN_KEY),
            List.of(
                CountPlacement.of(UniformInt.of(128, 256)),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(63))),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            SHROOM_CAVES_RED_MYCOSTONE_BLOB_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.SHROOM_CAVES_RED_MYCOSTONE_BLOB_KEY),
            List.of(
                CountPlacement.of(UniformInt.of(3, 5)),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(-30), VerticalAnchor.absolute(60))),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
            )
        );
        register(
            context,
            SHROOM_CAVES_BROWN_MYCOSTONE_BLOB_PLACED_KEY,
            configuredFeatures.getOrThrow(ModConfiguredFeatures.SHROOM_CAVES_BROWN_MYCOSTONE_BLOB_KEY),
            List.of(
                CountPlacement.of(UniformInt.of(3, 5)),
                HeightRangePlacement.of(UniformHeight.of(VerticalAnchor.absolute(-30), VerticalAnchor.absolute(60))),
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
