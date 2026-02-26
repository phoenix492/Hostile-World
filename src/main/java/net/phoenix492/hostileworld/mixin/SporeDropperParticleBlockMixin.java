package net.phoenix492.hostileworld.mixin;

import net.phoenix492.hostileworld.registration.ModParticles;
import net.phoenix492.hostileworld.util.ModTagKeys;

import net.minecraft.core.BlockPos;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class SporeDropperParticleBlockMixin {
    @Inject(at = @At("HEAD"), method = "animateTick")
    protected void hostileworld$onAnimateTickSporeDropperParticles(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (((Block)(Object)this).defaultBlockState().is(ModTagKeys.Blocks.DROPS_SPORES) && level.getBlockState(pos.below()).isAir()) {
            ParticleUtils.spawnParticleBelow(level, pos, level.getRandom(), ModParticles.SPORE_DROPPER_PARTICLE.get());
        }
    }
}
