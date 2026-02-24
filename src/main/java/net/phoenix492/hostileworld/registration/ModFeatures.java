package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.worldgen.feature.FlatShelfFungus;
import net.phoenix492.hostileworld.worldgen.feature.configurations.FlatShelfFungusConfiguration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> MOD_FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, HostileWorld.MODID);

    public static final DeferredHolder<Feature<?>, FlatShelfFungus> BROWN_SHELF_FUNGUS = MOD_FEATURES.register(
        "brown_shelf_fungus",
        () -> new FlatShelfFungus(FlatShelfFungusConfiguration.CODEC));

    public  static void register(IEventBus eventBus) {
        MOD_FEATURES.register(eventBus);
    }
}
