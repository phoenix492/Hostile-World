package net.phoenix492.registration;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.phoenix492.block.MushroomFireBlock;
import net.phoenix492.hostileworld.HostileWorld;

public class ModBlocks {

    public static final DeferredRegister.Blocks MOD_BLOCKS = DeferredRegister.createBlocks(HostileWorld.MODID);

    public static final DeferredBlock<MushroomFireBlock> MUSHROOM_FIRE = MOD_BLOCKS.register(
        "mushroom_fire",
        () -> new MushroomFireBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .replaceable()
                .noCollission()
                .instabreak()
                .lightLevel(state -> 15)
                .sound(SoundType.WOOL)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
        )
    );

    public static void register(IEventBus eventBus) {
        MOD_BLOCKS.register(eventBus);
    }
}
