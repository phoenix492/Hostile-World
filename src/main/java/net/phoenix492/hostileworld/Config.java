package net.phoenix492.hostileworld;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_PLAYER_TICK_FREQUENCY = SERVER_BUILDER
        .comment(" How often to tick fungal infection tracking for players. Value provided is once every N ticks, meaning higher values are less frequent. A value of one will check players every tick (20 times per second).", "Lower values are more taxing on performance but more accurate. Higher values create a delay in tracking but are gentler on performance.")
        .defineInRange("fungalInfectionPlayerTickFrequency", 5, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_ENTITY_TICK_FREQUENCY = SERVER_BUILDER
        .comment(" How often to tick fungal infection tracking for non-player entities. Same rules as above, but should be less frequent as there are far more entities than players.")
        .defineInRange("fungalInfectionEntityTickFrequency", 20, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_UNIVERSAL_DROPOFF = SERVER_BUILDER
        .comment(" Amount of infection units to take away from players each tick.", " Always applied, even when entities are actively receiving infection.")
        .defineInRange("fungalInfectionDropoff", 1, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_MINIMUM = SERVER_BUILDER
        .comment(" Minimum value for infection buildup.")
        .defineInRange("fungalInfectionMinimum", 0, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_INFECTION_MAXIMUM = SERVER_BUILDER
        .comment(" Maximum value for infection buildup.")
        .defineInRange("fungalInfectionMaximum", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGICIDE_REDUCTION_PER_LEVEL = SERVER_BUILDER
        .comment(" Amount of infection buildup reduced per level of Fungicide effect applied.")
        .defineInRange("fungicideReductionPerLevel", 10000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue FUNGICIDE_DAMAGE_PER_LEVEL = SERVER_BUILDER
        .comment(" Amount of damage dealt per level of Fungicide effect applied.")
        .defineInRange("fungicideDamagePerLevel", 2.0, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.IntValue FUNGAL_SPREAD_SPEED = SERVER_BUILDER
        .comment(" Amount of times fungus spreading blocks will check a nearby block each time one receives a random tick. Vanilla grass uses 4. High values will cause increasing amounts of lag due to running additional checks.")
        .defineInRange("fungalSpreadSpeed", 4, 0, Integer.MAX_VALUE);

    // This goes last. Don't forget.
    static final ModConfigSpec SERVER_SPEC = SERVER_BUILDER.build();
}
