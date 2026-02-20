package net.phoenix492.hostileworld.block;

import net.phoenix492.hostileworld.handler.FungalSpreadHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MycorestoneBlock extends RotatedPillarBlock {
    public MycorestoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Prevent loading unloaded chunks when spreading
        if (!level.isAreaLoaded(pos, 3)) {
            return;
        }

        FungalSpreadHandler.basicFungalSpread(state, level, pos, random);
    }

}
