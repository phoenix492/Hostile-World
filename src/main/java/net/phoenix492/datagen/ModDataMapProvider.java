package net.phoenix492.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.phoenix492.data.EnvironmentalInfectionBuildupData;
import net.phoenix492.registration.ModDataMaps;
import net.phoenix492.util.TagKeys;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(ModDataMaps.INFECTION_BUILDUP)
            .add(TagKeys.Biomes.BUILDS_FUNGAL_INFECTION_SLOW, new EnvironmentalInfectionBuildupData().setBuildupQuantity(2), false)
            .add(TagKeys.Biomes.BUILDS_FUNGAL_INFECTION_STANDARD, new EnvironmentalInfectionBuildupData().setBuildupQuantity(5), false)
            .add(TagKeys.Biomes.BUILDS_FUNGAL_INFECTION_FAST, new EnvironmentalInfectionBuildupData().setBuildupQuantity(10), false)
            .add(Biomes.MUSHROOM_FIELDS, new EnvironmentalInfectionBuildupData().setBuildupQuantity(5), false);
    }
}
