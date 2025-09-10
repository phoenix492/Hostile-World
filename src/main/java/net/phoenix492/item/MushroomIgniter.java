package net.phoenix492.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.phoenix492.block.MushroomFireBlock;

public class MushroomIgniter extends FlintAndSteelItem {
    public MushroomIgniter(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockPos clickedFacePos = blockpos.relative(context.getClickedFace());
        if (MushroomFireBlock.canBePlacedAt(level, clickedFacePos)) {
            level.playSound(player, clickedFacePos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            BlockState mushFireToPlace = MushroomFireBlock.getState(level, clickedFacePos);
            level.setBlock(clickedFacePos, mushFireToPlace, 11);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, blockpos);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        else {
            return InteractionResult.FAIL;
        }
    }
}
