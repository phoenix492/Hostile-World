package net.phoenix492.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class EnvironmentalInfectionBuildupData {
    private int buildupQuantity;

    public static final Codec<EnvironmentalInfectionBuildupData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("buildupQuantity").forGetter(EnvironmentalInfectionBuildupData::getBuildupQuantity)
    ).apply(instance, EnvironmentalInfectionBuildupData::new));

    private EnvironmentalInfectionBuildupData(int n) {
        setBuildupQuantity(n);
    }

    public EnvironmentalInfectionBuildupData() {
        this.buildupQuantity = 0;
    }

    public int getBuildupQuantity() {
        return buildupQuantity;
    }

    public EnvironmentalInfectionBuildupData setBuildupQuantity(int buildupQuantity) {
        this.buildupQuantity = buildupQuantity;
        return this;
    }
}
