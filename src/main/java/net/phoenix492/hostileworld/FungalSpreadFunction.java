package net.phoenix492.hostileworld;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.phoenix492.util.TagKeys;

import java.util.function.Function;

// TODO: Make this a datamap
public class FungalSpreadFunction {
    public static final Function<BlockState, BlockState> FUNGAL_SPREAD_FUNCTION = (transformed -> {
        if (transformed.is(TagKeys.Blocks.BECOMES_MYCELIUM)) {
            return Blocks.MYCELIUM.defaultBlockState();
        }
        if (transformed.is(TagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK)) {
            return Blocks.RED_MUSHROOM_BLOCK.defaultBlockState();
        }
        if (transformed.is(TagKeys.Blocks.BECOMES_BROWN_MUSHROOM_BLOCK)) {
            return Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState();
        }
        if (transformed.is(TagKeys.Blocks.BECOMES_MUSHROOM_STEM)) {
            return Blocks.MUSHROOM_STEM.defaultBlockState();
        }
        if (transformed.is(TagKeys.Blocks.BECOMES_RED_MUSHROOM)) {
            return Blocks.RED_MUSHROOM.defaultBlockState();
        }
        if (transformed.is(TagKeys.Blocks.BECOMES_BROWN_MUSHROOM)) {
            return Blocks.BROWN_MUSHROOM.defaultBlockState();
        }
        if (transformed.is(TagKeys.Blocks.CONSUMED_BY_FUNGUS)) {
            return Blocks.AIR.defaultBlockState();
        }
        // Default
        return Blocks.RED_MUSHROOM_BLOCK.defaultBlockState();
    });
}
