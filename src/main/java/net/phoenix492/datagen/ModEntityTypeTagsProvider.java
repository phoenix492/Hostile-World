package net.phoenix492.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.util.TagKeys;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TagKeys.Entities.FUNGAL_INFECTION_IMMUNE)
            .add(EntityType.MOOSHROOM);
    }

    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, HostileWorld.MODID, existingFileHelper);
    }
}
