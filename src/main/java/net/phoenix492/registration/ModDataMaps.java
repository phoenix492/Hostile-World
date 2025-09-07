package net.phoenix492.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.phoenix492.data.EnvironmentalInfectionBuildupData;
import net.phoenix492.hostileworld.HostileWorld;

@EventBusSubscriber
public class ModDataMaps {

    public static final DataMapType<Biome, EnvironmentalInfectionBuildupData> INFECTION_BUILDUP = DataMapType.builder(
        ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "fungal_infection_buildup"),
        Registries.BIOME,
        EnvironmentalInfectionBuildupData.CODEC
    ).build();


    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(INFECTION_BUILDUP);
    }
}
