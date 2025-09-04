package net.phoenix492.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

/**
 * Fired at the end of {@link net.phoenix492.eventhandler.FungusInfectionHandler}'s infection buildup method.
 */
public class FungusInfectionPlayerTickEvent extends Event {
    private final ServerPlayer player;

    public FungusInfectionPlayerTickEvent(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

}
