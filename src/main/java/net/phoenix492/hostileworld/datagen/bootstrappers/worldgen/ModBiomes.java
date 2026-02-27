package net.phoenix492.hostileworld.datagen.bootstrappers.worldgen;

import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeSpecialEffectsBuilder;

public class ModBiomes {

    public static final ResourceKey<Biome> FUNGAL_CAVERNS_KEY = registerKey("fungal_caverns");

    public static ResourceKey<Biome> registerKey(String name) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, name));
    }

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        register(
            context,
            FUNGAL_CAVERNS_KEY,
            new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(2.0f)
                .downfall(1.0f)
                .specialEffects(
                    BiomeSpecialEffectsBuilder
                        .create(12638463, 4159204, 329011, 7842047)
                    .build()
                )
                .generationSettings(
                    new BiomeGenerationSettings.PlainBuilder()
                        .addFeature(
                            GenerationStep.Decoration.UNDERGROUND_ORES,
                            placedFeatures.getOrThrow(ModPlacedFeatures.FUNGAL_CAVERNS_MATURE_AUTOIMMUNE_CLUSTER_PLACED_KEY)
                        )
                        .addFeature(
                            GenerationStep.Decoration.UNDERGROUND_DECORATION,
                            placedFeatures.getOrThrow(ModPlacedFeatures.FUNGAL_CAVERNS_RED_MYCOSTONE_BLOB_PLACED_KEY)
                        )
                        .addFeature(
                            GenerationStep.Decoration.UNDERGROUND_DECORATION,
                            placedFeatures.getOrThrow(ModPlacedFeatures.FUNGAL_CAVERNS_BROWN_MYCOSTONE_BLOB_PLACED_KEY)
                        )
                        .addFeature(
                            GenerationStep.Decoration.VEGETAL_DECORATION,
                            placedFeatures.getOrThrow(ModPlacedFeatures.FUNGAL_CAVERNS_RED_MUSHROOM_PLACED_KEY)
                        )
                        .addFeature(
                            GenerationStep.Decoration.VEGETAL_DECORATION,
                            placedFeatures.getOrThrow(ModPlacedFeatures.FUNGAL_CAVERNS_BROWN_MUSHROOM_PLACED_KEY)
                        )
                        .addFeature(
                            GenerationStep.Decoration.VEGETAL_DECORATION,
                            placedFeatures.getOrThrow(ModPlacedFeatures.FUNGAL_CAVERNS_BIG_RED_MUSHROOM_PLACED_KEY)
                        )
                        .addFeature(
                            GenerationStep.Decoration.VEGETAL_DECORATION,
                            placedFeatures.getOrThrow(ModPlacedFeatures.FUNGAL_CAVERNS_BIG_BROWN_MUSHROOM_PLACED_KEY)
                        )
                        .addFeature(
                            GenerationStep.Decoration.VEGETAL_DECORATION,
                            placedFeatures.getOrThrow(ModPlacedFeatures.FUNGAL_CAVERNS_BOOSTED_GLOW_LICHEN_PLACED_KEY)
                        )
                        .addFeature(
                            GenerationStep.Decoration.VEGETAL_DECORATION,
                            placedFeatures.getOrThrow(ModPlacedFeatures.FUNGAL_CAVERNS_BROWN_SHELF_FUNGUS_PLACED_KEY)
                        )
                    .build()
                )
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
            .build()
        );

    }

    private static void register(BootstrapContext<Biome> context, ResourceKey<Biome> biomeKey, Biome biome) {
        context.register(biomeKey, biome);
    }
}
