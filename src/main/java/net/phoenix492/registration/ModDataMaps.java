package net.phoenix492.registration;

import net.phoenix492.data.map.BiomeFungalSpreadData;
import net.phoenix492.data.map.BlockInfectionBuildupData;
import net.phoenix492.data.map.EnvironmentalInfectionBuildupData;
import net.phoenix492.data.map.FungalTransformationData;
import net.phoenix492.data.map.MycofireFlammabilityData;
import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

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

    public static final DataMapType<Block, MycofireFlammabilityData> MYCOFIRE_FLAMMABILITY_DATA = DataMapType.builder(
        ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "mycofire_flammability"),
        Registries.BLOCK,
        MycofireFlammabilityData.CODEC
    ).build();


    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(ENVIRONMENTAL_INFECTION_BUILDUP);
        event.register(BIOME_FUNGAL_SPREAD);
        event.register(FUNGAL_SPREAD_TRANSFORM);
        event.register(BLOCK_INFECTION_BUILDUP);
        event.register(MYCOFIRE_FLAMMABILITY_DATA);
    }
}
