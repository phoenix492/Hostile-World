package net.phoenix492.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * Cancellable, in which fungal infection won't be ticked for this player.
 */
public class FungalInfectionPlayerTickEvent extends Event implements ICancellableEvent {
    private final ServerPlayer player;

    public FungalInfectionPlayerTickEvent(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

}
