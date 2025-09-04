package net.phoenix492.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * Cancellable, in which case this player's fungal infection buildup won't decrease.
 */
public class FungalInfectionDropoffEvent extends Event implements ICancellableEvent {
    private final ServerPlayer player;

    public FungalInfectionDropoffEvent(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

}
