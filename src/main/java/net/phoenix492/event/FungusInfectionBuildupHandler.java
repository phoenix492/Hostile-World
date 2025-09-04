package net.phoenix492.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.phoenix492.hostileworld.HostileWorld;

import java.util.List;

/**
 * Event class that uses FUNGUS_INFECTION_BUILDUP data attachment to track how long a player has been in an infectious biome.
 */
@EventBusSubscriber(modid = HostileWorld.MODID)
public class FungusInfectionBuildupHandler {

    @SubscribeEvent
    public static void mushroomInfectionTracker(ServerTickEvent event) {
        List<ServerPlayer> playerList = event.getServer().getPlayerList().getPlayers();
        playerList.forEach(serverPlayer -> {
            //if (serverPlayer.blockPosition().
        });
    }
}
