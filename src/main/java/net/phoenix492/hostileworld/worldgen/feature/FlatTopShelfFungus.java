package net.phoenix492.hostileworld.worldgen.feature;

import net.phoenix492.hostileworld.worldgen.feature.configurations.FlatTopShelfFungusConfiguration;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import it.unimi.dsi.fastutil.Pair;

public class FlatTopShelfFungus extends Feature<FlatTopShelfFungusConfiguration> {
    private static final int WALL_FIND_RADIUS = 16;
    private record WallContext(BlockPos wallPos, Direction wallDirection, List<Pair<Integer, Integer>> validPlacements) {}


    private record ProtoFeatureBlock(BlockState blockState) {
            private static final ProtoFeatureBlock INSIDE_CAP_BLOCK = new ProtoFeatureBlock(Blocks.AIR.defaultBlockState());
    }

    public FlatTopShelfFungus(Codec<FlatTopShelfFungusConfiguration> codec) {
        super(codec);
    }

    private List<Pair<Integer, Integer>> validPlacements(WorldGenLevel levelAccessor, BlockPos blockPos, Direction wall, FlatTopShelfFungusConfiguration configuration) {
        Direction away = wall.getOpposite();
        Direction ccw = wall.getCounterClockWise();
        Direction cw = wall.getClockWise();

        BlockPos corner1 = blockPos.mutable().move(cw, 1).move(away, 1).immutable();
        BlockPos corner2 = blockPos.mutable().move(ccw, 2).move(wall, 2).immutable();

        List<Pair<Integer, Integer>> validLocations = new ArrayList<>();

        for (int horiOffset = configuration.minStemLength(); horiOffset <= configuration.maxStemLength(); horiOffset++) {
            for (int vertiOffset = configuration.minStemHeight(); vertiOffset <= configuration.maxStemHeight(); vertiOffset++) {
                Iterable<BlockPos> checkedBlocks = BlockPos.betweenClosed(
                    corner1.mutable().move(away, horiOffset).move(Direction.UP, vertiOffset),
                    corner2.mutable().move(away, horiOffset).move(Direction.UP, vertiOffset)
                );

                boolean validPos = true;
                for(BlockPos pos : checkedBlocks) {
                    if (!levelAccessor.getBlockState(pos).canBeReplaced()) {
                        validPos = false;
                        break;
                    }
                }

                if (validPos) {
                    validLocations.add(Pair.of(horiOffset, vertiOffset));
                }

            }
        }

        return validLocations;
    }

    private WallContext findWall(WorldGenLevel levelAccessor, BlockPos blockPos, List<Block> validWallTargets, FlatTopShelfFungusConfiguration configuration) {
        BlockPos.MutableBlockPos wallSearcher = new BlockPos.MutableBlockPos().set(blockPos);
        List<Direction> scanDirections = Direction.Plane.HORIZONTAL.stream().collect(Collectors.toList());

        for (int i = 1; i < WALL_FIND_RADIUS; i++) {
            for (Direction d : scanDirections) {
                wallSearcher.move(d, i);
                if (!levelAccessor.getBlockState(wallSearcher).canBeReplaced()) {
                    if (validWallTargets.contains(levelAccessor.getBlockState(wallSearcher).getBlock())) {
                        List<Pair<Integer, Integer>> validPlacements = validPlacements(levelAccessor, wallSearcher, d, configuration);
                        if (!validPlacements.isEmpty()) {
                            return new WallContext(wallSearcher, d, validPlacements);
                        }
                        // No valid placements here, this direction is no good! remove it from checked directions.
                        else {
                            scanDirections.remove(d);
                            wallSearcher.move(d.getOpposite(), i);
                            break;
                        }
                    }
                    // Not the right block, this direction is no good! Remove it from checked directions.
                    else {
                        scanDirections.remove(d);
                        wallSearcher.move(d.getOpposite(), i);
                        break;
                    }
                }
                wallSearcher.move(d.getOpposite(), i);
            }
        }
        return null;
    }

