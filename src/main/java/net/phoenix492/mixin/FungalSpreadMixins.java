package net.phoenix492.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.MyceliumBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.phoenix492.hostileworld.FungalSpreadFunction;
import net.phoenix492.util.TagKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public abstract class FungalSpreadMixins {

    @Mixin(BlockBehaviour.class)
    public abstract static class BlockBehaviorMixin {

        @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
        protected void hostileworld$onRandomTickForHugeMushroomBlock(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {}
    }

    @Mixin(SpreadingSnowyDirtBlock.class)
    public abstract static class SpreadingSnowyDirtBlockMixin {
        @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
        protected void hostileworld$onRandomTickForMyceliumBlock(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {}
    }

    @Mixin(MyceliumBlock.class)
    public abstract static class MyceliumBlockMixin extends SpreadingSnowyDirtBlockMixin {
        @Override
        protected void hostileworld$onRandomTickForMyceliumBlock(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {
            // First check if this block can't actually be grass, such as having a block on top. If so, set it to dirt and move on.
            // This is exactly what vanilla grass and mycelium do, but I've replicated here
            if (!SpreadingSnowyDirtBlockInvoker.invokeCanBeGrass(state, level, pos)) {
                // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
                if (!level.isAreaLoaded(pos, 1)) {
                    info.cancel();
                    return;
                }
                level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            }
            else {
                // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
                if (!level.isAreaLoaded(pos, 3)) {
                    info.cancel();
                    return;
                }
                // Unlike parent, no longer checks for light level when spreading. The fungus is evil like that.
                for (int i = 0; i < 4; i++) {
                    BlockPos spreadTargetPosition = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    BlockState spreadTargetBlockstate = level.getBlockState(spreadTargetPosition);
                    if (spreadTargetBlockstate.is(TagKeys.Blocks.BECOMES_FUNGUS)) {
                        BlockState spreadTargetTransformedBlockstate = FungalSpreadFunction.FUNGAL_SPREAD_FUNCTION.apply(spreadTargetBlockstate);
                        // If we're spreading Mycelium, we need to make sure mycelium can actually propagate to where it's going. We also need to set its snowy status.
                        if (spreadTargetTransformedBlockstate.is(Blocks.MYCELIUM)) {
                            if (SpreadingSnowyDirtBlockInvoker.invokeCanPropagate(Blocks.MYCELIUM.defaultBlockState(), level, spreadTargetPosition)) {
                                level.setBlockAndUpdate(spreadTargetPosition, spreadTargetTransformedBlockstate.setValue(BlockStateProperties.SNOWY, level.getBlockState(spreadTargetPosition.above()).is(Blocks.SNOW)));
                            }
                        }
                        // If we're spreading mushrooms, we also want to make sure they won't just pop off and leave dropped items everywhere.
                        else if (spreadTargetTransformedBlockstate.is(Blocks.RED_MUSHROOM) || spreadTargetTransformedBlockstate.is(Blocks.BROWN_MUSHROOM)) {
                            if (level.getBlockState(spreadTargetPosition.below()).is(Blocks.MYCELIUM)) {
                                level.setBlockAndUpdate(spreadTargetPosition, spreadTargetTransformedBlockstate);
                            }
                        }
                        // Otherwise just go ahead and set the block.
                        else {
                            level.setBlockAndUpdate(spreadTargetPosition, spreadTargetTransformedBlockstate);
                        }
                    }
                }
            }
            info.cancel();
        }
    }

    @Mixin(HugeMushroomBlock.class)
    public abstract static class HugeMushroomBlockMixin extends BlockBehaviorMixin {
        @Override
        protected void hostileworld$onRandomTickForHugeMushroomBlock(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {
            // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            if (!level.isAreaLoaded(pos, 3)) {
                info.cancel();
                return;
            }
            // Unlike parent, no longer checks for light level when spreading. The fungus is evil like that.
            for (int i = 0; i < 4; i++) {
                BlockPos spreadTargetPosition = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                BlockState spreadTargetBlockstate = level.getBlockState(spreadTargetPosition);
                if (spreadTargetBlockstate.is(TagKeys.Blocks.BECOMES_FUNGUS)) {
                    BlockState spreadTargetTransformedBlockstate = FungalSpreadFunction.FUNGAL_SPREAD_FUNCTION.apply(spreadTargetBlockstate);
                    // If we're spreading Mycelium, we need to make sure mycelium can actually propagate to where it's going. We also need to set its snowy status.
                    if (spreadTargetTransformedBlockstate.is(Blocks.MYCELIUM)) {
                        if (SpreadingSnowyDirtBlockInvoker.invokeCanPropagate(Blocks.MYCELIUM.defaultBlockState(), level, spreadTargetPosition)) {
                            level.setBlockAndUpdate(spreadTargetPosition, spreadTargetTransformedBlockstate.setValue(BlockStateProperties.SNOWY, level.getBlockState(spreadTargetPosition.above()).is(Blocks.SNOW)));
                        }
                    }
                    // If we're spreading mushrooms, we also want to make sure they won't just pop off and leave dropped items everywhere.
                    else if (spreadTargetTransformedBlockstate.is(Blocks.RED_MUSHROOM) || spreadTargetTransformedBlockstate.is(Blocks.BROWN_MUSHROOM)) {
                        if (level.getBlockState(spreadTargetPosition.below()).is(Blocks.MYCELIUM)) {
                            level.setBlockAndUpdate(spreadTargetPosition, spreadTargetTransformedBlockstate);
                        }
                    }
                    // Otherwise just go ahead and set the block.
                    else {
                        level.setBlockAndUpdate(spreadTargetPosition, spreadTargetTransformedBlockstate);
                    }
                }
            }
            info.cancel();
        }
    }

    /**
     * Each of the mixins in this class does the exact same thing, and that's make a block that ordinarily doesn't randomly tick begin randomly ticking.
     */
    @Mixin(Blocks.class)
    public abstract static class BlocksMixin {
        @Definition(id = "block", field = "Lnet/minecraft/world/level/block/Blocks;MUSHROOM_STEM:Lnet/minecraft/world/level/block/Block;")
        @Definition(id = "block_class", type = HugeMushroomBlock.class)
        @Definition(id = "register", method = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/Block;")
        @Expression("block = register('mushroom_stem', @(new block_class(?)))")
        @ModifyArg(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
        private static BlockBehaviour.Properties makeMushroomStemTick(BlockBehaviour.Properties properties) {
            return properties.randomTicks();
        }

        @Definition(id = "block", field = "Lnet/minecraft/world/level/block/Blocks;BROWN_MUSHROOM_BLOCK:Lnet/minecraft/world/level/block/Block;")
        @Definition(id = "HMB", type = HugeMushroomBlock.class)
        @Definition(id = "register", method = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/Block;")
        @Expression("block = register('brown_mushroom_block', @(new HMB(?)))")
        @ModifyArg(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
        private static BlockBehaviour.Properties makeBrownMushroomBlockTick(BlockBehaviour.Properties properties) {
            return properties.randomTicks();
        }

        @Definition(id = "block", field = "Lnet/minecraft/world/level/block/Blocks;RED_MUSHROOM_BLOCK:Lnet/minecraft/world/level/block/Block;")
        @Definition(id = "HMB", type = HugeMushroomBlock.class)
        @Definition(id = "register", method = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/Block;")
        @Expression("block = register('red_mushroom_block', @(new HMB(?)))")
        @ModifyArg(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
        private static BlockBehaviour.Properties makeRedMushroomBlockTick(BlockBehaviour.Properties properties) {
            return properties.randomTicks();
        }
    }
}

