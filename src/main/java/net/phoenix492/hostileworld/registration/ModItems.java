package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.block.MycofireBlock;
import net.phoenix492.hostileworld.data.component.MushroomIgniterData;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.item.MushroomIgniter;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items MOD_ITEMS = DeferredRegister.createItems(HostileWorld.MODID);

    public static final DeferredItem<Item> APOCALYPTIC_MUSHROOM_IGNITER = MOD_ITEMS.register(
      "apocalyptic_mushroom_igniter",
        () -> new MushroomIgniter(new Item.Properties().component(ModDataComponents.MUSHROOM_IGNITER_STRENGTH, new MushroomIgniterData(MycofireBlock.MycofireStrength.APOCALYPTIC)).stacksTo(1))
    );
    public static final DeferredItem<Item> STRONG_MUSHROOM_IGNITER = MOD_ITEMS.register(
        "strong_mushroom_igniter",
        () -> new MushroomIgniter(new Item.Properties().component(ModDataComponents.MUSHROOM_IGNITER_STRENGTH, new MushroomIgniterData(MycofireBlock.MycofireStrength.STRONG)).stacksTo(1))
    );
    public static final DeferredItem<Item> NORMAL_MUSHROOM_IGNITER = MOD_ITEMS.register(
        "normal_mushroom_igniter",
        () -> new MushroomIgniter(new Item.Properties().component(ModDataComponents.MUSHROOM_IGNITER_STRENGTH, new MushroomIgniterData(MycofireBlock.MycofireStrength.NORMAL)).stacksTo(1))
    );
    public static final DeferredItem<Item> WEAK_MUSHROOM_IGNITER = MOD_ITEMS.register(
        "weak_mushroom_igniter",
        () -> new MushroomIgniter(new Item.Properties().component(ModDataComponents.MUSHROOM_IGNITER_STRENGTH, new MushroomIgniterData(MycofireBlock.MycofireStrength.WEAK)).stacksTo(1))
    );
    public static final DeferredItem<Item> IMMUNITE_SHARD = MOD_ITEMS.register(
        "immunite_shard",
        () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> IMMUNITE_CLUSTER = MOD_ITEMS.register(
        "immunite_cluster",
        () -> new Item(new Item.Properties())
    );




    public static void register(IEventBus eventBus) {
        MOD_ITEMS.register(eventBus);
    }
}
