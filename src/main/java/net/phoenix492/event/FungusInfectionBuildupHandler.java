package net.phoenix492.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModDataAttachments;
import net.phoenix492.util.TagKeys;

import java.util.List;

/**
 * Event class that uses FUNGUS_INFECTION_BUILDUP data attachment to track how long a player has been in an infectious biome.
 */
@EventBusSubscriber(modid = HostileWorld.MODID)
public class FungusInfectionBuildupHandler {

    @SubscribeEvent
    public static void mushroomInfectionBuildup(ServerTickEvent.Pre event) {

        List<ServerPlayer> playerList = event.getServer().getPlayerList().getPlayers();
        playerList.forEach(serverPlayer -> {

            // Infection status of these players isn't tracked.
            if (serverPlayer.isSpectator() || serverPlayer.isCreative()) {
                return;
            }

            // Build-up infection on players in infectious biomes
            if (serverPlayer.level().getBiome(serverPlayer.blockPosition()).is(TagKeys.Biomes.BUILDS_FUNGAL_INFECTION)) {
                serverPlayer.setData(
                    ModDataAttachments.FUNGUS_INFECTION_BUILDUP,
                    serverPlayer.getData(ModDataAttachments.FUNGUS_INFECTION_BUILDUP) + Config.FUNGAL_INFECTION_BIOME_BUILDUP.getAsInt()
                );
            }

            // Remove infection from players at a constant rate
            if (serverPlayer.getData(ModDataAttachments.FUNGUS_INFECTION_BUILDUP) > 0) {
                serverPlayer.setData(
                    ModDataAttachments.FUNGUS_INFECTION_BUILDUP,
                    serverPlayer.getData(ModDataAttachments.FUNGUS_INFECTION_BUILDUP) - Config.FUNGAL_INFECTION_DROPOFF.getAsInt()
                );
            }

            // Bound infection buildup
            serverPlayer.setData(
                ModDataAttachments.FUNGUS_INFECTION_BUILDUP,
                Math.clamp(
                    serverPlayer.getData(ModDataAttachments.FUNGUS_INFECTION_BUILDUP),
                    Config.FUNGAL_INFECTION_MINIMUM.getAsInt(),
                    Config.FUNGAL_INFECTION_MAXIMUM.getAsInt()
                )
            );
        });
    }
}
