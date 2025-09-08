package net.phoenix492.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

/**
 * Object containing data for what <i>target</i> block a given <i>source</i> block is transformed into upon being chosen for fungal spread.
 * @param target What this block should be transformed into.
 * @param failChance The chance the fungal spread will fail and terminate early without spreading. Can be used to "slow down" spread to individual blocks.
 * @param consumeChance The chance that instead of being replaced by target, the source block will instead be replaced by air.
 * @param myceliumCheck Whether to check for the presence of mycelium below the source block before transformation. Used by mushrooms to avoid popoff.
 * @param propagationCheck Whether to perform the checks used for grass spread. Used so grass type blocks like mycelium don't spread to places they aren't supported (like to dirt underneath blocks)
 */
public record FungalTransformationData(Block target, float failChance, float consumeChance, boolean myceliumCheck, boolean propagationCheck) {
    public static final Codec<FungalTransformationData> CODEC = RecordCodecBuilder.create(instance-> instance.group(
        BuiltInRegistries.BLOCK.byNameCodec().fieldOf("target").forGetter(FungalTransformationData::target),
        Codec.floatRange(0, 1).optionalFieldOf("failChance", 0f).forGetter(FungalTransformationData::failChance),
        Codec.floatRange(0, 1).optionalFieldOf("consumeChance", 0f).forGetter(FungalTransformationData::consumeChance),
        Codec.BOOL.optionalFieldOf("myceliumCheck", false).forGetter(FungalTransformationData::myceliumCheck),
        Codec.BOOL.optionalFieldOf("propagationCheck", false).forGetter(FungalTransformationData::propagationCheck)
    ).apply(instance, FungalTransformationData::new));

    public FungalTransformationData(Block target) {
        this(target, 0, 0, false, false);
    }

    public FungalTransformationData(Block target, float failChance) {
        this(target, failChance, 0, false, false);
    }

    public FungalTransformationData(Block target, float failChance, float consumeChance) {
        this(target, failChance, consumeChance, false, false);
    }

    public FungalTransformationData(Block target, float failChance, float consumeChance, boolean myceliumCheck) {
        this(target, failChance, consumeChance, myceliumCheck, false);
    }
}
