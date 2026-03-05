package net.phoenix492.hostileworld.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import org.jetbrains.annotations.Nullable;

public class FungalCavernsBreathingParticle extends TextureSheetParticle {
    private final int weight;
    private final float trueGravity;
    private final ResourceKey<Biome> startingBiome;

    protected FungalCavernsBreathingParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.setSpriteFromAge(spriteSet);
        this.alpha = 1f;
        this.hasPhysics = false;
        this.weight = level.getRandom().nextInt(4) + 4;
        this.gravity = 0.008f * weight;
        this.trueGravity = gravity;
        this.lifetime = 600;
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.xd = 0;
        this.zd = 0;
        this.yd = 0;
        this.quadSize = 0.01f + (level.getRandom().nextFloat() / 100);
        this.startingBiome = level.getBiome(new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z))).getKey();
    }

    @Override
    public void tick() {
        if (!(level.getBiome(new BlockPos((int) Math.floor(this.x), (int) Math.floor(this.y), (int) Math.floor(this.z))).is(startingBiome))) {
            this.remove();
            return;
        }

        int cycleTime = (int) (level.getGameTime() % 400);
        if (cycleTime == 0 || this.onGround) {
            this.yd = 0;
            this.gravity = trueGravity;
        }
        else if (cycleTime <= 25) {
            this.yd += 0.005f;
            this.yd *= 1f + (0.001f/weight);
        }
        else if (cycleTime <= 75) {
            this.yd += 0.008f;
            this.yd *= 1f + (0.01f/weight);
        }
        else if (cycleTime >= 360) {
            this.gravity = 0;
            this.yd /= 1.05F;
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
