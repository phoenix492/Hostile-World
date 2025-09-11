package net.phoenix492.registration;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.item.MushroomIgniter;

public class ModItems {
    public static final DeferredRegister.Items MOD_ITEMS = DeferredRegister.createItems(HostileWorld.MODID);

    public static final DeferredItem<Item> DEBUG_MUSHROOM_IGNITER = MOD_ITEMS.register(
      "debug_mushroom_igniter",
        () -> new MushroomIgniter(new Item.Properties())
    );



    public static void register(IEventBus eventBus) {
        MOD_ITEMS.register(eventBus);
    }
}
