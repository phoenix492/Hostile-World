package net.phoenix492.util;

import net.minecraft.world.level.block.Blocks;
import net.phoenix492.block.MushroomFireBlock;
import net.phoenix492.registration.ModBlocks;

/**
 * Contains one static method called from commonSetup that adds flammability for blocks.
 */
public class FlammabilityHelper {
    public static void fireSetup() {
        MushroomFireBlock fireBlock = ModBlocks.MUSHROOM_FIRE.get();
        fireBlock.setFlammable(Blocks.MYCELIUM, 1000, 500);
        fireBlock.setFlammable(Blocks.BROWN_MUSHROOM_BLOCK, 1000, 500);
        fireBlock.setFlammable(Blocks.RED_MUSHROOM_BLOCK, 1000, 500);
        fireBlock.setFlammable(Blocks.RED_MUSHROOM, 1000, 500);
        fireBlock.setFlammable(Blocks.BROWN_MUSHROOM, 1000, 500);
        fireBlock.setFlammable(Blocks.MUSHROOM_STEM, 1000, 500);
    }
}
