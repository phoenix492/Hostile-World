package net.phoenix492.hostileworld.mixin;

import net.phoenix492.hostileworld.config.HostileWorldConfig;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Aquifer.NoiseBasedAquifer.class)
public abstract class NoAquiferInFungalCavernsMixin {
    @Unique
    private DensityFunction hostileworld$continents;


    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hostileworld$assignContinents(NoiseChunk noiseChunk, ChunkPos chunkPos, NoiseRouter noiseRouter, PositionalRandomFactory positionalRandomFactory, int minY, int height, Aquifer.FluidPicker globalFluidPicker, CallbackInfo ci) {
        hostileworld$continents = noiseRouter.continents();
    }

    @WrapOperation(
        method = "computeSurfaceLevel",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;isDeepDarkRegion(Lnet/minecraft/world/level/levelgen/DensityFunction;Lnet/minecraft/world/level/levelgen/DensityFunction;Lnet/minecraft/world/level/levelgen/DensityFunction$FunctionContext;)Z")
    )
    private boolean removeAquifersFromFungalCaverns(DensityFunction erosionFunction, DensityFunction depthFunction, DensityFunction.FunctionContext functionContext, Operation<Boolean> original) {
        if (HostileWorldConfig.SERVER_CONFIG.blockAquifersInFungalCaverns.get()) {
            return original.call(erosionFunction, depthFunction, functionContext) || hostileworld$isFungalCaverns(depthFunction, hostileworld$continents, functionContext);
        } else {
            return original.call(erosionFunction, depthFunction, functionContext);
        }
    }

    @Unique
    private static boolean hostileworld$isFungalCaverns(DensityFunction depthFunction, DensityFunction continentsFunction, DensityFunction.FunctionContext functionContext) {
        double continents = continentsFunction.compute(functionContext);
        return (depthFunction.compute(functionContext) > 0.1) && ((continents > -1.2) && (continents < -1.05));
    }
}
