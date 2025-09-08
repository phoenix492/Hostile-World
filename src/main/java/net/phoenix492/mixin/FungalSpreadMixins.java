package net.phoenix492.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.phoenix492.data.FungalTransformationData;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.registration.ModDataMaps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public abstract class FungalSpreadMixins {

    // The two parent mixins here add a hook into a parent method that the children can override, thus preventing mixin conflicts from directly overriding the method.
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
                if (level.isAreaLoaded(pos, 1)) {
                    level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
                }
                info.cancel();
                return;
            }

            // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            if (!level.isAreaLoaded(pos, 3)) {
                info.cancel();
                return;
            }

            for (int i = 0; i < Config.FUNGAL_SPREAD_SPEED.getAsInt(); i++) {
                BlockPos spreadTargetPosition = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                BlockState spreadTargetBlockstate = level.getBlockState(spreadTargetPosition);
                FungalTransformationData transformData = spreadTargetBlockstate.getBlockHolder().getData(ModDataMaps.FUNGAL_SPREAD_TRANSFORM);

                // If block has no defined transformation, then just return.
                if (transformData == null) {
                    continue;
                }

                // Once we've confirmed we have the data, we set the block state here so that the conditions afterward can modify it.
                BlockState targetState = transformData.target().defaultBlockState();

                // Run failure check.
                if (transformData.failChance() > random.nextFloat()) {
                    continue;
                }

                // Run biome failure check
                if (level.getBiome(spreadTargetPosition).getData(ModDataMaps.BIOME_FUNGAL_SPREAD) != null && level.getBiome(spreadTargetPosition).getData(ModDataMaps.BIOME_FUNGAL_SPREAD).spreadSuccessChance() < random.nextFloat()) {
                    continue;
                }

                // The propagation check makes sure the target block is capable of being grass & isn't underwater.
                if (transformData.propagationCheck()) {
                    if (!SpreadingSnowyDirtBlockInvoker.invokeCanPropagate(targetState, level, spreadTargetPosition)) {
                        continue;
                    }
                }

                // The mycelium check has us check underneath the target block for mycelium, used by default so mushrooms don't pop-off
                if (transformData.myceliumCheck()) {
                    if (!level.getBlockState(spreadTargetPosition.below()).is(Blocks.MYCELIUM)) {
                        continue;
                    }
                }

                // Run consumption check
                if (transformData.consumeChance() > random.nextFloat()) {
                    targetState = Blocks.AIR.defaultBlockState();
                }

                // Additional check for snowy blocks (grasses) to check their snow status upon spread.
                if (targetState.getBlock() instanceof SnowyDirtBlock) {
                    targetState.setValue(BlockStateProperties.SNOWY, level.getBlockState(spreadTargetPosition.above()).is(Blocks.SNOW));
                }

                // Finally, set & update the block to its transformed variant.
                level.setBlockAndUpdate(spreadTargetPosition, targetState);
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

            for (int i = 0; i < Config.FUNGAL_SPREAD_SPEED.getAsInt(); i++) {
                BlockPos spreadTargetPosition = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                BlockState spreadTargetBlockstate = level.getBlockState(spreadTargetPosition);
                FungalTransformationData transformData = spreadTargetBlockstate.getBlockHolder().getData(ModDataMaps.FUNGAL_SPREAD_TRANSFORM);

                // If block has no defined transformation, then just return.
                if (transformData == null) {
                    continue;
                }

                // Once we've confirmed we have the data, we set the block state here so that the conditions afterward can modify it.
                BlockState targetState = transformData.target().defaultBlockState();

                // Run failure check.
                if (transformData.failChance() > random.nextFloat()) {
                    continue;
                }

                // Run biome failure check
                if (level.getBiome(spreadTargetPosition).getData(ModDataMaps.BIOME_FUNGAL_SPREAD) != null && level.getBiome(spreadTargetPosition).getData(ModDataMaps.BIOME_FUNGAL_SPREAD).spreadSuccessChance() < random.nextFloat()) {
                    continue;
                }

                // The propagation check makes sure the target block is capable of being grass & isn't underwater.
                if (transformData.propagationCheck()) {
                    if (!SpreadingSnowyDirtBlockInvoker.invokeCanPropagate(targetState, level, spreadTargetPosition)) {
                        continue;
                    }
                }

                // The mycelium check has us check underneath the target block for mycelium, used by default so mushrooms don't pop-off
                if (transformData.myceliumCheck()) {
                    if (!level.getBlockState(spreadTargetPosition.below()).is(Blocks.MYCELIUM)) {
                        continue;
                    }
                }

                // Run consumption check
                if (transformData.consumeChance() > random.nextFloat()) {
                    targetState = Blocks.AIR.defaultBlockState();
                }

                // Additional check for snowy blocks (grasses) to check their snow status upon spread.
                if (targetState.getBlock() instanceof SnowyDirtBlock) {
                    targetState.setValue(BlockStateProperties.SNOWY, level.getBlockState(spreadTargetPosition.above()).is(Blocks.SNOW));
                }

                // Finally, set & update the block to its transformed variant.
                level.setBlockAndUpdate(spreadTargetPosition, targetState);
            }
            info.cancel();
        }
    }

    /**
     * Each of the mixins in this class does the exact same thing, and that's make a block that ordinarily doesn't randomly tick begin randomly ticking.
     * It does so by mixing into its registration, grabbing the BlockBehavior.Properties passed to the new block call, and calling .randomTicks() on it.
     */
    @Mixin(Blocks.class)
    public abstract static class BlocksMixin {
        @Definition(id = "block", field = "Lnet/minecraft/world/level/block/Blocks;MUSHROOM_STEM:Lnet/minecraft/world/level/block/Block;")
        @Definition(id = "block_class", type = HugeMushroomBlock.class)
        @Definition(id = "register", method = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/Block;")
        @Expression("block = register('mushroom_stem', @(new block_class(?)))")
        @ModifyArg(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
        private static BlockBehaviour.Properties hostileworld$makeMushroomStemTick(BlockBehaviour.Properties properties) {
            return properties.randomTicks();
        }

        @Definition(id = "block", field = "Lnet/minecraft/world/level/block/Blocks;BROWN_MUSHROOM_BLOCK:Lnet/minecraft/world/level/block/Block;")
        @Definition(id = "block_class", type = HugeMushroomBlock.class)
        @Definition(id = "register", method = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/Block;")
        @Expression("block = register('brown_mushroom_block', @(new block_class(?)))")
        @ModifyArg(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
        private static BlockBehaviour.Properties hostileworld$makeBrownMushroomBlockTick(BlockBehaviour.Properties properties) {
            return properties.randomTicks();
        }

        @Definition(id = "block", field = "Lnet/minecraft/world/level/block/Blocks;RED_MUSHROOM_BLOCK:Lnet/minecraft/world/level/block/Block;")
        @Definition(id = "block_class", type = HugeMushroomBlock.class)
        @Definition(id = "register", method = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/level/block/Block;")
        @Expression("block = register('red_mushroom_block', @(new block_class(?)))")
        @ModifyArg(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
        private static BlockBehaviour.Properties hostileworld$makeRedMushroomBlockTick(BlockBehaviour.Properties properties) {
            return properties.randomTicks();
        }
    }
}

