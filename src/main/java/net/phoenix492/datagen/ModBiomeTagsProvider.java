package net.phoenix492.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.util.TagKeys;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TagKeys.Biomes.FUNGAL_INFECTION)
            .add(Biomes.MUSHROOM_FIELDS);
    }

    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HostileWorld.MODID, existingFileHelper);
    }
}
