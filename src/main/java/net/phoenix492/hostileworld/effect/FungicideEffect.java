package net.phoenix492.hostileworld.effect;

import net.phoenix492.hostileworld.config.HostileWorldConfig;
import net.phoenix492.hostileworld.registration.ModDataAttachments;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FungicideEffect extends InstantenousMobEffect {
    public FungicideEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // Reduce infection buildup by configurable amount per effect level.
        // Amplifier starts at zero, so adding one simplifies the "per level" calculations.
        entity.getData(ModDataAttachments.FUNGAL_INFECTION).reduceInfectionLevel((long) (amplifier + 1) * HostileWorldConfig.SERVER_CONFIG.fungicideReductionPerLevel.get());

        // Deal (Configurable)*Level damage to the player in exchange for pruning the infection.
        entity.hurt(entity.damageSources().magic(), HostileWorldConfig.SERVER_CONFIG.fungicideDamagePerLevel.get().floatValue() * (amplifier+1));

        return true;
    }
}
