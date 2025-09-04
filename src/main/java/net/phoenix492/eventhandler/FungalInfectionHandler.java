package net.phoenix492.eventhandler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.phoenix492.event.FungalInfectionEnvironmentalBuildupEvent;
import net.phoenix492.event.FungalInfectionApplyEffectEvent;
import net.phoenix492.event.FungalInfectionDropoffEvent;
import net.phoenix492.event.FungalInfectionEvent;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModDataAttachments;
import net.phoenix492.util.TagKeys;

/**
 * Handles infection status for entities every server tick.
 * Posts several cancellable events corresponding to different parts of the handling. <br>
 * <ol>
 * <li> {@link FungalInfectionEvent} - Signals an entity being ticked for fungal infection handling. </li>
 * <li> {@link FungalInfectionEnvironmentalBuildupEvent} - Signals an entity undergoing fungal infection buildup due to environment (biome) </li>
 * <li> {@link FungalInfectionDropoffEvent} - Signals entity's natural fungal infection drop-off. </li>
 * <li> {@link FungalInfectionApplyEffectEvent} - Signals entity receiving fungal infection effect. </li>
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
    public static void serverPlayerTick(PlayerTickEvent.Pre event) {

        ServerPlayer serverPlayer;

        // Ignore clientside player ticks
        if (event.getEntity().level().isClientSide()) {
            return;
        } else {
            serverPlayer = (ServerPlayer) event.getEntity();
        }

        // Allow a player's fungal infection ticking to be cancelled.
        if (NeoForge.EVENT_BUS.post(new FungalInfectionEvent.Pre(serverPlayer)).isCanceled()) {
            return;
        }

        // Apply environmental infection buildup for player.
        fungalInfectionEnvironmentalBuildup(serverPlayer);

        // Apply fungal infection drop-off for player.
        fungalInfectionDropoff(serverPlayer);

        // Apply fungal infection effect to player.
        fungalInfectionApplyEffect(serverPlayer);

    }

    private static void fungalInfectionEnvironmentalBuildup(ServerPlayer serverPlayer) {
        if (!NeoForge.EVENT_BUS.post(new FungalInfectionEnvironmentalBuildupEvent.Pre(serverPlayer)).isCanceled()) {
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

    private static void fungalInfectionDropoff(ServerPlayer serverPlayer) {
        if (!NeoForge.EVENT_BUS.post(new FungalInfectionDropoffEvent.Pre(serverPlayer)).isCanceled()) {
            return;
        }
        // Remove infection from entities at a constant rate.
        if (serverPlayer.getData(ModDataAttachments.FUNGAL_INFECTION_BUILDUP) > Config.FUNGAL_INFECTION_MINIMUM.get()) {
            serverPlayer.setData(
                ModDataAttachments.FUNGAL_INFECTION_BUILDUP,
                serverPlayer.getData(ModDataAttachments.FUNGAL_INFECTION_BUILDUP) - Config.FUNGAL_INFECTION_DROPOFF.getAsInt()
            );
        }

        clampInfection(serverPlayer);
    }

    private static void fungalInfectionApplyEffect(ServerPlayer serverPlayer) {
//        if (!NeoForge.EVENT_BUS.post(new FungalInfectionApplyEffectEvent.Pre(serverPlayer)).isCanceled()) {
//            return;
//        }
    }
}
