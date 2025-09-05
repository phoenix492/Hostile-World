package net.phoenix492.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.phoenix492.hostileworld.Config;

// TODO: Migrate to capability
public class FungalInfectionData {
    private long infectionLevel;

    public static final Codec<FungalInfectionData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.LONG.fieldOf("infectionLevel").forGetter(FungalInfectionData::getInfectionLevel)
        ).apply(instance, FungalInfectionData::new)
    );

    // Private constructor for deserialization
    private FungalInfectionData(long initialLevel) {
        this.infectionLevel = initialLevel;
    }

    public FungalInfectionData() {
        this.infectionLevel = 0;
    }

    public FungalInfectionData setInfectionLevel(long value) {
        this.infectionLevel = value;
        this.clampInfection();
        return this;
    }

    public long getInfectionLevel() {
        return this.infectionLevel;
    }

    public FungalInfectionData reduceInfectionLevel(long value) {
        this.infectionLevel -= value;
        this.clampInfection();
        return this;
    }

    public FungalInfectionData increaseInfectionLevel(long value) {
        this.infectionLevel += value;
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
