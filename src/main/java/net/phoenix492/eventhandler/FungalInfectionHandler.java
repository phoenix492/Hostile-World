package net.phoenix492.eventhandler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.phoenix492.event.EnvironmentalFungalInfectionBuildupEvent;
import net.phoenix492.event.FungalInfectionApplyEffectEvent;
import net.phoenix492.event.FungalInfectionDropoffEvent;
import net.phoenix492.event.FungalInfectionPlayerTickEvent;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModDataAttachments;
import net.phoenix492.util.TagKeys;

import java.util.List;

/**
 * Handles infection status for each player every server tick.
 * Posts several cancellable events corresponding to different parts of the handling. <br>
 * <ol>
 * <li> {@link FungalInfectionPlayerTickEvent} - Posts before each player is ticked. </li>
 * <li> {@link EnvironmentalFungalInfectionBuildupEvent} - Posts before buildup due to player environment (biome) </li>
 * <li> {@link FungalInfectionDropoffEvent} - Posts before natural infection drop-off. </li>
 * </ol>
 */
@EventBusSubscriber(modid = HostileWorld.MODID)
public class FungalInfectionHandler {

    /**
     * Public helper/sanitization method that ensures an entity's infection status lies between configuration defined values.
     * @param livingEntity Living entity whose infection buildup should be clamped
     */
    public static void clampInfection(LivingEntity livingEntity) {
        // Bound infection buildup to config defined values.
        livingEntity.setData(
            ModDataAttachments.FUNGAL_INFECTION_BUILDUP,
            Math.clamp(
                livingEntity.getData(ModDataAttachments.FUNGAL_INFECTION_BUILDUP),
                Config.FUNGAL_INFECTION_MINIMUM.getAsInt(),
                Config.FUNGAL_INFECTION_MAXIMUM.getAsInt()
            )
        );
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Pre event) {
        List<ServerPlayer> playerList = event.getServer().getPlayerList().getPlayers();
        playerList.forEach(serverPlayer -> {
            // Allow a player's fungal infection ticking to be cancelled.
            if (NeoForge.EVENT_BUS.post(new FungalInfectionPlayerTickEvent(serverPlayer)).isCanceled()) {
                return;
            }

            // Apply environmental infection buildup for player
            environmentalInfectionBuildup(serverPlayer);

            // Apply fungal infection dropoff for player
            infectionDropoff(serverPlayer);

            // Apply fungal infection effect to player, unless event is cancelled.
            applyInfectionEffect(serverPlayer);

        });

    }

    private static void environmentalInfectionBuildup(ServerPlayer serverPlayer) {
        if (!NeoForge.EVENT_BUS.post(new EnvironmentalFungalInfectionBuildupEvent(serverPlayer)).isCanceled()) {
            return;
        }

        // Build-up infection on players in infectious biomes.
        if (serverPlayer.level().getBiome(serverPlayer.blockPosition()).is(TagKeys.Biomes.BUILDS_FUNGAL_INFECTION)) {
            serverPlayer.setData(
                ModDataAttachments.FUNGAL_INFECTION_BUILDUP,
                serverPlayer.getData(ModDataAttachments.FUNGAL_INFECTION_BUILDUP) + Config.FUNGAL_INFECTION_BIOME_BUILDUP.getAsInt()
            );
        }

        clampInfection(serverPlayer);
    }

    private static void infectionDropoff(ServerPlayer serverPlayer) {
        if (!NeoForge.EVENT_BUS.post(new FungalInfectionDropoffEvent(serverPlayer)).isCanceled()) {
            return;
        }
        // Remove infection from players at a constant rate.
        if (serverPlayer.getData(ModDataAttachments.FUNGAL_INFECTION_BUILDUP) > Config.FUNGAL_INFECTION_MINIMUM.get()) {
            serverPlayer.setData(
                ModDataAttachments.FUNGAL_INFECTION_BUILDUP,
                serverPlayer.getData(ModDataAttachments.FUNGAL_INFECTION_BUILDUP) - Config.FUNGAL_INFECTION_DROPOFF.getAsInt()
            );
        }

        clampInfection(serverPlayer);
    }

    private static void applyInfectionEffect(ServerPlayer serverPlayer) {
        if (!NeoForge.EVENT_BUS.post(new FungalInfectionApplyEffectEvent(serverPlayer)).isCanceled()) {
            return;
        }

    }
}
