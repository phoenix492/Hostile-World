package net.phoenix492.hostileworld.worldgen.feature;

import net.phoenix492.hostileworld.worldgen.feature.configurations.FlatTopShelfFungusConfiguration;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import it.unimi.dsi.fastutil.Pair;

// TODO: Add special behavior for HugeMushroomBlocks that makes them disable sides correctly.
public class FlatTopShelfFungus extends Feature<FlatTopShelfFungusConfiguration> {
    private static final int WALL_FIND_RADIUS = 16;
    private record WallContext(BlockPos wallPos, Direction wallDirection, List<Pair<Integer, Integer>> validPlacements) {}

    private static class ShelfFungusPlacer {
        private final BlockPos.MutableBlockPos cursor;
        private final WorldGenLevel level;
        private final BlockPos origin;
        private final Direction away;
        private final Direction wall;
        private final Direction ccw;
        private final Direction cw;
        private BlockPos mark;

        public ShelfFungusPlacer(WorldGenLevel level, BlockPos origin, Direction wall) {
            this.level = level;
            this.cursor = origin.mutable();
            this.origin = origin.immutable();
            this.mark = origin.immutable();
            this.wall = wall;
            this.away = wall.getOpposite();
            this.ccw = wall.getCounterClockWise();
            this.cw = wall.getClockWise();
        }

        public ShelfFungusPlacer away() {
            cursor.move(away);
            return this;
        }

        public ShelfFungusPlacer away(int i) {
            cursor.move(away, i);
            return this;
        }

        public ShelfFungusPlacer in() {
            cursor.move(wall);
            return this;
        }

        public ShelfFungusPlacer in(int i) {
            cursor.move(wall, i);
            return this;
        }

        public ShelfFungusPlacer ccw() {
            cursor.move(ccw);
            return this;
        }

        public ShelfFungusPlacer ccw(int i) {
            cursor.move(ccw, i);
            return this;
        }

        public ShelfFungusPlacer cw() {
            cursor.move(cw);
            return this;
        }

        public ShelfFungusPlacer cw(int i) {
            cursor.move(cw, i);
            return this;
        }

        public ShelfFungusPlacer up() {
            cursor.move(Direction.UP);
            return this;
        }

        public ShelfFungusPlacer up(int i) {
            cursor.move(Direction.UP, i);
            return this;
        }

        public ShelfFungusPlacer down() {
            cursor.move(Direction.DOWN);
            return this;
        }

        public ShelfFungusPlacer down(int i) {
            cursor.move(Direction.DOWN, i);
            return this;
        }

        public ShelfFungusPlacer move(Direction d) {
            cursor.move(d);
            return this;
        }

        public ShelfFungusPlacer move(Direction d, int i) {
            cursor.move(d, i);
            return this;
        }

        public void place(BlockState state) {
            level.setBlock(cursor, state, 3);
        }

        public void place(Block block) {
            level.setBlock(cursor, block.defaultBlockState(), 3);
        }

        public BlockPos placeAndLog(BlockState state) {
            level.setBlock(cursor, state, 3);
            return cursor.immutable();
        }

        public BlockPos placeAndLog(Block block) {
            level.setBlock(cursor, block.defaultBlockState(), 3);
            return cursor.immutable();
        }

        public BlockState getBlock() {
            return level.getBlockState(cursor);
        }

        public BlockPos getPos() {
            return this.cursor.immutable();
        }

        public void mark() {
            this.mark = cursor.immutable();
        }

        public void recall() {
            this.cursor.set(mark);
        }

        public void moveTo(BlockPos pos) {
            this.cursor.set(pos);
        }
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
        RandomSource random = context.random();
        FlatTopShelfFungusConfiguration shelfFungusConfiguration = context.config();

        final BlockState CAP_BLOCK = shelfFungusConfiguration.capBlock().defaultBlockState();
        final BlockState STEM_BLOCK = shelfFungusConfiguration.stemBlock().defaultBlockState();
        final List<Block> validWallTargets = shelfFungusConfiguration.validWallTargets();
        final int WIDTH = shelfFungusConfiguration.width();
        final int OUTWARD_LENGTH = shelfFungusConfiguration.outwardCapLength();
        final int INWARD_LENGTH = shelfFungusConfiguration.inwardCapLength();

        WallContext wallContext = findWall(worldgenlevel, blockpos, validWallTargets, shelfFungusConfiguration);
        if (wallContext == null) {
            return false;
        }

        // Pick a random set of stem dimensions from all that are valid.
        Pair<Integer, Integer> stemDimensions = wallContext.validPlacements().get(random.nextInt(wallContext.validPlacements().size()));

        List<BlockPos> horizontalCapCheck = new ArrayList<>();
        ShelfFungusPlacer placer = new ShelfFungusPlacer(worldgenlevel, wallContext.wallPos, wallContext.wallDirection);

