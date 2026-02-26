package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = HostileWorld.MODID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Client/Resourcepack Providers
        generator.addProvider(event.includeClient(), new ModBlockModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModBlockstateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModParticleDescriptionProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput));

        // Server/Datapack Providers
        generator.addProvider(event.includeServer(), new ModBiomeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModEntityTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModDataMapProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModDatapackProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new LootTableProvider(
            packOutput,
            Set.of(),
            List.of(
                new LootTableProvider.SubProviderEntry(
                    ModBlockLootSubProvider::new,
                    LootContextParamSets.BLOCK
                )
            ),
            lookupProvider
        ));
    }
}
