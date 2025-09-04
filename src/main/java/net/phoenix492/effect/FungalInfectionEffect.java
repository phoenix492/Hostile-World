package net.phoenix492.effect;

import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FungalInfectionEffect extends MobEffect {

    public static final Int2DoubleFunction DAMAGE_REDUCTION_AMPLIFIER_MAP = key -> switch (key) {
        case 0 -> -1d;
        case 1 -> -2d;
        case 2 -> -4d;
        case 3 -> -8d;
        default -> -16d;
    };

    public static final Int2DoubleFunction SPEED_REDUCTION_AMPLIFIER_MAP = key -> switch (key) {
        case 0 -> 0d;
        case 1 -> -0.05d;
        case 2 -> -0.10d;
        case 3 -> -0.25d;
        case 4 -> -0.50d;
        default -> -0.95d;
    };

    public FungalInfectionEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
