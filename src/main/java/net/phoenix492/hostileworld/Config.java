package net.phoenix492.hostileworld;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_BIOME_BUILDUP = BUILDER
        .comment(" Amount of infection units per tick to build up on players in biomes tagged with hostileworld:builds_fungal_infection")
        .defineInRange("fungalInfectionBiomeBuildup", 3, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_DROPOFF = BUILDER
        .comment(" Amount of infection units to take away from players each tick.", " Applied even when players are in infectious biomes, so effectively subtracts 1 from the above number.")
        .defineInRange("fungalInfectionDropoff", 1, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_MINIMUM = BUILDER
        .comment(" Minimum value for infection buildup on a player.")
        .defineInRange("fungalInfectionMinimum", 0, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_MAXIMUM = BUILDER
        .comment(" Maximum value for infection buildup on a player.")
        .defineInRange("fungalInfectionMaximum", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
    static final ModConfigSpec SPEC = BUILDER.build();

}
