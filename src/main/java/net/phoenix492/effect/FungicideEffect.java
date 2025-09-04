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
        // Reduce infection buildup by configurable amount per effect level.
        // Amplifier starts at zero, so adding one simplifies the "per level" calculations.
        if (livingEntity.hasData(ModDataAttachments.FUNGAL_INFECTION_BUILDUP)) {
            livingEntity.setData(
                ModDataAttachments.FUNGAL_INFECTION_BUILDUP,
                Math.clamp(
                    livingEntity.getData(ModDataAttachments.FUNGAL_INFECTION_BUILDUP) - (long) (amplifier + 1) * Config.FUNGICIDE_REDUCTION_PER_LEVEL.getAsInt(),
                    Config.FUNGAL_INFECTION_MINIMUM.get(),
                    Config.FUNGAL_INFECTION_MAXIMUM.get()
                )
            );
        }

        // Deal (Configurable)*Level damage to the player in exchange for pruning the infection.
        livingEntity.hurt(livingEntity.damageSources().magic(), Config.FUNGICIDE_DAMAGE_PER_LEVEL.get().floatValue() * (amplifier+1));
    }
}
