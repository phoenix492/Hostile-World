package net.phoenix492.registration;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.phoenix492.effect.FungicideEffect;
import net.phoenix492.effect.FungusInfectionEffect;
import net.phoenix492.hostileworld.HostileWorld;

public class ModEffects {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, HostileWorld.MODID);

    public static final Holder<MobEffect> FUNGICIDE_EFFECT = MOB_EFFECTS.register(
        "fungicide",
        () -> new FungicideEffect(MobEffectCategory.BENEFICIAL, 0xcc99c3)
    );
    public static final Holder<MobEffect> FUNGUS_INFECTION_EFFECT = MOB_EFFECTS.register(
        "fungus_infection",
        () -> new FungusInfectionEffect(MobEffectCategory.HARMFUL, 0x915487)
    );

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
