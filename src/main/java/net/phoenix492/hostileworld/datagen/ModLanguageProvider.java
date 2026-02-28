package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.registration.ModBlocks;
import net.phoenix492.hostileworld.registration.ModEffects;
import net.phoenix492.hostileworld.registration.ModItems;
import net.phoenix492.hostileworld.util.ModTagKeys;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, HostileWorld.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Effects
        addEffect(ModEffects.FUNGICIDE_EFFECT, "Fungicide");
        add("effect.hostileworld.fungicide.description", "Wracks your body to purge mushroom based infections from it. Painful.");
        addEffect(ModEffects.FUNGUS_INFECTION_EFFECT, "Mycorrhiza");
        add("effect.hostileworld.fungal_infection.description", "Something foreign to the body has taken root. Stacking penalties with potentially fatal consequences at high levels.");

        // Items
        addItem(ModItems.APOCALYPTIC_MUSHROOM_IGNITER, "Apocalyptic Mushroom Igniter");
        addItem(ModItems.STRONG_MUSHROOM_IGNITER, "Strong Mushroom Igniter");
        addItem(ModItems.NORMAL_MUSHROOM_IGNITER, "Normal Mushroom Igniter");
        addItem(ModItems.WEAK_MUSHROOM_IGNITER, "Weak Mushroom Igniter");
        addItem(ModItems.IMMUNITE_SHARD, "Immunite Shard");
        addItem(ModItems.IMMUNITE_CLUSTER, "Immunite Cluster");
        add("item.minecraft.potion.effect.fungicide", "Potion of Cure Mycorrhiza");
        add("item.minecraft.splash_potion.effect.fungicide", "Splash Potion of Cure Mycorrhiza");
        add("item.minecraft.lingering_potion.effect.fungicide", "Lingering Potion of Cure Mycorrhiza");
        add("item.minecraft.potion.effect.strong_fungicide", "Potion of Cure Mycorrhiza");
        add("item.minecraft.splash_potion.effect.strong_fungicide", "Splash Potion of Cure Mycorrhiza");
        add("item.minecraft.lingering_potion.effect.strong_fungicide", "Lingering Potion of Cure Mycorrhiza");

        // Block
        addBlock(ModBlocks.RED_MYCOSTONE, "Red Mycostone");
        addBlock(ModBlocks.BROWN_MYCOSTONE, "Brown Mycostone");
        addBlock(ModBlocks.MIXED_MYCOSTONE, "Mixed Mycostone");
        addBlock(ModBlocks.MYCORESTONE, "Mycorestone");
        addBlock(ModBlocks.MYCOTURF, "Mycoturf");
        addBlock(ModBlocks.MYCOFIRE, "Mycofire");
        addBlock(ModBlocks.NASCENT_AUTOIMMUNE_CLUSTER, "Nascent Autoimmune Cluster");
        addBlock(ModBlocks.MATURE_AUTOIMMUNE_CLUSTER, "Mature Autoimmune Cluster");
        addBlock(ModBlocks.MYENOKI_PATCH, "Myenoki Patch");

        // Creative Tabs
        add("creativetab.hostileworld.hostileworld_creative_tab", "Hostile World");

        // Tags
        addTag(() -> ModTagKeys.Blocks.BECOMES_NASCENT_AUTOIMMUNE_CLUSTER, "Becomes Nascent Autoimmune Cluster");
        addTag(() -> ModTagKeys.Blocks.BECOMES_MATURE_AUTOIMMUNE_CLUSTER, "Becomes Mature Autoimmune Cluster");

        addTag(() -> ModTagKeys.Blocks.BECOMES_FUNGUS, "Can Be Infected By Fungus");

        addTag(() -> ModTagKeys.Blocks.BECOMES_BROWN_FUNGUS, "Becomes Brown Fungus");
        addTag(() -> ModTagKeys.Blocks.BECOMES_BROWN_MUSHROOM, "Becomes Brown Mushroom");
        addTag(() -> ModTagKeys.Blocks.BECOMES_BROWN_MUSHROOM_BLOCK, "Becomes Brown Mushroom Block");
        addTag(() -> ModTagKeys.Blocks.BECOMES_BROWN_MYCOSTONE, "Becomes Brown Mycostone");

        addTag(() -> ModTagKeys.Blocks.BECOMES_RED_FUNGUS, "Becomes Red Fungus");
        addTag(() -> ModTagKeys.Blocks.BECOMES_RED_MUSHROOM, "Becomes Red Mushroom");
        addTag(() -> ModTagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK, "Becomes Red Mushroom Block");
        addTag(() -> ModTagKeys.Blocks.BECOMES_RED_MYCOSTONE, "Becomes Red Mycostone");

        addTag(() -> ModTagKeys.Blocks.BECOMES_MYCOSTONE, "Becomes Mycostone");
        addTag(() -> ModTagKeys.Blocks.BECOMES_MIXED_MYCOSTONE, "Becomes Mixed Mycostone");
        addTag(() -> ModTagKeys.Blocks.BECOMES_MUSHROOM_STEM, "Becomes Mushroom Stem");
        addTag(() -> ModTagKeys.Blocks.BECOMES_MYCELIUM, "Becomes Mycelium");
        addTag(() -> ModTagKeys.Blocks.BECOMES_MYCORESTONE, "Becomes Mycorestone");
        addTag(() -> ModTagKeys.Blocks.BECOMES_MYCOTURF, "Becomes Mycoturf");
        addTag(() -> ModTagKeys.Blocks.CONSUMED_BY_FUNGUS, "Consumed When Converted by Fungus");

        addTag(() -> ModTagKeys.Blocks.MYCOSTONE_BLOB_REPLACEABLES, "Mycostone Blob Replaceables");
        addTag(() -> ModTagKeys.Blocks.MATURE_AUTOIMMUNE_CLUSTER_REPLACEABLES, "Mature Autoimmune Cluster Replaceables");

        addTag(() -> ModTagKeys.Blocks.MYCOFIRE_BURNS_TO_DEEPSLATE, "Mycofire Burns to Deepslate");
        addTag(() -> ModTagKeys.Blocks.MYCOFIRE_BURNS_TO_DIRT, "Mycofire Burns to Dirt");
        addTag(() -> ModTagKeys.Blocks.MYCOFIRE_BURNS_TO_STONE, "Mycofire Burns to Stone");

        addTag(() -> ModTagKeys.Blocks.MYCOSTONES, "Mycostones");
        addTag(() -> ModTagKeys.Blocks.BROWN_FUNGUS, "Brown Fungus");
        addTag(() -> ModTagKeys.Blocks.RED_FUNGUS, "Red Fungus");

        addTag(() -> ModTagKeys.Blocks.DROPS_SPORES, "Drops Fungal Spores");
        addTag(() -> ModTagKeys.Blocks.SPREADS_FUNGUS, "Spreads Fungus");

        addTag(() -> ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_FAST, "Builds Fungal Infection (Fast)");
        addTag(() -> ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_STANDARD, "Builds Fungal Infection (Standard)");
        addTag(() -> ModTagKeys.Biomes.BUILDS_FUNGAL_INFECTION_SLOW, "Builds Fungal Infection (Slow)");
        addTag(() -> ModTagKeys.Biomes.MYCOFIRE_RESISTANT, "Mycofire Resistant");

        addTag(() -> ModTagKeys.Entities.FUNGAL_INFECTION_IMMUNE, "Immune to Fungal Infection");
    }
}
