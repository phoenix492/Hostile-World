package net.phoenix492.hostileworld.handler;

import net.phoenix492.hostileworld.config.HostileWorldConfig;
import net.phoenix492.hostileworld.data.map.FungalTransformationData;
import net.phoenix492.hostileworld.mixin.FungalSpreadMixins;
import net.phoenix492.hostileworld.mixin.SpreadingSnowyDirtBlockInvoker;
import net.phoenix492.hostileworld.registration.ModBlocks;
import net.phoenix492.hostileworld.registration.ModDataMaps;
import net.phoenix492.hostileworld.registration.ModGameRules;
import net.phoenix492.hostileworld.util.ModTagKeys;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Contains logic for fungal spread, intended to be called from the randomTick method of blocks.<br> <br>
 * Fun dev history fact, this was originally inlined into the mixins in {@link FungalSpreadMixins}. Even then, it created a large block
 * of repeat code, once for Mycelium and once for HugeMushroomBlocks, the original two spreaders. Now, it's expanded to include
 * use by SporeDropping blocks that randomTick(), as well as my own custom blocks that spread fungus.
 */
public class FungalSpreadHandler {

    // Will attempt to spread to a config defined amount of blocks in the area.
    public static void basicFungalSpread(BlockState spreaderState, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos spreadTargetPosition = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

        for (int i = 0; i < HostileWorldConfig.SERVER_CONFIG.fungalSpreadSpeed.get(); i++) {
            trySpread(spreaderState, level, spreadTargetPosition, random);
        }
    }

    // Runs chance based failure checks.
    public static void trySpread(BlockState spreaderState, ServerLevel level, BlockPos spreadTargetPosition, RandomSource random) {
        BlockState spreadTargetBlockstate = level.getBlockState(spreadTargetPosition);
        FungalTransformationData transformationData = spreadTargetBlockstate.getBlockHolder().getData(ModDataMaps.FUNGAL_SPREAD_TRANSFORM);

        if (!gameruleEnabled(level)) return;
        if (transformDataNull(transformationData)) return;

        // Once we've confirmed we have the data, we set the block state here so that the conditions afterward can modify it.
        // Ignore the linter warning here, if we have transform data, then we have at least one entry in the targets list.
        BlockState targetState = transformationData.targets().getRandomValue(random).get().defaultBlockState();

        if (!spreadAlikeCheckPass(targetState, spreaderState, transformationData)) return;
        if (!failCheckPass(random, transformationData)) return;
        if (!biomeFailCheckPass(level, spreadTargetPosition, random)) return;
        if (!propagationCheckPass(level, targetState, spreadTargetPosition, transformationData)) return;
        if (!myceliumCheckPass(level, spreadTargetPosition, transformationData)) return;
        if (!airExposureCheckPass(level, spreadTargetPosition, transformationData)) return;

        targetState = modifyTargetState(level, targetState, spreadTargetPosition, spreaderState, transformationData, random);

        // Finally, set & update the block to its transformed variant.
        level.setBlockAndUpdate(spreadTargetPosition, targetState);
    }

    // Does not run chance based failures. Used by Spore Droppers.
    // We still need the RandomSource for other random chances that aren't failure.
    // Notably, modifyTargetState's consume chance and the block selection.
    public static void trySpreadNoChance(BlockState spreaderState, ServerLevel level, BlockPos spreadTargetPosition, RandomSource random) {
        BlockState spreadTargetBlockstate = level.getBlockState(spreadTargetPosition);
        FungalTransformationData transformationData = spreadTargetBlockstate.getBlockHolder().getData(ModDataMaps.FUNGAL_SPREAD_TRANSFORM);

        if (!gameruleEnabled(level)) return;
        if (transformDataNull(transformationData)) return;

        // Once we've confirmed we have the data, we set the block state here so that the conditions afterward can modify it.
        // Ignore the linter warning here, if we have transform data, then we have at least one entry in the targets list.
        BlockState targetState = transformationData.targets().getRandomValue(random).get().defaultBlockState();

        if (!spreadAlikeCheckPass(targetState, spreaderState, transformationData)) return;
        if (!biomeFailCheckPassNoRNG(level, spreadTargetPosition)) return;
        if (!propagationCheckPass(level, targetState, spreadTargetPosition, transformationData)) return;
        if (!myceliumCheckPass(level, spreadTargetPosition, transformationData)) return;
        if (!airExposureCheckPass(level, spreadTargetPosition, transformationData)) return;

        targetState = modifyTargetState(level, targetState, spreadTargetPosition, spreaderState, transformationData, random);

        // Finally, set & update the block to its transformed variant.
        level.setBlockAndUpdate(spreadTargetPosition, targetState);
    }

    // Check that fungal spread gamerule is enabled
    private static boolean gameruleEnabled(ServerLevel level) {
        return level.getGameRules().getBoolean(ModGameRules.DO_FUNGAL_SPREAD);
    }

