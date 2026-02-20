package net.phoenix492.hostileworld.data.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Block;

/**
 * Object containing data for what <i>target</i> block a given <i>source</i> block is transformed into upon being chosen for fungal spread.
 * @param targets What this block should be transformed into. A weighted list
 * @param failChance The chance the fungal spread will fail and terminate early without spreading. Can be used to "slow down" spread to individual blocks.
 * @param consumeChance The chance that instead of being replaced by target, the source block will instead be replaced by air.
 * @param myceliumCheck Whether to check for the presence of mycelium below the source block before transformation. Used by mushrooms to avoid popoff.
 * @param propagationCheck Whether to perform the checks used for grass spread. Used so grass type blocks like mycelium don't spread to places they aren't supported (like to dirt underneath blocks)
 * @param airExposureCheck Whether to check if the block is exposed to air on at least one side. Used by Mycostone conversions to stop it from penetrating deep into the world.
 * @param mycostoneVariantCheck Special check used by regular stone to see which variant of Mycostone they should transform into, based on if the block spreading fungus to it is red, brown, or other fungus. Only works with Mycostones.
 * @param spreadAlikeCheck Requires the target block to be the same as the block that originally attempted to spread. Use by deepslate so only Mycorestone is "strong" enough to spread to it.
 */
public record FungalTransformationData(SimpleWeightedRandomList<Block> targets, float failChance, float consumeChance, boolean myceliumCheck, boolean propagationCheck, boolean airExposureCheck, boolean mycostoneVariantCheck, boolean spreadAlikeCheck) {
    public static final Codec<FungalTransformationData> CODEC = RecordCodecBuilder.create(instance-> instance.group(
        SimpleWeightedRandomList.wrappedCodec(BuiltInRegistries.BLOCK.byNameCodec()).fieldOf("targets").forGetter(FungalTransformationData::targets),
        Codec.floatRange(0, 1).optionalFieldOf("failChance", 0f).forGetter(FungalTransformationData::failChance),
        Codec.floatRange(0, 1).optionalFieldOf("consumeChance", 0f).forGetter(FungalTransformationData::consumeChance),
        Codec.BOOL.optionalFieldOf("myceliumCheck", false).forGetter(FungalTransformationData::myceliumCheck),
        Codec.BOOL.optionalFieldOf("propagationCheck", false).forGetter(FungalTransformationData::propagationCheck),
        Codec.BOOL.optionalFieldOf("airExposureCheck", false).forGetter(FungalTransformationData::airExposureCheck),
        Codec.BOOL.optionalFieldOf("mycostoneVariantCheck", false).forGetter(FungalTransformationData::mycostoneVariantCheck),
        Codec.BOOL.optionalFieldOf("spreadAlikeCheck", false).forGetter(FungalTransformationData::spreadAlikeCheck)
    ).apply(instance, FungalTransformationData::new));

    public static FungalTransformationDataBuilder builder() {
        return new FungalTransformationDataBuilder();
    }

    public static class FungalTransformationDataBuilder {
        private final SimpleWeightedRandomList.Builder<Block> targetListBuilder = SimpleWeightedRandomList.builder();
        private float failChance = 0f;
        private float consumeChance = 0f;
        private boolean myceliumCheck = false;
        private boolean propagationCheck = false;
        private boolean airExposureCheck = false;
        private boolean mycostoneVariantCheck = false;
        private boolean spreadAlikeCheck = false;

        public FungalTransformationDataBuilder failChance(float failChance) {
            this.failChance = failChance;
            return this;
        }

        public FungalTransformationDataBuilder consumeChance(float consumeChance) {
            this.consumeChance = consumeChance;
            return this;
        }

        public FungalTransformationDataBuilder myceliumCheck() {
            this.myceliumCheck = true;
            return this;
        }

        public FungalTransformationDataBuilder propagationCheck() {
            this.propagationCheck = true;
            return this;
        }

        public FungalTransformationDataBuilder airExposureCheck() {
            this.airExposureCheck = true;
            return this;
        }

        public FungalTransformationDataBuilder mycostoneVariantCheck() {
            this.mycostoneVariantCheck = true;
            return this;
        }

        public FungalTransformationDataBuilder spreadAlikeCheck() {
            this.spreadAlikeCheck = true;
            return this;
        }

        public FungalTransformationDataBuilder addTarget(Block b) {
            this.targetListBuilder.add(b);
            return this;
        }

        public FungalTransformationDataBuilder addTarget(Block b, int weight) {
            this.targetListBuilder.add(b, weight);
            return this;
        }

        public FungalTransformationData build() {
            validate();
            return new FungalTransformationData(
                targetListBuilder.build(),
                failChance,
                consumeChance,
                myceliumCheck,
                propagationCheck,
                airExposureCheck,
                mycostoneVariantCheck,
                spreadAlikeCheck
            );
        }

        private void validate() {
            if (targetListBuilder.build().isEmpty()) {
                throw new IllegalStateException("Build called on FungalTransformationDataBuilder with no target blocks!");
            }
        }
    }
}
