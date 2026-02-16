package net.phoenix492.util;

import net.phoenix492.block.MycofireBlock;
import net.phoenix492.registration.ModBlocks;

import net.minecraft.world.level.block.Blocks;

/**
 * Contains one static method called from commonSetup that adds flammability for blocks.
 */
public class FlammabilityHelper {
    public static void fireSetup() {
        MycofireBlock mushFireBlock = ModBlocks.MYCOFIRE.get();
        mushFireBlock.setFlammable(Blocks.MYCELIUM,                 1000, 500);
        mushFireBlock.setFlammable(Blocks.BROWN_MUSHROOM_BLOCK,     1000, 500);
        mushFireBlock.setFlammable(Blocks.RED_MUSHROOM_BLOCK,       1000, 500);
        mushFireBlock.setFlammable(Blocks.RED_MUSHROOM,             1000, 500);
        mushFireBlock.setFlammable(Blocks.BROWN_MUSHROOM,           1000, 500);
        mushFireBlock.setFlammable(Blocks.MUSHROOM_STEM,            1000, 500);
        mushFireBlock.setFlammable(ModBlocks.RED_MYCOSTONE.get(),   1000, 500);
        mushFireBlock.setFlammable(ModBlocks.BROWN_MYCOSTONE.get(), 1000, 500);
        mushFireBlock.setFlammable(ModBlocks.MIXED_MYCOSTONE.get(), 1000, 500);
        mushFireBlock.setFlammable(ModBlocks.MYCORESTONE.get(),     1000, 500);
        mushFireBlock.setFlammable(ModBlocks.MYCOTURF.get(),        1000, 500);

    }
}
