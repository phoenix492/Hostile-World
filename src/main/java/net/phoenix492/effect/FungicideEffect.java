package net.phoenix492.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.phoenix492.eventhandler.FungalInfectionHandler;
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
                livingEntity.getData(ModDataAttachments.FUNGAL_INFECTION_BUILDUP) - (amplifier + 1) * Config.FUNGICIDE_REDUCTION_PER_LEVEL.getAsInt()
            );
            FungalInfectionHandler.clampInfection(livingEntity);
        }

        // Deal (Configurable)*Level damage to the player in exchange for pruning the infection.
        livingEntity.hurt(livingEntity.damageSources().magic(), Config.FUNGICIDE_DAMAGE_PER_LEVEL.get().floatValue() * (amplifier+1));
    }
}
