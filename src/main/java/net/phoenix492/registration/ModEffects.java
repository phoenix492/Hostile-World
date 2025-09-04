package net.phoenix492.registration;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.phoenix492.effect.FungicideEffect;
import net.phoenix492.effect.FungalInfectionEffect;
import net.phoenix492.hostileworld.HostileWorld;

public class ModEffects {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, HostileWorld.MODID);

    public static final Holder<MobEffect> FUNGICIDE_EFFECT = MOB_EFFECTS.register(
        "fungicide",
        () -> new FungicideEffect(MobEffectCategory.BENEFICIAL, 0xcc99c3)
    );

    public static final Holder<MobEffect> FUNGUS_INFECTION_EFFECT = MOB_EFFECTS.register(
        "fungal_infection",
        () -> new FungalInfectionEffect(MobEffectCategory.HARMFUL, 0x915487)
            .addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "fungal_infection_damage_reduction"),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                FungalInfectionEffect.DAMAGE_REDUCTION_AMPLIFIER_MAP
            )
            .addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "fungal_infection_speed_reduction"),
                AttributeModifier.Operation.ADD_VALUE,
                FungalInfectionEffect.SPEED_REDUCTION_AMPLIFIER_MAP
            )
    );

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