    // Check that FungalTransformationData isn't null
    private static boolean transformDataNull(FungalTransformationData transformData) {
        return transformData == null;
    }

    // Spread alike check verifies the target block and the spreading block are the same.
    private static boolean spreadAlikeCheckPass(BlockState targetState, BlockState spreaderState, FungalTransformationData transformationData) {
        if (transformationData.spreadAlikeCheck()) {
            return (targetState.getBlock().equals(spreaderState.getBlock()));
        } else {
            return true;
        }
    }

    // Simple chance based fail check
    private static boolean failCheckPass(RandomSource random, FungalTransformationData transformationData) {
        return !(transformationData.failChance() >= random.nextFloat());
    }

    // Biome dependent based fail check
    private static boolean biomeFailCheckPass(ServerLevel level, BlockPos spreadTargetPosition, RandomSource random) {
        if (level.getBiome(spreadTargetPosition).getData(ModDataMaps.BIOME_FUNGAL_SPREAD) == null) {
            return true;
        }

        // Ignore linter, we already checked that BiomeFungalSpread isn't null.
        return (level.getBiome(spreadTargetPosition).getData(ModDataMaps.BIOME_FUNGAL_SPREAD).spreadSuccessChance() > random.nextFloat());

    }

    // Modified version of the above that only checks the chance for success is more than 0
    private static boolean biomeFailCheckPassNoRNG(ServerLevel level, BlockPos spreadTargetPosition) {
        if (level.getBiome(spreadTargetPosition).getData(ModDataMaps.BIOME_FUNGAL_SPREAD) == null) {
            return true;
        }

        // Ignore linter, we already checked that BiomeFungalSpread isn't null.
        return (level.getBiome(spreadTargetPosition).getData(ModDataMaps.BIOME_FUNGAL_SPREAD).spreadSuccessChance() > 0);

    }

    // The propagation check makes sure the target block is capable of being grass & isn't underwater.
    private static boolean propagationCheckPass(ServerLevel level, BlockState targetState, BlockPos spreadTargetPosition, FungalTransformationData transformationData) {
        if (transformationData.propagationCheck()) {
            return (SpreadingSnowyDirtBlockInvoker.invokeCanPropagate(targetState, level, spreadTargetPosition));
        } else {
            return true;
        }
    }

    // The mycelium check has us check underneath the target block for mycelium, used by default so mushrooms don't pop-off
    private static boolean myceliumCheckPass(ServerLevel level, BlockPos spreadTargetPosition, FungalTransformationData transformationData) {
        if (transformationData.myceliumCheck()) {
            return (level.getBlockState(spreadTargetPosition.below()).is(Blocks.MYCELIUM));
        } else {
            return true;
        }
    }

    // The air exposure check sees if any side of the target block is exposed to air, used so the infection doesn't irrevocably taint the world to the core.
    private static boolean airExposureCheckPass(ServerLevel level, BlockPos spreadTargetPosition, FungalTransformationData transformationData) {
        if (transformationData.airExposureCheck()) {
            boolean airExposed = false;
            for (Direction d : Direction.values()) {
                if (level.getBlockState(spreadTargetPosition.relative(d)).getBlock() instanceof AirBlock) {
                    airExposed = true;
                    break; // small early break optimization when spreading
                }
            }
            return airExposed;
        } else {
            return true;
        }
    }

    // Any changes to our target state are done here.
    private static BlockState modifyTargetState(ServerLevel level, BlockState oldTargetState, BlockPos spreadTargetPosition, BlockState spreaderState, FungalTransformationData transformationData, RandomSource random) {
        BlockState newState = oldTargetState;

        // The stone variant check uses the source block to determine which mycostone variant we should spread.
        if (newState.is(ModTagKeys.Blocks.MYCOSTONES) && transformationData.mycostoneVariantCheck()) {
            if(spreaderState.is(ModTagKeys.Blocks.RED_FUNGUS)) {
                newState = ModBlocks.RED_MYCOSTONE.get().defaultBlockState();
            } else if (spreaderState.is(ModTagKeys.Blocks.BROWN_FUNGUS)) {
                newState = ModBlocks.BROWN_MYCOSTONE.get().defaultBlockState();
            } else {
                newState = ModBlocks.MIXED_MYCOSTONE.get().defaultBlockState();
            }
        }

        // The consumption check has a chance of replacing it with air instead,
        // to evoke the feeling of the fungus consuming the block entirely.
        if (transformationData.consumeChance() > random.nextFloat()) {
            newState = Blocks.AIR.defaultBlockState();
        }

        // Additional check for snowy blocks (grasses) to check their snow status upon spread.
        if (newState.getBlock() instanceof SnowyDirtBlock) {
            newState = newState.setValue(BlockStateProperties.SNOWY, level.getBlockState(spreadTargetPosition.above()).is(Blocks.SNOW));
        }

        return newState;
    }
}
