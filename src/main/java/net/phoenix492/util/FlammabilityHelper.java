package net.phoenix492.util;

import net.minecraft.world.level.block.Blocks;
import net.phoenix492.block.MushroomFireBlock;
import net.phoenix492.registration.ModBlocks;

/**
 * Contains one static method called from commonSetup that adds flammability for blocks.
 */
public class FlammabilityHelper {
    public static void fireSetup() {
        MushroomFireBlock mushFireBlock = ModBlocks.MUSHROOM_FIRE.get();
        mushFireBlock.setFlammable(Blocks.MYCELIUM,                 1000, 500);
        mushFireBlock.setFlammable(Blocks.BROWN_MUSHROOM_BLOCK,     1000, 500);
        mushFireBlock.setFlammable(Blocks.RED_MUSHROOM_BLOCK,       1000, 500);
        mushFireBlock.setFlammable(Blocks.RED_MUSHROOM,             1000, 500);
        mushFireBlock.setFlammable(Blocks.BROWN_MUSHROOM,           1000, 500);
        mushFireBlock.setFlammable(Blocks.MUSHROOM_STEM,            1000, 500);
        mushFireBlock.setFlammable(ModBlocks.RED_MYCOSTONE.get(),   1000, 500);
        mushFireBlock.setFlammable(ModBlocks.BROWN_MYCOSTONE.get(),   1000, 500);
        mushFireBlock.setFlammable(ModBlocks.MIXED_MYCOSTONE.get(),   1000, 500);

    }
}
