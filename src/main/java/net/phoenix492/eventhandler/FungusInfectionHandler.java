package net.phoenix492.eventhandler;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.phoenix492.event.FungusInfectionPlayerTickEvent;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModDataAttachments;
import net.phoenix492.util.TagKeys;

import java.util.List;

/**
 * Handles events relating to Fungus Infection.
 */
@EventBusSubscriber(modid = HostileWorld.MODID)
public class FungusInfectionHandler {

    @SubscribeEvent
    public static void infectionBuildup(ServerTickEvent.Pre event) {

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

            // Fire infection tick event if player has any amount of buildup.
            if (serverPlayer.getData(ModDataAttachments.FUNGUS_INFECTION_BUILDUP) > 0) {
                NeoForge.EVENT_BUS.post(new FungusInfectionPlayerTickEvent(serverPlayer));
            }
        });
    }

    @SubscribeEvent
    public static void infectionTick(FungusInfectionPlayerTickEvent event) {
        int buildup = event.getPlayer().getData(ModDataAttachments.FUNGUS_INFECTION_BUILDUP);

    }
}
