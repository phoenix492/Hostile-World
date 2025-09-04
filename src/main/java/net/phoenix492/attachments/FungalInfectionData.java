package net.phoenix492.attachments;

import net.phoenix492.hostileworld.Config;

// TODO: Add Codec for serialization. Class is currently non-serializable and thus non-persistent.
// TODO: Migrate to capability
public class FungalInfectionData {
    long infectionLevel;

    public FungalInfectionData() {
        this.infectionLevel = 0;
    }

    public FungalInfectionData setInfectionLevel(long value) {
        this.infectionLevel = value;
        this.clampInfection();
        return this;
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

    public long getInfectionLevel() {
        return this.infectionLevel;
    }

    /**
     * Ensures infection level lies between configuration defined values.
     */
    private void clampInfection() {
        this.infectionLevel = Math.clamp(this.infectionLevel, Config.FUNGAL_INFECTION_MINIMUM.getAsInt(), Config.FUNGAL_INFECTION_MAXIMUM.getAsInt());
    }
}
