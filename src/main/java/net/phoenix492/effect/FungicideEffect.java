package net.phoenix492.effect;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.registration.ModDataAttachments;

public class FungicideEffect extends InstantenousMobEffect {
    public FungicideEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // Reduce infection buildup by configurable amount per effect level.
        // Amplifier starts at zero, so adding one simplifies the "per level" calculations.
        entity.getData(ModDataAttachments.FUNGAL_INFECTION).reduceInfectionLevel((long) (amplifier + 1) * Config.FUNGICIDE_REDUCTION_PER_LEVEL.getAsInt());

        // Deal (Configurable)*Level damage to the player in exchange for pruning the infection.
        entity.hurt(entity.damageSources().magic(), Config.FUNGICIDE_DAMAGE_PER_LEVEL.get().floatValue() * (amplifier+1));

        return true;
    }
}
