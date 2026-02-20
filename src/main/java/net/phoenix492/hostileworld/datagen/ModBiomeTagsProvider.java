package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.util.ModTagKeys;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

public class ModBiomeTagsProvider extends BiomeTagsProvider {

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_SLOW)
            .addOptional(ResourceLocation.fromNamespaceAndPath("minecraft", "dark_forest"));
        tag(ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_STANDARD)
            .addOptional(ResourceLocation.fromNamespaceAndPath("minecraft", "mushroom_fields"));
    }

    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HostileWorld.MODID, existingFileHelper);
    }
}
