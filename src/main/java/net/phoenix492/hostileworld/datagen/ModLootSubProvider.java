package net.phoenix492.hostileworld.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

public class ModLootSubProvider implements LootTableSubProvider {
    HolderLookup.Provider lookupProvider;
    protected ModLootSubProvider(HolderLookup.Provider lookupProvider) {
        this.lookupProvider = lookupProvider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {

    }
}
