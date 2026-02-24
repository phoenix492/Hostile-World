package net.phoenix492.hostileworld.registration;

import net.minecraft.world.level.GameRules;

public class ModGameRules {

    public static GameRules.Key<GameRules.BooleanValue> DO_FUNGAL_SPREAD;

    public static void register() {
        DO_FUNGAL_SPREAD = GameRules.register(
            "doFungalSpread",
            GameRules.Category.UPDATES,
            GameRules.BooleanValue.create(true)
        );
    }
}
