package net.phoenix492.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MyceliumBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public abstract class MyceliumSpreadMixin {

    @Mixin(SpreadingSnowyDirtBlock.class)
    public abstract static class SpreadingSnowyDirtBlockMixin {

        @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
        protected void hostileworld$onRandomTickForMycelium(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {}
    }

    @Mixin(MyceliumBlock.class)
    public abstract static class MyceliumBlockMixin extends SpreadingSnowyDirtBlockMixin {
        @Override
        protected void hostileworld$onRandomTickForMycelium(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {
            if (!SpreadingSnowyDirtBlockInvoker.invokeCanBeGrass(state, level, pos)) {
                if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
                level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            } else {
                if (!level.isAreaLoaded(pos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
                if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                    BlockState blockstate = ((MyceliumBlock)(Object)this).defaultBlockState();

                    for (int i = 0; i < 4; i++) {
                        BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                        if ((level.getBlockState(blockpos).is(Blocks.DIRT) || level.getBlockState(blockpos).is(Blocks.GRASS_BLOCK)) && SpreadingSnowyDirtBlockInvoker.invokeCanPropagate(blockstate, level, blockpos)) {
                            level.setBlockAndUpdate(
                                blockpos, blockstate.setValue(BlockStateProperties.SNOWY, level.getBlockState(blockpos.above()).is(Blocks.SNOW))
                            );
                        }
                    }
                }
            }
            info.cancel();
        }
    }
}

