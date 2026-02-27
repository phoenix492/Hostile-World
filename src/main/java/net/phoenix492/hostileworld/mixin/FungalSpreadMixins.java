package net.phoenix492.hostileworld.mixin;

import net.phoenix492.hostileworld.config.HostileWorldConfig;
import net.phoenix492.hostileworld.data.map.BiomeFungalSpreadData;
import net.phoenix492.hostileworld.handler.FungalSpreadHandler;
import net.phoenix492.hostileworld.registration.ModDataMaps;
import net.phoenix492.hostileworld.util.ModTagKeys;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.MyceliumBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public abstract class FungalSpreadMixins {

    // The parent mixins here add hooks into a parent method that the children can override, thus preventing mixin conflicts from directly overriding the method.
    @Mixin(BlockBehaviour.class)
    public abstract static class BlockBehaviorMixin {
        @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
        protected void hostileworld$onRandomTickForHugeMushroomBlock(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {}

        @Inject(at = @At("HEAD"), method = "randomTick")
        protected void hostileworld$onRandomTickForSporeDropper(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {
            if (!level.getBlockState(pos).is(ModTagKeys.Blocks.DROPS_SPORES)) {
                return;
            }

            BlockPos.MutableBlockPos scannedBlockPos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
            for (int i = 2; i <= HostileWorldConfig.SERVER_CONFIG.sporeDropperRange.get(); i++) {
                scannedBlockPos.setY(scannedBlockPos.getY() - 1);

                // If the block is air, we don't actually need to check it. Air doesn't convert, stupid, gg go next.
                if (level.getBlockState(scannedBlockPos).isAir()) {
                    continue;
                }

                // If the block we're checking is not replaceable, this will be our last checked block since it's solid
                // and should block anymore spores.
                if (!level.getBlockState(scannedBlockPos).is(BlockTags.REPLACEABLE)) {
                    i = HostileWorldConfig.SERVER_CONFIG.sporeDropperRange.get();
                }

                FungalSpreadHandler.trySpreadNoChance(state, level, scannedBlockPos, random);
            }
        }
    }

    @Mixin(SpreadingSnowyDirtBlock.class)
    public abstract static class SpreadingSnowyDirtBlockMixin {
        @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
        protected void hostileworld$onRandomTickForGrassBlock(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {}

        @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
        protected void hostileworld$onRandomTickForMyceliumBlock(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {}
    }

    @Mixin(GrassBlock.class)
    public abstract static class GrassBlockMixin extends SpreadingSnowyDirtBlockMixin{
        @Override
        protected void hostileworld$onRandomTickForGrassBlock(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {
            if (!HostileWorldConfig.SERVER_CONFIG.spontaneousGermination.get()) {
                return;
            }

            BiomeFungalSpreadData spreadData = level.getBiome(pos).getData(ModDataMaps.BIOME_FUNGAL_SPREAD);
            if (spreadData == null) {
                return;
            }

            if (spreadData.germinationChance() <= 0) {
                return;
            }

            if (spreadData.germinationChance() > random.nextFloat()) {
                level.setBlockAndUpdate(pos, Blocks.MYCELIUM.defaultBlockState());
            }
        }
    }

    @Mixin(MyceliumBlock.class)
    public abstract static class MyceliumBlockMixin extends SpreadingSnowyDirtBlockMixin {
        @Override
        protected void hostileworld$onRandomTickForMyceliumBlock(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo info) {
            // First check if this block can't actually be grass, such as having a block on top. If so, set it to dirt and move on.
            // This is exactly what vanilla grass and mycelium do, but I've replicated here
            if (!SpreadingSnowyDirtBlockInvoker.invokeCanBeGrass(state, level, pos)) {
                // Prevent loading unloaded chunks when checking dirt eligibility
                if (level.isAreaLoaded(pos, 1)) {
                    level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
                }
                info.cancel();
                return;
            }

            // Prevent loading unloaded chunks when spreading
            if (!level.isAreaLoaded(pos, 3)) {
                info.cancel();
                return;
            }

            // Call the basic fungal spread handler now that we've done block specific stuff.
            FungalSpreadHandler.basicFungalSpread(state, level, pos, random);

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

            // Call the basic fungal spread handler now that we've done block specific stuff.
            FungalSpreadHandler.basicFungalSpread(state, level, pos, random);

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

