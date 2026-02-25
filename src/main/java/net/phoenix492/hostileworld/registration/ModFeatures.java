package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.worldgen.feature.FlatTopShelfFungus;
import net.phoenix492.hostileworld.worldgen.feature.configurations.FlatTopShelfFungusConfiguration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> MOD_FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, HostileWorld.MODID);

    public static final DeferredHolder<Feature<?>, FlatTopShelfFungus> FLAT_TOP_SHELF_FUNGUS = MOD_FEATURES.register(
        "flat_top_shelf_fungus",
        () -> new FlatTopShelfFungus(FlatTopShelfFungusConfiguration.CODEC));

    public  static void register(IEventBus eventBus) {
        MOD_FEATURES.register(eventBus);
    }
}
