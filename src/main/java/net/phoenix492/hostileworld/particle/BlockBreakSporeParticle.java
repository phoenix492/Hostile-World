package net.phoenix492.hostileworld.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

import org.jetbrains.annotations.Nullable;

public class BlockBreakSporeParticle extends TextureSheetParticle {
    private final int fadeoutMod;

    protected BlockBreakSporeParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.lifetime = 40;
        this.gravity = 0.06f;
        this.friction = 0.95f;
        this.quadSize = 0.03f;
        this.rCol = 0.6f;
        this.gCol = 0.5f;
        this.bCol = 0.6f;
        this.yd = ((random.nextFloat()*2 - 1) / 15) + 0.06f;
        this.xd = (random.nextFloat()*2 - 1) / 12;
        this.zd = (random.nextFloat()*2 - 1) / 12;
        this.fadeoutMod = random.nextInt(21);
        setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        if (this.age > (lifetime - (10 + fadeoutMod))) {
            this.alpha -= 0.05f;
        }
        if (this.alpha <= 0) {
            this.remove();
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
            return new BlockBreakSporeParticle(level, x, y, z, spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}
