package net.phoenix492.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.registration.ModDataAttachments;
import org.jetbrains.annotations.Nullable;

public class FungicideEffect extends InstantenousMobEffect {
    public FungicideEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
        if (livingEntity.hasData(ModDataAttachments.FUNGUS_INFECTION_BUILDUP)) {
            livingEntity.setData(
                ModDataAttachments.FUNGUS_INFECTION_BUILDUP,
                livingEntity.getData(ModDataAttachments.FUNGUS_INFECTION_BUILDUP) - (amplifier * Config.FUNGICIDE_REDUCTION_PER_LEVEL.getAsInt())
            );
        }
    }
}
