package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HostileWorld.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HOSTILE_WORLD_CREATIVE_TAB = CREATIVE_MODE_TABS.register(
        "hostileworld_creative_tab",
        () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModBlocks.MIXED_MYCOSTONE.asItem()))
            .title(Component.translatable("creativetab.hostileworld.hostileworld_creative_tab"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItems.APOCALYPTIC_MUSHROOM_IGNITER);
                output.accept(ModItems.STRONG_MUSHROOM_IGNITER);
                output.accept(ModItems.NORMAL_MUSHROOM_IGNITER);
                output.accept(ModItems.WEAK_MUSHROOM_IGNITER);
                output.accept(ModBlocks.MYCOTURF.asItem());
                output.accept(ModBlocks.RED_MYCOSTONE.asItem());
                output.accept(ModBlocks.BROWN_MYCOSTONE.asItem());
                output.accept(ModBlocks.MIXED_MYCOSTONE.asItem());
                output.accept(ModBlocks.MYCORESTONE.asItem());
                output.accept(ModBlocks.NASCENT_AUTOIMMUNE_CLUSTER.asItem());
                output.accept(ModBlocks.MATURE_AUTOIMMUNE_CLUSTER.asItem());
                output.accept(ModItems.IMMUNITE_SHARD);
                output.accept(ModItems.IMMUNITE_CLUSTER);
            })
            .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
