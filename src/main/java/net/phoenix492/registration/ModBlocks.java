package net.phoenix492.registration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.phoenix492.block.MushroomFireBlock;
import net.phoenix492.block.MycostoneBlock;
import net.phoenix492.hostileworld.HostileWorld;

import java.util.function.Supplier;

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

    public static final DeferredBlock<Block> RED_MYCOSTONE = registerWithItem(
        "red_mycostone",
        () -> new MycostoneBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.CRIMSON_HYPHAE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .randomTicks()
        )
    );

    public static final DeferredBlock<Block> BROWN_MYCOSTONE = registerWithItem(
        "brown_mycostone",
        () -> new MycostoneBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.CRIMSON_HYPHAE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .randomTicks()
        )
    );

    public static final DeferredBlock<Block> MIXED_MYCOSTONE = registerWithItem(
        "mixed_mycostone",
        () -> new MycostoneBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.CRIMSON_HYPHAE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
                .randomTicks()
        )
    );


    private static <T extends Block> DeferredBlock<T> registerWithItem(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = MOD_BLOCKS.register(
            name,
            block
        );
        ModItems.MOD_ITEMS.registerSimpleBlockItem(name, toReturn);
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        MOD_BLOCKS.register(eventBus);
    }
}
