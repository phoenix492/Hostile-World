package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.data.component.MushroomIgniterData;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, HostileWorld.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MushroomIgniterData>> MUSHROOM_IGNITER_STRENGTH = DATA_COMPONENTS.registerComponentType(
      "mushroom_igniter_strength",
      builder -> builder.persistent(MushroomIgniterData.IGNITER_DATA_CODEC)
    );

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}
