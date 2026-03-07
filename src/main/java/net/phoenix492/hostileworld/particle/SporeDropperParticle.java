package net.phoenix492.hostileworld.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import org.jetbrains.annotations.Nullable;

public class SporeDropperParticle extends TextureSheetParticle {
    private static final int FADEOUT_LENGTH = 40;
    int counter = 0;
    float x_movement_direction;
    float z_movement_direction;
    float oscillationFactor;
    int fadeoutTimer;

    protected SporeDropperParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        RandomSource random = level.getRandom();

        // Random float between -1 and 1
        this.x_movement_direction = (random.nextFloat() * 2) - 1;
        // Apply pythagorean and multiply by either -1 or 1 to snap it to the top/bottom half of a "circle"
        this.z_movement_direction = Mth.sqrt(1 - Mth.square(x_movement_direction)) * (random.nextInt(1) * 2 - 1);

        this.oscillationFactor = 0.01F;
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
        counter += 6;
        if (!this.onGround) {
            this.xd = Math.sin(Math.toRadians(counter)) * x_movement_direction * this.oscillationFactor;
            this.zd = Math.sin(Math.toRadians(counter)) * z_movement_direction * this.oscillationFactor;
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

    public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {

        @Override
            public @Nullable Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                return new SporeDropperParticle(level, x, y, z, spriteSet, xSpeed, ySpeed, zSpeed);
            }
        }
}
