package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.block.FungusSpreadingBlock;
import net.phoenix492.hostileworld.block.MycofireBlock;
import net.phoenix492.hostileworld.block.MycorestoneBlock;
import net.phoenix492.hostileworld.block.MycostoneBlock;
import net.phoenix492.hostileworld.block.MycoturfBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks MOD_BLOCKS = DeferredRegister.createBlocks(HostileWorld.MODID);

    public static final DeferredBlock<MycofireBlock> MYCOFIRE = MOD_BLOCKS.register(
        "mycofire",
        () -> new MycofireBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .replaceable()
                .noCollission() // WHY
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
                .strength(3.0F, 6.0F)
                .sound(SoundType.NETHERRACK)
                .randomTicks()
        )
    );

    public static final DeferredBlock<Block> BROWN_MYCOSTONE = registerWithItem(
        "brown_mycostone",
        () -> new MycostoneBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BROWN)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 6.0F)
                .sound(SoundType.NETHERRACK)
                .randomTicks()
        )
    );

    public static final DeferredBlock<Block> MIXED_MYCOSTONE = registerWithItem(
        "mixed_mycostone",
        () -> new MycostoneBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.TERRACOTTA_BROWN)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(3.0F, 6.0F)
                .sound(SoundType.NETHERRACK)
                .randomTicks()
        )
    );

    public static final DeferredBlock<RotatedPillarBlock> MYCORESTONE = registerWithItem(
        "mycorestone",
        () -> new MycorestoneBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.TERRACOTTA_PURPLE)
                .instrument(NoteBlockInstrument.BIT)
                .requiresCorrectToolForDrops()
                .strength(5.0f, 6.0f)
                .sound(SoundType.DEEPSLATE)
                .randomTicks()
        )
    );

    public static final DeferredBlock<FungusSpreadingBlock> MYCOTURF = registerWithItem(
        "mycoturf",
        () -> new MycoturfBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .instrument(NoteBlockInstrument.DIDGERIDOO)
                .strength(0.1f)
                .sound(SoundType.MOSS)
                .randomTicks()
        )
    );

    public static final DeferredBlock<FungusSpreadingBlock> NASCENT_AUTOIMMUNE_CLUSTER = registerWithItem(
        "nascent_autoimmune_cluster",
        () -> new FungusSpreadingBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.TERRACOTTA_PURPLE)
                .requiresCorrectToolForDrops()
                .strength(3.0f, 6.0f)
                .sound(SoundType.NETHER_ORE)
                .randomTicks()
        )
    );

    public static final DeferredBlock<FungusSpreadingBlock> MATURE_AUTOIMMUNE_CLUSTER = registerWithItem(
        "mature_autoimmune_cluster",
        () -> new FungusSpreadingBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.TERRACOTTA_PURPLE)
                .requiresCorrectToolForDrops()
                .strength(4.5f, 6.0f)
                .sound(SoundType.NETHER_ORE)
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
