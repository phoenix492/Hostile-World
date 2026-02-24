package net.phoenix492.hostileworld.worldgen.feature;

import net.phoenix492.hostileworld.worldgen.feature.configurations.FlatShelfFungusConfiguration;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import it.unimi.dsi.fastutil.Pair;

public class FlatShelfFungus extends Feature<FlatShelfFungusConfiguration> {
    public final int WALL_FIND_RADIUS = 16;
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

    public FlatShelfFungus(Codec<FlatShelfFungusConfiguration> codec) {
        super(codec);
    }

    private boolean areaIsClear(WorldGenLevel levelAccessor, AABB area) {
        for (BlockState state : levelAccessor.getBlockStates(area).toList()) {
            if (!state.isAir()) {
                return false;
            }
        }
        return true;
    }

    private List<Pair<Integer, Integer>> validPlacements(WorldGenLevel levelAccessor, BlockPos blockPos, Direction wall, FlatShelfFungusConfiguration configuration) {
        Direction away = wall.getOpposite();
        Direction ccw = wall.getCounterClockWise();
        Direction cw = wall.getClockWise();

        BlockPos corner1 = blockPos.mutable().move(cw, 1).move(away, 1).immutable();
        BlockPos corner2 = blockPos.mutable().move(ccw, 2).move(wall, 2).immutable();

        List<Pair<Integer, Integer>> validLocations = new ArrayList<>();

        // Create an Axis Aligned Bounding Box for checking a 3x1x3 area for space above where the stem terminates, the heuristic I've gone with.
        // TODO: THIS SECTION OF CODE DOES NOT WORK!!!!!! FIGURE OUT WHY!!!!!
        AABB checkedArea = AABB.of(BoundingBox.fromCorners(corner1, corner2));
        for (int horiOffset = configuration.minStemLength(); horiOffset <= configuration.maxStemLength(); horiOffset++) {
            for (int vertiOffset = configuration.minStemHeight(); vertiOffset <= configuration.maxStemHeight(); vertiOffset++) {
                if (!levelAccessor.getBlockState(blockPos.mutable().move(away, horiOffset).move(Direction.UP, vertiOffset)).isAir()) {
                    break;
                }
                if (areaIsClear(levelAccessor, checkedArea.move(blockPos.mutable().move(away, horiOffset)).move(blockPos.mutable().move(Direction.UP, vertiOffset)))) {
                    validLocations.add(Pair.of(horiOffset, vertiOffset));
                }
            }
        }

        return validLocations;
    }

    private WallContext findWall(WorldGenLevel levelAccessor, BlockPos blockPos, List<Block> validWallTargets, FlatShelfFungusConfiguration configuration) {
        BlockPos.MutableBlockPos wallSearcher = new BlockPos.MutableBlockPos().set(blockPos);
        List<Direction> scanDirections = Direction.Plane.HORIZONTAL.stream().collect(Collectors.toList());

        for (int i = 1; i < WALL_FIND_RADIUS; i++) {
            for (Direction d : scanDirections) {
                wallSearcher.move(d, i);
                if (!levelAccessor.getBlockState(wallSearcher).isAir()) {
                    if (validWallTargets.contains(levelAccessor.getBlockState(wallSearcher).getBlock())) {
                        List<Pair<Integer, Integer>> validPlacements = validPlacements(levelAccessor, wallSearcher, d, configuration);
                        if (!validPlacements.isEmpty()) {
                            return new WallContext(wallSearcher, d, validPlacements);
                        }
                        // No valid placements here, this direction is no good! remove it from checked directions.
                        else {
                            scanDirections.remove(d);
                        }
                    }
                    // Not the right block, this direction is no good! Remove it from checked directions.
                    else {
                        scanDirections.remove(d);
                    }
                }
                wallSearcher.move(d.getOpposite(), i);
            }
        }
        return null;
    }

    @Override
    public boolean place(FeaturePlaceContext<FlatShelfFungusConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        RandomSource random = context.random();
        FlatShelfFungusConfiguration shelfFungusConfiguration = context.config();

        final BlockState CAP_BLOCK = Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState();
        final BlockState STEM_BLOCK = Blocks.MUSHROOM_STEM.defaultBlockState();
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
        for (int i = 1; placer.getBlock().isAir() && i <= WIDTH; i++) {
            horizontalCapCheck.add(placer.placeAndLog(CAP_BLOCK));
            placer.ccw();
        }
        placer.recall();

        placer.cw();
        for (int i = 1; placer.getBlock().isAir() && i <= WIDTH; i++) {
            horizontalCapCheck.add(placer.placeAndLog(CAP_BLOCK));
            placer.cw();
        }

        for (BlockPos pos : horizontalCapCheck) {
            // First the outward blocks
            placer.moveTo(pos);
            placer.mark();
            placer.away();
            for (int i = 1; placer.getBlock().isAir() && i <= OUTWARD_LENGTH; i++) {
                placer.place(CAP_BLOCK);
                placer.away();
            }

            // Then the inward blocks.
            placer.recall();
            placer.in();
            for (int i = 1; placer.getBlock().isAir() && i <= INWARD_LENGTH; i++) {
                placer.place(CAP_BLOCK);
                placer.in();
            }
        }

        // Now it's time to go around placing the ring.
        placer.moveTo(capCenter);

        // First move to outside the CW-AWAY corner and then move 1 down.
        placer.cw(WIDTH+1);
        placer.away(OUTWARD_LENGTH+1);
        placer.down();

        // Then just go around the outside placing the ring.
        // TODO: Make this check the mushroom block for sane places to place the ring instead of assuming everywhere is fine.
        for (int i = 0; i < OUTWARD_LENGTH + INWARD_LENGTH + 2; i++) {
            if (placer.getBlock().isAir()) {
                placer.place(CAP_BLOCK);
            }
            placer.in();
        }

        for (int i = 0; i < 2*WIDTH + 2; i++) {
            if (placer.getBlock().isAir()) {
                placer.place(CAP_BLOCK);
            }
            placer.ccw();
        }

        for (int i = 0; i < OUTWARD_LENGTH + INWARD_LENGTH + 2; i++) {
            if (placer.getBlock().isAir()) {
                placer.place(CAP_BLOCK);
            }
            placer.away();
        }

        for (int i = 0; i < 2*WIDTH + 2; i++) {
            if (placer.getBlock().isAir()) {
                placer.place(CAP_BLOCK);
            }
            placer.cw();
        }

        return true;
    }

}