        // Stem placement
        for (int i = 0; i < stemDimensions.left(); i ++) {
            placer.away();
            placer.place(STEM_BLOCK);
        }
        for (int i = 0; i < stemDimensions.right(); i ++) {
            placer.up();
            placer.place(STEM_BLOCK);
        }

        // Initial cap center block
        placer.up();
        BlockPos capCenter = placer.placeAndLog(CAP_BLOCK);
        horizontalCapCheck.add(capCenter);

        // CCW and CW mushroom placements, storing which we have to check backwards on.
        placer.mark();
        placer.ccw();
        for (int i = 1; placer.getBlock().canBeReplaced() && i <= WIDTH; i++) {
            horizontalCapCheck.add(placer.placeAndLog(CAP_BLOCK));
            placer.ccw();
        }
        placer.recall();

        placer.cw();
        for (int i = 1; placer.getBlock().canBeReplaced() && i <= WIDTH; i++) {
            horizontalCapCheck.add(placer.placeAndLog(CAP_BLOCK));
            placer.cw();
        }

        for (BlockPos pos : horizontalCapCheck) {
            // First the outward blocks
            placer.moveTo(pos);
            placer.mark();
            placer.away();
            for (int i = 1; placer.getBlock().canBeReplaced() && i <= OUTWARD_LENGTH; i++) {
                placer.place(CAP_BLOCK);
                placer.away();
            }

            // Then the inward blocks.
            placer.recall();
            placer.in();
            for (int i = 1; placer.getBlock().canBeReplaced() && i <= INWARD_LENGTH; i++) {
                placer.place(CAP_BLOCK);
                placer.in();
            }
        }

        // Now it's time to go around placing the ring.
        // ...Unless we're not supposed to.
        if (!shelfFungusConfiguration.generateRim()) {
            return true;
        }

        // Assume NOTHING about how the cap generated.
        placer.moveTo(capCenter);

        // Move to *outside* the away edge of the cap
        while(worldgenlevel.getBlockState(placer.getPos()).is(CAP_BLOCK.getBlock())) {
            placer.away();
        }

        // Move around the outside of the cap placing cap blocks below us.
        BlockPos rimStart = placer.getPos();
        Direction travelDirection = placer.ccw;
        Direction capDirection = placer.wall;
        List<BlockPos.MutableBlockPos> ringPlacements = new ArrayList<>();
        int counter = 0;
        boolean cornerFlag = false;

        do {
            // First flag the block below us as part of the ring as long as this isn't a corner when we're not supposed to place them
            if (
                worldgenlevel.getBlockState(placer.getPos().below()).canBeReplaced() &&
                (!cornerFlag || shelfFungusConfiguration.generateRimCorners())
            ) {
                placer.down();
                ringPlacements.add(placer.getPos().mutable());
                placer.up();
            }

            // If the last iteration flags to us that we're on a clockwise corner, we need to turn.
            if (cornerFlag) {
                travelDirection = travelDirection.getClockWise();
                capDirection = capDirection.getClockWise();
                cornerFlag = false;
            }

            // If the way forward is part of the cap, this obviously means we're now facing INTO the cap (which should be clockwise of us.)
            if (worldgenlevel.getBlockState(placer.getPos().relative(travelDirection)).is(CAP_BLOCK.getBlock())) {
                // Turn anticlockwise until we're not facing the cap anymore. It's now clockwise of us again.
                while(worldgenlevel.getBlockState(placer.getPos().relative(travelDirection)).is(CAP_BLOCK.getBlock())) {
                    travelDirection = travelDirection.getCounterClockWise();
                    capDirection = capDirection.getCounterClockWise();
                }
            }
            // Now, if there's a mushroom block forward and clockwise of us
            // (at this point we've already verified we're not facing the cap and placed the ring below),
            // that means there should be another ordinary straight line spot to place the ring ahead and below of us, so we can loop as normal.
            // Otherwise, this must mean we've hit a corner that turns clockwise, so we need to let the next iteration know it should handle that after placement!
            if (!worldgenlevel.getBlockState(placer.getPos().relative(travelDirection).relative(capDirection)).is(CAP_BLOCK.getBlock())) {
                cornerFlag = true;
            }
            placer.move(travelDirection);

            // Failsafe counter.
            counter++;
        } while (!placer.getPos().equals(rimStart) && counter < 99);

        // Now we go through and place the ring up to the config defined depth.
        for (BlockPos.MutableBlockPos pos : ringPlacements) {
            for (int i = 0; i < shelfFungusConfiguration.rimDepth(); i++ ) {
                if (worldgenlevel.getBlockState(pos).canBeReplaced()) {
                    worldgenlevel.setBlock(pos, CAP_BLOCK, 3);
                    pos.move(Direction.DOWN);
                }
                else {
                    break;
                }
            }
        }

        return true;
    }

}
