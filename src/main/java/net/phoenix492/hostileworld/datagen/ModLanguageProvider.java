package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.registration.ModBlocks;
import net.phoenix492.hostileworld.registration.ModEffects;
import net.phoenix492.hostileworld.registration.ModItems;

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

        // Creative Tabs
        add("creativetab.hostileworld.hostileworld_creative_tab", "Hostile World");

    }
}
