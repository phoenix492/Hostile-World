package net.phoenix492.data.attachment;

import net.phoenix492.hostileworld.Config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// TODO: Migrate to capability
public class EntityFungalInfectionData {
    private long infectionLevel;

    public static final Codec<EntityFungalInfectionData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.LONG.fieldOf("infectionLevel").forGetter(EntityFungalInfectionData::getInfectionLevel)
        ).apply(instance, EntityFungalInfectionData::new)
    );

    // Private constructor for deserialization
    private EntityFungalInfectionData(long initialLevel) {
        this.infectionLevel = initialLevel;
    }

    public EntityFungalInfectionData() {
        this.infectionLevel = 0;
    }

    public EntityFungalInfectionData setInfectionLevel(long value) {
        this.infectionLevel = value;
        this.clampInfection();
        return this;
    }

    public long getInfectionLevel() {
        return this.infectionLevel;
    }

    /**
     * Reduce infection level directly. Does not apply scaling according to infection tick frequency, for that use the tick scaled method.
     * @param value
     * @return This object
     */
    public EntityFungalInfectionData reduceInfectionLevel(long value) {
        this.infectionLevel -= value;
        this.clampInfection();
        return this;
    }

    /**
     * Increase infection level directly. Does not apply scaling according to infection tick frequency, for that use the tick scaled method.
     * @param value
     * @return This object
     */
    public EntityFungalInfectionData increaseInfectionLevel(long value) {
        this.infectionLevel += value;
        this.clampInfection();
        return this;
    }

    /**
     * Reduce infection level. Applies scaling based on infection tick frequency.
     * @param value
     * @return This object
     */
    public EntityFungalInfectionData tickScaledReduceInfectionLevel(long value) {
        this.infectionLevel -= value * Config.FUNGAL_INFECTION_PLAYER_TICK_FREQUENCY.get();
        this.clampInfection();
        return this;
    }

    /**
     * Increase infection level. Applies scaling based on infection tick frequency.
     * @param value
     * @return This object
     */
    public EntityFungalInfectionData tickScaledIncreaseInfectionLevel(long value) {
        this.infectionLevel += value * Config.FUNGAL_INFECTION_PLAYER_TICK_FREQUENCY.get();
        this.clampInfection();
        return this;
    }

    /**
     * Ensures infection level lies between configuration defined values.
     */
    private void clampInfection() {
        this.infectionLevel = Math.clamp(this.infectionLevel, Config.FUNGAL_INFECTION_MINIMUM.getAsInt(), Config.FUNGAL_INFECTION_MAXIMUM.getAsInt());
    }
}
