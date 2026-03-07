package net.phoenix492.hostileworld.worldgen.feature.abstracted;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public abstract class SurfaceSnappingFeature<FC extends FeatureConfiguration> extends Feature<FC> {
    public static final int WALL_FIND_RADIUS = 16;
    private final EnumSet<Direction> snapDirections;
    protected record SurfaceContext(BlockPos surfacePos, Direction surfaceDirection) {}

    public SurfaceSnappingFeature(Codec<FC> codec, EnumSet<Direction> snapDirections) {
        super(codec);
        this.snapDirections = snapDirections;
    }

    protected final SurfaceContext findWall(WorldGenLevel levelAccessor, BlockPos blockPos, HolderSet<Block> validSurfaceTargets) {
        BlockPos.MutableBlockPos wallSearcher = new BlockPos.MutableBlockPos().set(blockPos);
        List<Direction> scanDirections = new ArrayList<>(snapDirections);

        for (int i = 1; i < WALL_FIND_RADIUS; i++) {
            for (Direction d : scanDirections) {
                wallSearcher.move(d, i);
                if (!levelAccessor.getBlockState(wallSearcher).canBeReplaced()) {
                    if (validSurfaceTargets.contains(levelAccessor.getBlockState(wallSearcher).getBlockHolder())) {
                        return new SurfaceContext(wallSearcher, d);
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
}
