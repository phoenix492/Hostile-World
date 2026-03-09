package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.registration.ModParticles;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class ModParticleDescriptionProvider extends ParticleDescriptionProvider {

    protected ModParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper);
    }

    @Override
    protected void addDescriptions() {
        spriteSet(
            ModParticles.SPORE_DROPPER_PARTICLE.get(),
            ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "spore_dropper")
        );
        spriteSet(
            ModParticles.FUNGAL_CAVERNS_BREATHING_PARTICLE.get(),
            ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "fungal_caverns_breathing_particle")
        );
        spriteSet(
            ModParticles.BLOCK_BREAK_SPORE_PARTICLE.get(),
            ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "block_break_spore")
        );
    }
}
