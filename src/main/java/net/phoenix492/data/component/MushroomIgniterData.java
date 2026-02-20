package net.phoenix492.data.component;

import net.phoenix492.block.MycofireBlock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

public record MushroomIgniterData(MycofireBlock.MycofireStrength strength) {
    public static final Codec<MushroomIgniterData> IGNITER_DATA_CODEC = RecordCodecBuilder.create(instance ->
       instance.group(
           StringRepresentable.fromValues(MycofireBlock.MycofireStrength::values).fieldOf("strength").forGetter(MushroomIgniterData::strength)
       ).apply(instance, MushroomIgniterData::new)
    );
}
