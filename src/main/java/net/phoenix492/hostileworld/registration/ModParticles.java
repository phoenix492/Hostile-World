package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, HostileWorld.MODID);

    public static final Supplier<SimpleParticleType> SPORE_PARTICLES = PARTICLE_TYPES.register(
        "spore_particles",
        () -> new SimpleParticleType(false)
    );

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