    @Override
    public boolean place(FeaturePlaceContext<FlatTopShelfFungusConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        if (!worldgenlevel.getBlockState(blockpos).isAir()) {
            return false;
        }

        RandomSource random = context.random();
        FlatTopShelfFungusConfiguration shelfFungusConfiguration = context.config();

        final BlockState CAP_BLOCK = shelfFungusConfiguration.capBlock().defaultBlockState();
        final BlockState STEM_BLOCK = shelfFungusConfiguration.stemBlock().defaultBlockState();
        final List<Block> validWallTargets = shelfFungusConfiguration.validWallTargets();
        final int WIDTH = shelfFungusConfiguration.width();
        final int OUTWARD_LENGTH = shelfFungusConfiguration.outwardCapLength();
        final int INWARD_LENGTH = shelfFungusConfiguration.inwardCapLength();
        final List<BlockPos> horizontalCapAnchor = new ArrayList<>();
        final Map<BlockPos, ProtoFeatureBlock> capBlocks = new HashMap<>();
        final Map<BlockPos, ProtoFeatureBlock> stemBlocks = new HashMap<>();
        WallContext wallContext = findWall(worldgenlevel, blockpos, validWallTargets, shelfFungusConfiguration);

        if (wallContext == null) {
            return false;
        }

        Direction in = wallContext.wallDirection();
        Direction away = in.getOpposite();
        Direction cw = in.getClockWise();
        Direction ccw = in.getCounterClockWise();
        BlockPos.MutableBlockPos cursor = wallContext.wallPos().mutable();

        // Pick a random set of stem dimensions from all that are valid.
        Pair<Integer, Integer> stemDimensions = wallContext.validPlacements().get(random.nextInt(wallContext.validPlacements().size()));

        // Stem placement
        for (int i = 0; i < stemDimensions.left(); i ++) {
            cursor.move(away);
            stemBlocks.put(cursor.immutable(), new ProtoFeatureBlock(STEM_BLOCK));
        }
        for (int i = 0; i < stemDimensions.right(); i ++) {
            cursor.move(Direction.UP);
            stemBlocks.put(cursor.immutable(), new ProtoFeatureBlock(STEM_BLOCK));
        }

        // Initial cap center block
        cursor.move(Direction.UP);
        BlockPos capCenter = cursor.immutable();
        capBlocks.put(capCenter, new ProtoFeatureBlock(CAP_BLOCK));
        horizontalCapAnchor.add(capCenter);

        // CCW and CW mushroom placements, storing which we have to check backwards on.
        // From now on we also place air protocapblocks in our map that we can check against for blockstate adjustment later.
        cursor.move(ccw);
        for (int i = 1; worldgenlevel.getBlockState(cursor).canBeReplaced() && i <= WIDTH; i++) {
            capBlocks.put(cursor.immutable(), new ProtoFeatureBlock(CAP_BLOCK));
            for (int j = 1; j <= shelfFungusConfiguration.rimDepth(); j++) {
                capBlocks.put(cursor.below(j), ProtoFeatureBlock.INSIDE_CAP_BLOCK);
            }
            horizontalCapAnchor.add(cursor.immutable());
            cursor.move(ccw);
        }

        cursor.set(capCenter);
        cursor.move(cw);
        for (int i = 1; worldgenlevel.getBlockState(cursor).canBeReplaced() && i <= WIDTH; i++) {
            capBlocks.put(cursor.immutable(), new ProtoFeatureBlock(CAP_BLOCK));
            for (int j = 1; j <= shelfFungusConfiguration.rimDepth(); j++) {
                capBlocks.put(cursor.below(j), ProtoFeatureBlock.INSIDE_CAP_BLOCK);
            }
            horizontalCapAnchor.add(cursor.immutable());
            cursor.move(cw);
        }

        for (BlockPos horizontalCapBlockPos : horizontalCapAnchor) {
            // First the outward blocks
            cursor.set(horizontalCapBlockPos);
            cursor.move(away);
            for (int i = 1; worldgenlevel.getBlockState(cursor).canBeReplaced() && i <= OUTWARD_LENGTH; i++) {
                capBlocks.put(cursor.immutable(), new ProtoFeatureBlock(CAP_BLOCK));
                for (int j = 1; j <= shelfFungusConfiguration.rimDepth(); j++) {
                    capBlocks.put(cursor.below(j), ProtoFeatureBlock.INSIDE_CAP_BLOCK);
                }
                cursor.move(away);
            }

            // Then the inward blocks.
            cursor.set(horizontalCapBlockPos);
            cursor.move(in);
            for (int i = 1; worldgenlevel.getBlockState(cursor).canBeReplaced() && i <= INWARD_LENGTH; i++) {
                capBlocks.put(cursor.immutable(), new ProtoFeatureBlock(CAP_BLOCK));
                for (int j = 1; j <= shelfFungusConfiguration.rimDepth(); j++) {
                    capBlocks.put(cursor.below(j), ProtoFeatureBlock.INSIDE_CAP_BLOCK);
                }
                cursor.move(in);
            }
        }

        // Now it's time to go around placing the ring.
        // ...Unless we're not supposed to.
        if (shelfFungusConfiguration.generateRim()) {
            // Assume NOTHING about how the cap generated.
            cursor.set(capCenter);

            // Move to *outside* the away edge of the cap
            while(capBlocks.get(cursor.immutable()) != null && capBlocks.get(cursor.immutable()).blockState().is(CAP_BLOCK.getBlock())) {
                cursor.move(away);
            }

            // Move around the outside of the cap placing cap blocks below us.
            BlockPos rimStart = cursor.immutable();
            Direction travelDirection = ccw;
            Direction capDirection = in;
            List<BlockPos.MutableBlockPos> ringPlacements = new ArrayList<>();
            int counter = 0;
            boolean cornerFlag = false;

            do {
                // First flag the block below us as part of the ring as long as this isn't a corner when we're not supposed to place them
                if (
                    worldgenlevel.getBlockState(cursor.below()).canBeReplaced() &&
                        (!cornerFlag || shelfFungusConfiguration.generateRimCorners())
                ) {
                    cursor.move(Direction.DOWN);
                    ringPlacements.add(cursor.mutable());
                    cursor.move(Direction.UP);
                }

                // If the last iteration flags to us that we're on a clockwise corner, we need to turn.
                if (cornerFlag) {
                    travelDirection = travelDirection.getClockWise();
                    capDirection = capDirection.getClockWise();
                    cornerFlag = false;
                }

                // If the way forward is part of the cap, this obviously means we're now facing INTO the cap (which should be clockwise of us.)
                // Turn until we're no longer facing part of the cap.
                // && capBlocks.get(blockpos.relative(travelDirection)).getBlockState().is(CAP_BLOCK.getBlock())
                while (capBlocks.get(blockpos.relative(travelDirection).immutable()) != null) {
                    travelDirection = travelDirection.getCounterClockWise();
                    capDirection = capDirection.getCounterClockWise();
                }
                // Now, if there's a mushroom block forward and clockwise of us
                // (at this point we've already verified we're not facing the cap and placed the ring below),
                // that means there should be another ordinary straight line spot to place the ring ahead and below of us, so we can loop as normal.
                // Otherwise, this must mean we've hit a corner that turns clockwise, so we need to let the next iteration know it should handle that after placement!
                // !capBlocks.get(cursor.relative(travelDirection).relative(capDirection)).getBlockState().is(CAP_BLOCK.getBlock())
                if (capBlocks.get(cursor.relative(travelDirection).relative(capDirection).immutable()) == null) {
                    cornerFlag = true;
                }
                cursor.move(travelDirection);

                // Failsafe counter.
                counter++;
            } while (!cursor.equals(rimStart) && counter < 99);

            // Now we go through and place the ring up to the config defined depth.
            for (BlockPos.MutableBlockPos pos : ringPlacements) {
                for (int i = 0; i < shelfFungusConfiguration.rimDepth(); i++ ) {
                    if (worldgenlevel.getBlockState(pos).canBeReplaced()) {
                        capBlocks.put(pos.immutable(), new ProtoFeatureBlock(CAP_BLOCK));
                        pos.move(Direction.DOWN);
                    }
                    else {
                        break;
                    }
                }
            }
        }

        // If these are HugeMushroomBlocks, we've gotta go through and adjust their side blockstates to make the cap look natural.
        if (CAP_BLOCK.getBlock() instanceof HugeMushroomBlock) {
            for (Map.Entry<BlockPos, ProtoFeatureBlock> entry : capBlocks.entrySet()) {
                BlockPos pos = entry.getKey();
                ProtoFeatureBlock block = entry.getValue();
                if(block == ProtoFeatureBlock.INSIDE_CAP_BLOCK) {
                    continue;
                }
                BlockState replacement = block.blockState();

                for (Direction d : Direction.values()) {
                    if (d == Direction.DOWN) {
                        replacement = replacement.setValue(PipeBlock.DOWN, false);
                        continue;
                    }
                    BlockPos checkedPos = pos.relative(d);
                    ProtoFeatureBlock checkedBlock = capBlocks.get(checkedPos);
                    if (checkedBlock instanceof ProtoFeatureBlock) {
                        replacement = replacement.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(d), false);
                    }
                }

                capBlocks.put(pos, new ProtoFeatureBlock(replacement));
            }
        }

        // Finally, go through every ProtoCapBlock position and place an ACTUAL block there in the world.
        for (Map.Entry<BlockPos, ProtoFeatureBlock> entry : stemBlocks.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue().blockState();
            if (state.is(STEM_BLOCK.getBlock())) {
                worldgenlevel.setBlock(pos, state, 3);
            }
        }
        for (Map.Entry<BlockPos, ProtoFeatureBlock> entry : capBlocks.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue().blockState();
            if (state.is(CAP_BLOCK.getBlock())) {
                worldgenlevel.setBlock(pos, state, 3);
            }
        }


        return true;
    }

}



