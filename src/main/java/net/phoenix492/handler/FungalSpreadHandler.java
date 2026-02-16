package net.phoenix492.handler;

import net.phoenix492.data.map.FungalTransformationData;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.mixin.FungalSpreadMixins;
import net.phoenix492.mixin.SpreadingSnowyDirtBlockInvoker;
import net.phoenix492.registration.ModBlocks;
import net.phoenix492.registration.ModDataMaps;
import net.phoenix492.util.ModTagKeys;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

/**
 * Contains logic for fungal spread, intended to be called from the randomTick method of blocks.<br>
 * Previously this was inlined into the mixins in {@link FungalSpreadMixins} but that created huge blocks of repeated
 * code, and I needed it again when implementing mycostone.
 */
public class FungalSpreadHandler {
    public static void basicFungalSpread(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < Config.FUNGAL_SPREAD_SPEED.getAsInt(); i++) {
            BlockPos spreadTargetPosition = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
            BlockState spreadTargetBlockstate = level.getBlockState(spreadTargetPosition);
            FungalTransformationData transformData = spreadTargetBlockstate.getBlockHolder().getData(ModDataMaps.FUNGAL_SPREAD_TRANSFORM);


            // If block has no defined transformation, then just return.
            if (transformData == null) {
                continue;
            }

            /*
             Check to make sure the defined transformation we get is actually a valid value,
             Linter says .get() could be a null pointer exception, but I'm not so sure.
             It doesn't hurt to check though, and it keeps the linter happy.
            */
            Optional<Block> optionalTarget = transformData.targets().getRandomValue(random);
            if (optionalTarget.isEmpty()) {
                continue;
            }

            // Once we've confirmed we have the data, we set the block state here so that the conditions afterward can modify it.
            BlockState targetState = optionalTarget.get().defaultBlockState();

            // Spread alike check verifies the target block and the spreading block are the same.
            if (transformData.spreadAlikeCheck()) {
                if (!targetState.getBlock().equals(state.getBlock())) {
                    continue;
                }
            }

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

            // The air exposure check sees if any side of the target block is exposed to air, used so the infection doesn't irrevocably taint the world to the core.
            if (transformData.airExposureCheck()) {
                boolean airExposed = false;
                for (Direction d : Direction.values()) {
                    if (level.getBlockState(spreadTargetPosition.relative(d)).getBlock() instanceof AirBlock) {
                        airExposed = true;
                        break; // small early break optimization when spreading
                    }
                }
                if (!airExposed) {
                    continue;
                }
            }

            // The stone variant check uses the source block to determine which mycostone variant we should spread.
            if (targetState.is(ModTagKeys.Blocks.MYCOSTONES) && transformData.mycostoneVariantCheck()) {
                if(state.is(ModTagKeys.Blocks.RED_FUNGUS)) {
                    targetState = ModBlocks.RED_MYCOSTONE.get().defaultBlockState();
                } else if (state.is(ModTagKeys.Blocks.BROWN_FUNGUS)) {
                    targetState = ModBlocks.BROWN_MYCOSTONE.get().defaultBlockState();
                } else {
                    targetState = ModBlocks.MIXED_MYCOSTONE.get().defaultBlockState();
                }
            }

            // Run consumption check
            if (transformData.consumeChance() > random.nextFloat()) {
                targetState = Blocks.AIR.defaultBlockState();
            }

            // Additional check for snowy blocks (grasses) to check their snow status upon spread.
            if (targetState.getBlock() instanceof SnowyDirtBlock) {
                targetState = targetState.setValue(BlockStateProperties.SNOWY, level.getBlockState(spreadTargetPosition.above()).is(Blocks.SNOW));
            }

            // Finally, set & update the block to its transformed variant.
            level.setBlockAndUpdate(spreadTargetPosition, targetState);
        }
    }
}
