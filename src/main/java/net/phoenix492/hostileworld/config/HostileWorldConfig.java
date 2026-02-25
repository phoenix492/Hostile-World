package net.phoenix492.hostileworld.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import org.apache.commons.lang3.tuple.Pair;

public class HostileWorldConfig {
    public static final HostileWorldConfig SERVER_CONFIG;
    public static final ModConfigSpec SPEC;
    static {
        Pair<HostileWorldConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(HostileWorldConfig::new);
        SERVER_CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }

    // Fungal Infection
    public final ModConfigSpec.ConfigValue<Integer> fungalInfectionPlayerTickFrequency;
    public final ModConfigSpec.ConfigValue<Integer> fungalInfectionEntityTickFrequency;
    public final ModConfigSpec.ConfigValue<Integer> fungalInfectionUniversalDropoff;
    public final ModConfigSpec.ConfigValue<Integer> fungalInfectionSporeDropperBuildup;
    public final ModConfigSpec.ConfigValue<Integer> fungalInfectionMinimum;
    public final ModConfigSpec.ConfigValue<Integer> fungalInfectionMaximum;
    public final ModConfigSpec.ConfigValue<Integer> fungicideReductionPerLevel;
    public final ModConfigSpec.ConfigValue<Double> fungicideDamagePerLevel;

    // Fungal Spread
    public final ModConfigSpec.ConfigValue<Integer> fungalSpreadSpeed;
    public final ModConfigSpec.ConfigValue<Integer> sporeDropperRange;
    public final ModConfigSpec.ConfigValue<Boolean> spontaneousGermination;

    // Mycofire
    public final ModConfigSpec.ConfigValue<Integer> weakMaxAge;
    public final ModConfigSpec.ConfigValue<Integer> weakTickCooldownBase;
    public final ModConfigSpec.ConfigValue<Integer> weakTickCooldownRange;
    public final ModConfigSpec.ConfigValue<Double> weakBurnoutChanceMult;
    public final ModConfigSpec.ConfigValue<Double> weakSpreadChanceMult;

    public final ModConfigSpec.ConfigValue<Integer> normalMaxAge;
    public final ModConfigSpec.ConfigValue<Integer> normalTickCooldownBase;
    public final ModConfigSpec.ConfigValue<Integer> normalTickCooldownRange;
    public final ModConfigSpec.ConfigValue<Double> normalBurnoutChanceMult;
    public final ModConfigSpec.ConfigValue<Double> normalSpreadChanceMult;

    public final ModConfigSpec.ConfigValue<Integer> strongMaxAge;
    public final ModConfigSpec.ConfigValue<Integer> strongTickCooldownBase;
    public final ModConfigSpec.ConfigValue<Integer> strongTickCooldownRange;
    public final ModConfigSpec.ConfigValue<Double> strongBurnoutChanceMult;
    public final ModConfigSpec.ConfigValue<Double> strongSpreadChanceMult;

    public final ModConfigSpec.ConfigValue<Integer> apocalypticMaxAge;
    public final ModConfigSpec.ConfigValue<Integer> apocalypticTickCooldownBase;
    public final ModConfigSpec.ConfigValue<Integer> apocalypticTickCooldownRange;
    public final ModConfigSpec.ConfigValue<Double> apocalypticBurnoutChanceMult;
    public final ModConfigSpec.ConfigValue<Double> apocalypticSpreadChanceMult;

    // Worldgen
    public final ModConfigSpec.ConfigValue<Boolean> blockAquifersInFungalCaverns;


