package net.phoenix492.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/**
 * Cancellable, in which the fungal infection effect won't be applied for this player.
 */
public class FungalInfectionApplyEffectEvent extends Event implements ICancellableEvent {
    private final ServerPlayer player;

    public FungalInfectionApplyEffectEvent(ServerPlayer player) {
        this.player = player;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

}
