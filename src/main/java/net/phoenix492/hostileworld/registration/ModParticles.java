package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.particle.FungalCavernsBreathingParticle;
import net.phoenix492.hostileworld.particle.SporeDropperParticle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, HostileWorld.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPORE_DROPPER_PARTICLE = PARTICLE_TYPES.register(
        "spore_dropper_particle",
        () -> new SimpleParticleType(false)
    );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FUNGAL_CAVERNS_BREATHING_PARTICLE = PARTICLE_TYPES.register(
        "fungal_caverns_breathing_particle",
        () -> new SimpleParticleType(false)
    );

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    @EventBusSubscriber(Dist.CLIENT)
    public static final class ModParticleFactories {
        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.SPORE_DROPPER_PARTICLE.get(), SporeDropperParticle.Provider::new);
            event.registerSpriteSet(ModParticles.FUNGAL_CAVERNS_BREATHING_PARTICLE.get(), FungalCavernsBreathingParticle.Provider::new);
        }
    }
}