    private HostileWorldConfig(ModConfigSpec.Builder BUILDER) {
        BUILDER.push("Fungal Infection Settings");
            fungalInfectionPlayerTickFrequency = BUILDER
                .comment(" How often to tick fungal infection tracking for players. Value provided is once every N ticks, meaning higher values are less frequent. A value of one will check players every tick (20 times per second).", "Lower values are more taxing on performance but more accurate. Higher values create a delay in tracking but are gentler on performance.")
                .defineInRange("fungalInfectionPlayerTickFrequency", 5, 1, Integer.MAX_VALUE);
            fungalInfectionEntityTickFrequency = BUILDER
                .comment(" How often to tick fungal infection tracking for non-player entities. Same rules as above, but should be less frequent as there are far more entities than players.")
                .defineInRange("fungalInfectionEntityTickFrequency", 20, 1, Integer.MAX_VALUE);
            fungalInfectionUniversalDropoff = BUILDER
                .comment(" Amount of infection units to take away from players each tick.", " Always applied, even when entities are actively receiving infection.")
                .defineInRange("fungalInfectionUniversalDropoff", 1, 0, Integer.MAX_VALUE);
            fungalInfectionSporeDropperBuildup = BUILDER
                .comment(" Amount of infection units to give to players who are standing beneath designated \"Spore dropping\" blocks (by default red and brown mushroom blocks) each tick.")
                .defineInRange("fungalInfectionSporeDropperBuildup", 10, 0, Integer.MAX_VALUE);
            fungalInfectionMinimum = BUILDER
                .comment(" Minimum value for infection buildup.")
                .defineInRange("fungalInfectionMinimum", 0, 0, Integer.MAX_VALUE);
            fungalInfectionMaximum = BUILDER
                .comment(" Maximum value for infection buildup.")
                .defineInRange("fungalInfectionMaximum", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            fungicideReductionPerLevel = BUILDER
                .comment(" Amount of infection buildup reduced per level of Fungicide effect applied.")
                .defineInRange("fungicideReductionPerLevel", 10000, 1, Integer.MAX_VALUE);
            fungicideDamagePerLevel = BUILDER
                .comment(" Amount of damage dealt per level of Fungicide effect applied.")
                .defineInRange("fungicideDamagePerLevel", 2.0, 0.0, Double.MAX_VALUE);
        BUILDER.pop();

        BUILDER.push("Fungal Spread Settings");
            fungalSpreadSpeed = BUILDER
                .comment(" Amount of times fungus spreading blocks will check a nearby block each time one receives a random tick. Vanilla grass uses 4. High values will cause increasing amounts of lag due to running additional checks.")
                .defineInRange("fungalSpreadSpeed", 4, 0, Integer.MAX_VALUE);
            sporeDropperRange = BUILDER
                .comment(" How far (in blocks) that blocks tagged as \"spore dropping\" will affect the world below them.", " This includes things such as mushrooms quickly creating mycelium below them and entities receiving infection buildup while underneath spore droppers.", " High values may increase lag (untested, but default is safe).")
                .defineInRange("sporeDropperRange", 15, 0, 512);
            spontaneousGermination = BUILDER
                .comment(" Determines if biomes with a germinationChance defined in biome_fungal_spread.json should have a chance of Grass turning into Mycelium, to prevent fully pacifying these biomes.")
                .define("spontaneousGermination", true);
        BUILDER.pop();

        BUILDER.comment(
            """
                Settings for parameters of how each level of Mycofire spreads, as follows they are:
                maxAge: How old the fire is allowed to be. Effectively controls how long it lasts and how far it spreads.
                tickCooldownBase: Minimum time between fire updates in ticks (1/20ths of a second)
                tickCooldownRange: A random amount of ticks between 1 and this number is added to the previous number
                burnoutChanceMult: Multiplier for how likely the fire is to destroy/convert a block
                spreadChanceMult: Multiplier for how like the fire is to spread to another block"""
        ).push("Mycofire Settings");
            BUILDER.push("Weak");
                weakMaxAge = BUILDER.defineInRange("weakMaxAge", 5, 0, Integer.MAX_VALUE);
                weakTickCooldownBase = BUILDER.defineInRange("weakTickCooldownBase", 30, 1, Integer.MAX_VALUE);
                weakTickCooldownRange = BUILDER.defineInRange("weakTickCooldownRange", 20, 1, Integer.MAX_VALUE);
                weakBurnoutChanceMult = BUILDER.defineInRange("weakBurnoutChanceMult", 0.5, Double.MIN_VALUE, Double.MAX_VALUE);
                weakSpreadChanceMult = BUILDER.defineInRange("weakSpreadChanceMult", 0.5, Double.MIN_VALUE, Double.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("Normal");
                normalMaxAge = BUILDER.defineInRange("normalMaxAge", 15, 0, Integer.MAX_VALUE);
                normalTickCooldownBase = BUILDER.defineInRange("normalTickCooldownBase", 30, 1, Integer.MAX_VALUE);
                normalTickCooldownRange = BUILDER.defineInRange("normalTickCooldownRange", 10, 1, Integer.MAX_VALUE);
                normalBurnoutChanceMult = BUILDER.defineInRange("normalBurnoutChanceMult", 1, Double.MIN_VALUE, Double.MAX_VALUE);
                normalSpreadChanceMult = BUILDER.defineInRange("normalSpreadChanceMult", 1, Double.MIN_VALUE, Double.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("Strong");
                strongMaxAge = BUILDER.defineInRange("strongMaxAge", 20, 0, Integer.MAX_VALUE);
                strongTickCooldownBase = BUILDER.defineInRange("strongTickCooldownBase", 20, 1, Integer.MAX_VALUE);
                strongTickCooldownRange = BUILDER.defineInRange("strongTickCooldownRange", 10, 1, Integer.MAX_VALUE);
                strongBurnoutChanceMult = BUILDER.defineInRange("strongBurnoutChanceMult", 2, Double.MIN_VALUE, Double.MAX_VALUE);
                strongSpreadChanceMult = BUILDER.defineInRange("strongSpreadChanceMult", 2, Double.MIN_VALUE, Double.MAX_VALUE);
            BUILDER.pop();

            BUILDER.push("Apocalyptic");
                apocalypticMaxAge = BUILDER.defineInRange("apocalypticMaxAge", 0, 0, Integer.MAX_VALUE);
                apocalypticTickCooldownBase = BUILDER.defineInRange("apocalypticTickCooldownBase", 1, 1, Integer.MAX_VALUE);
                apocalypticTickCooldownRange = BUILDER.defineInRange("apocalypticTickCooldownRange", 1, 1, Integer.MAX_VALUE);
                apocalypticBurnoutChanceMult = BUILDER.defineInRange("apocalypticBurnoutChanceMult", 999, Double.MIN_VALUE, Double.MAX_VALUE);
                apocalypticSpreadChanceMult = BUILDER.defineInRange("apocalypticSpreadChanceMult", 999, Double.MIN_VALUE, Double.MAX_VALUE);
            BUILDER.pop();
        BUILDER.pop();

        BUILDER.push("Worldgen Settings");
            blockAquifersInFungalCaverns = BUILDER
                .comment("Whether to enable a hardcoded check that will block aquifers from spawning in the Fungal Caverns.",
                         "Uses the same technique vanilla uses to block them from the Deep Dark.",
                         "You likely only need to disable this if you have worldgen mods/datapacks that heavily affect the terrain and biome distribution,",
                         "AND also desire aquifers spawning in the following Noise value ranges:",
                         "Continentalness: -1.2 to -1.05",
                         "Depth: >0.1")
                .define("blockAquifersInFungalCaverns", true);
        BUILDER.pop();
    }
}
