package net.phoenix492.hostileworld.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

import org.jetbrains.annotations.Nullable;

public class FungalCavernsBreathingParticle extends TextureSheetParticle {
    private final int weight;

    protected FungalCavernsBreathingParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.setSpriteFromAge(spriteSet);
        this.alpha = 1f;
        this.hasPhysics = true;
        this.weight = level.getRandom().nextInt(10) + 1;
        this.gravity = 0.01f * weight;
        this.lifetime = 600;
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.xd = 0;
        this.zd = 0;
        this.yd = 0;
    }

    @Override
    public void tick() {
        int cycleTime = (int) (level.getGameTime() % 200);
        if (cycleTime < 5 || this.onGround) {
            this.yd = 0;
        }
        else if (cycleTime <= 50) {
            this.yd = 0.1f/weight;
        }

        super.tick();

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {
        @Override
            public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                return new FungalCavernsBreathingParticle(level, x, y, z, spriteSet, xSpeed, ySpeed, zSpeed);
            }
        }
}
