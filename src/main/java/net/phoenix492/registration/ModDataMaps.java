package net.phoenix492.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.phoenix492.data.BiomeFungalSpreadData;
import net.phoenix492.data.BlockInfectionBuildupData;
import net.phoenix492.data.EnvironmentalInfectionBuildupData;
import net.phoenix492.data.FungalTransformationData;
import net.phoenix492.hostileworld.HostileWorld;

@EventBusSubscriber
public class ModDataMaps {

    public static final DataMapType<Biome, EnvironmentalInfectionBuildupData> ENVIRONMENTAL_INFECTION_BUILDUP = DataMapType.builder(
        ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "environmental_infection_buildup"),
        Registries.BIOME,
        EnvironmentalInfectionBuildupData.CODEC
    ).build();

    public static final DataMapType<Biome, BiomeFungalSpreadData> BIOME_FUNGAL_SPREAD = DataMapType.builder(
        ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "biome_fungal_spread"),
        Registries.BIOME,
        BiomeFungalSpreadData.CODEC
    ).build();

    public static final DataMapType<Block, BlockInfectionBuildupData> BLOCK_INFECTION_BUILDUP = DataMapType.builder(
        ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "block_infection_buildup"),
        Registries.BLOCK,
        BlockInfectionBuildupData.CODEC
    ).build();

    public static final DataMapType<Block, FungalTransformationData> FUNGAL_SPREAD_TRANSFORM = DataMapType.builder(
        ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "fungal_spread_transform"),
        Registries.BLOCK,
        FungalTransformationData.CODEC
    ).build();



    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(ENVIRONMENTAL_INFECTION_BUILDUP);
        event.register(BIOME_FUNGAL_SPREAD);
        event.register(FUNGAL_SPREAD_TRANSFORM);
        event.register(BLOCK_INFECTION_BUILDUP);
    }
}
