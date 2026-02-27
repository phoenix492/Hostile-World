package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.datagen.bootstrappers.worldgen.ModBiomes;
import net.phoenix492.hostileworld.datagen.bootstrappers.worldgen.ModConfiguredFeatures;
import net.phoenix492.hostileworld.datagen.bootstrappers.worldgen.ModPlacedFeatures;
import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
        .add(Registries.BIOME, ModBiomes::bootstrap);

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(HostileWorld.MODID));
    }
}
