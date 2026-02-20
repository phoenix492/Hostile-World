package net.phoenix492.block;

import net.phoenix492.handler.FungalSpreadHandler;
import net.phoenix492.registration.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MycostoneBlock extends Block {
    public MycostoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Prevent loading unloaded chunks when spreading
        if (!level.isAreaLoaded(pos, 3)) {
            return;
        }

        // Mycostone Mixing! Red and brown mycostones bordering each other should form mixed mycostone as they coagulate.
        if (!state.is(ModBlocks.MIXED_MYCOSTONE.get())) {
            // Get our opposite blockstate.
            Block opposite = null;
            if (state.is(ModBlocks.RED_MYCOSTONE.get())) {
                opposite = ModBlocks.BROWN_MYCOSTONE.get();
            }
            else if (state.is(ModBlocks.BROWN_MYCOSTONE.get())) {
                opposite = ModBlocks.RED_MYCOSTONE.get();
            }

            if (opposite != null) {
                for (Direction d : Direction.values()) {
                    if (level.getBlockState(pos.relative(d)).is(opposite)) {
                        level.setBlockAndUpdate(pos, ModBlocks.MIXED_MYCOSTONE.get().defaultBlockState());
                        break;
                    }
                }
            }
        }

        FungalSpreadHandler.basicFungalSpread(state, level, pos, random);
    }
}
