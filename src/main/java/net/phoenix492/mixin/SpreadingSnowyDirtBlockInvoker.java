package net.phoenix492.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpreadingSnowyDirtBlock.class)
public interface SpreadingSnowyDirtBlockInvoker {
    @Invoker("canPropagate")
    static boolean invokeCanPropagate(BlockState state, LevelReader levelReader, BlockPos pos) {
        throw new AssertionError();
    }

    @Invoker("canBeGrass")
    static boolean invokeCanBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
       throw new AssertionError();
    }
}
