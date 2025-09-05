package net.phoenix492.hostileworld;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    // TODO: Move this from a config to a datamap
    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_BIOME_BUILDUP = SERVER_BUILDER
        .comment(" Amount of infection units per tick to build up on players in biomes tagged with hostileworld:builds_fungal_infection")
        .defineInRange("fungalInfectionBiomeBuildup", 3, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_DROPOFF = SERVER_BUILDER
        .comment(" Amount of infection units to take away from players each tick.", " Always applied, even when players are in infectious biomes.")
        .defineInRange("fungalInfectionDropoff", 1, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_MINIMUM = SERVER_BUILDER
        .comment(" Minimum value for infection buildup on a player.")
        .defineInRange("fungalInfectionMinimum", 0, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_MAXIMUM = SERVER_BUILDER
        .comment(" Maximum value for infection buildup on a player.")
        .defineInRange("fungalInfectionMaximum", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGICIDE_REDUCTION_PER_LEVEL = SERVER_BUILDER
        .comment(" Amount of infection buildup reduced per level of Fungicide effect applied.")
        .defineInRange("fungicideReductionPerLevel", 10000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue FUNGICIDE_DAMAGE_PER_LEVEL = SERVER_BUILDER
        .comment(" Amount of damage dealt per level of Fungicide effect applied.")
        .defineInRange("fungicideDamagePerLevel", 2.0, 0.0, Double.MAX_VALUE);

    // This goes last. Don't forget.
    static final ModConfigSpec SERVER_SPEC = SERVER_BUILDER.build();
}
