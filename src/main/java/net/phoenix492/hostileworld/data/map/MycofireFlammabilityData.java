package net.phoenix492.hostileworld.data.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public record MycofireFlammabilityData(int encouragement, int flammability, Block burnoutTarget) {
    public static final Codec<MycofireFlammabilityData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("encouragement").forGetter(MycofireFlammabilityData::encouragement),
        Codec.INT.fieldOf("flammability").forGetter(MycofireFlammabilityData::encouragement),
        BuiltInRegistries.BLOCK.byNameCodec().fieldOf("burnoutTarget").forGetter(MycofireFlammabilityData::burnoutTarget)
    ).apply(instance, MycofireFlammabilityData::new));

    public static MycofireFlammabilityData.MycofireFlammabilityDataBuilder builder() {
        return new MycofireFlammabilityData.MycofireFlammabilityDataBuilder();
    }

    public static class MycofireFlammabilityDataBuilder {
        private int encouragement = 1;
        private int flammability = 1;
        private Block burnoutTarget = Blocks.AIR;


        public MycofireFlammabilityData.MycofireFlammabilityDataBuilder encouragement(int encouragement) {
            this.encouragement = encouragement;
            return this;
        }
        public MycofireFlammabilityData.MycofireFlammabilityDataBuilder flammability(int flammability) {
            this.flammability = flammability;
            return this;
        }
        public MycofireFlammabilityData.MycofireFlammabilityDataBuilder burnoutTarget(Block burnoutTarget) {
            this.burnoutTarget = burnoutTarget;
            return this;
        }

        public MycofireFlammabilityData build() {
            validate();
            return new MycofireFlammabilityData(
                encouragement,
                flammability,
                burnoutTarget
            );
        }

        private void validate() {}
    }
}
