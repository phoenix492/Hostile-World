package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = HostileWorld.MODID)
public class ModPotions {
    public static final DeferredRegister<Potion> MOD_POTIONS = DeferredRegister.create(Registries.POTION, HostileWorld.MODID);

    public static final Holder<Potion> FUNGICIDE = MOD_POTIONS.register(
        "fungicide",
        () -> new Potion(new MobEffectInstance(ModEffects.FUNGICIDE_EFFECT, 0, 0, false, true, false))
    );

    public static final Supplier<Potion> STRONG_FUNGICIDE = MOD_POTIONS.register(
        "strong_fungicide",
        () -> new Potion(new MobEffectInstance(ModEffects.FUNGICIDE_EFFECT, 0, 1, false, true, false))
    );

    public static void register(IEventBus eventBus) {
        MOD_POTIONS.register(eventBus);
    }

    @SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        final PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(
            Potions.AWKWARD,
            ModItems.IMMUNITE_CLUSTER.get(),
            FUNGICIDE
        );
    }
}