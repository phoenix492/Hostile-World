package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.util.ModTagKeys;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

public class ModBiomeTagsProvider extends BiomeTagsProvider {

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        // Mod Tags
        tag(ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_SLOW)
            .addOptional(ResourceLocation.fromNamespaceAndPath("minecraft", "dark_forest"));
        tag(ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_STANDARD)
            .addOptional(ResourceLocation.fromNamespaceAndPath("minecraft", "mushroom_fields"));
        tag(ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_FAST)
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_caves"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_core"));

        // C Tags
        tag(Tags.Biomes.IS_HOT)
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_caves"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_core"));
        tag(Tags.Biomes.IS_HOT_OVERWORLD)
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_caves"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_core"));
        tag(Tags.Biomes.IS_MUSHROOM)
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_caves"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_core"));
        tag(Tags.Biomes.IS_UNDERGROUND)
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_caves"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_core"));
        tag(Tags.Biomes.IS_CAVE)
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_caves"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_core"));
        tag(Tags.Biomes.IS_RARE)
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_caves"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_core"));

        // MC Tags
        tag(BiomeTags.IS_OVERWORLD)
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_caves"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "shroom_core"));
    }

    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HostileWorld.MODID, existingFileHelper);
    }
}
