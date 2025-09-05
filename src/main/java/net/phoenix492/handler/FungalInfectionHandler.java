package net.phoenix492.handler;

import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.phoenix492.event.FungalInfectionApplyEffectEvent;
import net.phoenix492.event.FungalInfectionDropoffEvent;
import net.phoenix492.event.FungalInfectionEnvironmentalBuildupEvent;
import net.phoenix492.event.FungalInfectionEvent;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModDataAttachments;
import net.phoenix492.registration.ModEffects;
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
    // TODO: Replace with configured values. Validate in config that each stage is greater than the last.
    private static final Long2IntFunction INFECTION_TO_AMPLIFIER = key -> {
        if (key > 10000) {
            return 4;
        }
        if (key > 8000) {
            return 3;
        }
        if (key > 6000) {
            return 2;
        }
        if (key > 4000) {
            return 1;
        }
        if (key > 2000) {
            return 0;
        }
        return -1;
    };
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

        // Post-infection event.
        NeoForge.EVENT_BUS.post(new FungalInfectionEvent.Post(serverPlayer));

    }

    private static void fungalInfectionEnvironmentalBuildup(ServerPlayer serverPlayer) {
        if (NeoForge.EVENT_BUS.post(new FungalInfectionEnvironmentalBuildupEvent.Pre(serverPlayer)).isCanceled()) {
            return;
        }

        // Build-up infection on players in infectious biomes.
        if (serverPlayer.level().getBiome(serverPlayer.blockPosition()).is(TagKeys.Biomes.BUILDS_FUNGAL_INFECTION)) {
            serverPlayer.getData(ModDataAttachments.FUNGAL_INFECTION).increaseInfectionLevel(Config.FUNGAL_INFECTION_BIOME_BUILDUP.getAsInt());
        }

        NeoForge.EVENT_BUS.post(new FungalInfectionEnvironmentalBuildupEvent.Post(serverPlayer));

    }

    private static void fungalInfectionDropoff(ServerPlayer serverPlayer) {
        if (NeoForge.EVENT_BUS.post(new FungalInfectionDropoffEvent.Pre(serverPlayer)).isCanceled()) {
            return;
        }
        // Remove infection from entities at a constant rate.
        if (serverPlayer.getData(ModDataAttachments.FUNGAL_INFECTION).getInfectionLevel() > Config.FUNGAL_INFECTION_MINIMUM.get()) {
            serverPlayer.setData(
                ModDataAttachments.FUNGAL_INFECTION,
                serverPlayer.getData(ModDataAttachments.FUNGAL_INFECTION).reduceInfectionLevel(Config.FUNGAL_INFECTION_DROPOFF.getAsInt())
            );
        }

        NeoForge.EVENT_BUS.post(new FungalInfectionDropoffEvent.Post(serverPlayer));

    }

    private static void fungalInfectionApplyEffect(ServerPlayer serverPlayer) {
        int amplifier = INFECTION_TO_AMPLIFIER.get(serverPlayer.getData(ModDataAttachments.FUNGAL_INFECTION).getInfectionLevel());
        if (amplifier < 0) {
            return;
        }
        MobEffectInstance effect = new MobEffectInstance(
            ModEffects.FUNGUS_INFECTION_EFFECT,
            20,
            amplifier,
            true,
            true,
            true
        );

        if (NeoForge.EVENT_BUS.post(new FungalInfectionApplyEffectEvent.Pre(serverPlayer, effect)).isCanceled()) {
            return;
        }

        serverPlayer.addEffect(effect);

        NeoForge.EVENT_BUS.post(new FungalInfectionApplyEffectEvent.Pre(serverPlayer, effect));
    }
}
