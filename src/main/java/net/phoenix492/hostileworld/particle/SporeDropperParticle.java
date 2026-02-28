package net.phoenix492.hostileworld.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

import org.jetbrains.annotations.Nullable;

public class SporeDropperParticle extends TextureSheetParticle {
    private static final int TICK_TIMER_CAP = 360;
    private static final int FADEOUT_LENGTH = 40;
    int counter = 0;
    int x_movement_direction;
    int z_movement_direction;
    int fadeoutTimer;

    protected SporeDropperParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        RandomSource random = level.getRandom();
        this.x_movement_direction = random.nextInt(-1, 1);
        this.z_movement_direction = random.nextInt(-1, 1);
        this.gravity = 0.04f - (random.nextFloat() / 50);
        this.lifetime = 200;
        this.fadeoutTimer = 0;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.hasPhysics = true;
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.setSpriteFromAge(spriteSet);
        this.quadSize = 0.01f;
    }

    @Override
    public void tick() {
        if (counter < TICK_TIMER_CAP) {
            counter += 6;
        } else {
            counter = 0;
        }
        if (!this.onGround) {
            this.xd = Math.sin(Math.toRadians(counter)) * x_movement_direction * 0.01D;
            this.zd = Math.sin(Math.toRadians(counter)) * z_movement_direction * 0.01D;
        }

        if (this.onGround || this.age >= this.lifetime - FADEOUT_LENGTH) {
            fadeoutTimer++;
        }

        if (Math.abs(yd) > 0.1d) {
            yd = 0.5d * (yd) / (Math.abs(yd));
        }

        alpha = ((float) (FADEOUT_LENGTH - fadeoutTimer) / FADEOUT_LENGTH);
        if (alpha == 0) {
            remove();
        }

        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SporeDropperParticle(level, x, y, z, spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}
