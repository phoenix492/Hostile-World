package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.registration.ModBlocks;
import net.phoenix492.hostileworld.registration.ModItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Set;

public class ModBlockLootSubProvider extends BlockLootSubProvider {

    protected ModBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        // The contents of our DeferredRegister.
        return ModBlocks.MOD_BLOCKS.getEntries()
            .stream()
            // Cast to Block here, otherwise it will be a ? extends Block and Java will complain.
            .map(e -> (Block) e.value())
            .toList();
    }

    @Override
    protected void generate() {
        add(ModBlocks.NASCENT_AUTOIMMUNE_CLUSTER.get(),
            createSilkTouchDispatchTable(
                ModBlocks.NASCENT_AUTOIMMUNE_CLUSTER.get(),
                LootItem.lootTableItem(ModItems.IMMUNITE_SHARD)
            )
        );
        add(ModBlocks.MATURE_AUTOIMMUNE_CLUSTER.get(),
            createSilkTouchDispatchTable(
                ModBlocks.MATURE_AUTOIMMUNE_CLUSTER.get(),
                LootItem.lootTableItem(ModItems.IMMUNITE_CLUSTER)
            )
        );
        add(ModBlocks.MYCORRHIZAL_GROWTH.get(),
            createShearsOnlyDrop(ModBlocks.MYCORRHIZAL_GROWTH.asItem())
        );
        add(ModBlocks.BLOOMING_MYCORRHIZAL_GROWTH.get(),
                LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                        .add(AlternativesEntry.alternatives(
                            LootItem.lootTableItem(ModBlocks.BLOOMING_MYCORRHIZAL_GROWTH.asItem()).when(HAS_SHEARS),
                            NestedLootTable.inlineLootTable(
                                LootTable.lootTable()
                                    .withPool(LootPool.lootPool()
                                        .add(LootItem.lootTableItem(Items.RED_MUSHROOM)).apply(ApplyBonusCount.addBonusBinomialDistributionCount(registries.holderOrThrow(Enchantments.FORTUNE), 0.5f, 0))
                                        .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM)).apply(ApplyBonusCount.addBonusBinomialDistributionCount(registries.holderOrThrow(Enchantments.FORTUNE), 0.5f, 0))
                                        .setRolls(ConstantValue.exactly(1))
                                    )

                                .build()
                            )
                        ))
                    )
        );
        dropSelf(ModBlocks.RED_MYCOSTONE.get());
        dropSelf(ModBlocks.BROWN_MYCOSTONE.get());
        dropSelf(ModBlocks.MIXED_MYCOSTONE.get());
        dropSelf(ModBlocks.MYCOTURF.get());
        dropSelf(ModBlocks.MYCORESTONE.get());
    }
}
