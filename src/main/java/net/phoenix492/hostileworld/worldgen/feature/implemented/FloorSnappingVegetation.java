package net.phoenix492.hostileworld.worldgen.feature.implemented;

import net.phoenix492.hostileworld.worldgen.feature.abstracted.SurfaceSnappingFeature;
import net.phoenix492.hostileworld.worldgen.feature.configurations.FloorSnappingVegetationConfiguration;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.EnumSet;

public class FloorSnappingVegetation extends SurfaceSnappingFeature<FloorSnappingVegetationConfiguration> {
    public FloorSnappingVegetation(Codec<FloorSnappingVegetationConfiguration> codec) {
        super(codec, EnumSet.of(Direction.DOWN));
    }

    @Override
    public boolean place(FeaturePlaceContext<FloorSnappingVegetationConfiguration> context) {
        FloorSnappingVegetationConfiguration config = context.config();
        SurfaceContext surfaceContext = findWall(context.level(), context.origin(), config.validFloorTargets());
        WorldGenLevel level = context.level();
        if (surfaceContext instanceof SurfaceContext) {
            BlockPos placeTarget = surfaceContext.surfacePos().above();
            if (level.getBlockState(placeTarget).isAir()) {
                level.setBlock(placeTarget, config.block().getState(level.getRandom(), placeTarget), 3);
                return true;
            }
        }
        return false;
    }
}
