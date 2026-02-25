package net.phoenix492.hostileworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MycoturfBlock extends FungusSpreadingBlock {
    public MycoturfBlock(Properties properties) {
        super(properties);
    }

    // Yoinked from MyceliumBlock
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        super.animateTick(state, level, pos, randomSource);
        if (randomSource.nextInt(10) == 0) {
            level.addParticle(
                ParticleTypes.MYCELIUM,
                (double)pos.getX() + randomSource.nextDouble(),
                (double)pos.getY() + 1.1,
                (double)pos.getZ() + randomSource.nextDouble(),
                0.0,
                0.0,
                0.0
            );
        }
    }
}
