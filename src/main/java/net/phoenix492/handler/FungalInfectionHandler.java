package net.phoenix492.handler;

import it.unimi.dsi.fastutil.longs.Long2IntFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.phoenix492.data.BlockInfectionBuildupData;
import net.phoenix492.data.EnvironmentalInfectionBuildupData;
import net.phoenix492.effect.FungalInfectionEffect;
import net.phoenix492.event.*;
import net.phoenix492.hostileworld.Config;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModDataAttachments;
import net.phoenix492.registration.ModDataMaps;
import net.phoenix492.registration.ModEffects;
import net.phoenix492.util.TagKeys;

/**
 * Handles infection status for entities every server tick.
 * Posts several cancellable events corresponding to different parts of the handling. <br>
 * <ul>
 *  <li>
 *     {@link FungalInfectionBlockBuildupEvent} - Signals an entity undergoing fungal infection buildup due to interaction with a block.
 *     Fired separately from the tick sequence below when an entity breaks a block.
 *  </li>
 * </ul>
 * <ol>
 * <li> {@link FungalInfectionTickEvent} - Signals an entity being ticked for fungal infection handling. </li>
 * <li> {@link FungalInfectionEnvironmentalBuildupEvent} - Signals an entity undergoing fungal infection buildup due to environment (biome). </li>
 * <li> {@link FungalInfectionBlockBuildupEvent} - Also fired in the tick sequence when the block an entity is standing on is checked. </li>
 * <li> {@link FungalInfectionDropoffEvent} - Signals entity's natural fungal infection drop-off. </li>
 * <li> {@link FungalInfectionApplyEffectEvent} - Signals entity receiving fungal infection effect. </li>
 * </ol>
 */
@EventBusSubscriber(modid = HostileWorld.MODID)
public class FungalInfectionHandler {

    // TODO: See if there's a way to make this compatible with mods that add block breaking entities.
    @SubscribeEvent
    public static void blockBreak(BlockEvent.BreakEvent event) {
        BlockInfectionBuildupData blockInfectionData = event.getState().getBlockHolder().getData(ModDataMaps.BLOCK_INFECTION_BUILDUP);

        /*
         Check for nullity and if we should even be doing buildup on break.
         If someone wants to adjust block buildup via event but have the base block not buildup infection inherently,
         the correct way to do this is to set onBreak to true for the block and not provide a quantity.
        */
        if (blockInfectionData == null || !blockInfectionData.onBreak()) {
            return;
        }
        if (NeoForge.EVENT_BUS.post(new FungalInfectionBlockBuildupEvent.Pre(event.getPlayer(), event.getState(), blockInfectionData)).isCanceled()) {
            return;
        }
        event.getPlayer().getData(ModDataAttachments.FUNGAL_INFECTION.get()).increaseInfectionLevel(blockInfectionData.breakQuantity());

        // Post infection
        NeoForge.EVENT_BUS.post(new FungalInfectionBlockBuildupEvent.Post(event.getPlayer(), event.getState(), blockInfectionData));
    }

    @SubscribeEvent
    public static void serverPlayerTick(PlayerTickEvent.Post event) {
        ServerPlayer serverPlayer;

        // Ignore clientside player ticks
        if (event.getEntity().level().isClientSide()) {
            return;
        } else {
            serverPlayer = (ServerPlayer) event.getEntity();
        }

        // Allow a player's fungal infection ticking to be cancelled.
        if (NeoForge.EVENT_BUS.post(new FungalInfectionTickEvent.Pre(serverPlayer)).isCanceled()) {
            return;
        }

        fungalInfectionEnvironmentalBuildup(serverPlayer);
        fungalInfectionStandingBuildup(serverPlayer);
        fungalInfectionDropoff(serverPlayer);
        fungalInfectionApplyEffect(serverPlayer);

        // Post-infection event.
        NeoForge.EVENT_BUS.post(new FungalInfectionTickEvent.Post(serverPlayer));

    }

    @SubscribeEvent
    public static void serverLivingEntityTick(EntityTickEvent.Post event) {
        LivingEntity entity;

        // Ignore clientside entity ticks, non-living entities
        if (!(event.getEntity() instanceof LivingEntity)
            || event.getEntity().level().isClientSide()) {
            return;
        } else {
            entity = (LivingEntity) event.getEntity();
        }

        // Allow entity fungal infection ticking to be cancelled.
        if (NeoForge.EVENT_BUS.post(new FungalInfectionTickEvent.Pre(entity)).isCanceled()) {
            return;
        }

        fungalInfectionEnvironmentalBuildup(entity);
        fungalInfectionStandingBuildup(entity);
        fungalInfectionDropoff(entity);
        fungalInfectionApplyEffect(entity);

        // Post-infection event.
        NeoForge.EVENT_BUS.post(new FungalInfectionTickEvent.Post(entity));

    }

    /**
     * Applies data-map defined environmental (biome) infection to the passed entity.
     * @param entity
     */
    private static void fungalInfectionEnvironmentalBuildup(LivingEntity entity) {

        // Build-up infection on players in infectious biomes.
        Holder<Biome> biomeHolder = entity.level().getBiome(entity.blockPosition());
        EnvironmentalInfectionBuildupData buildupData = entity.level().registryAccess().lookupOrThrow(Registries.BIOME).getData(ModDataMaps.ENVIRONMENTAL_INFECTION_BUILDUP, biomeHolder.getKey());

        if (buildupData == null) {
            buildupData = new EnvironmentalInfectionBuildupData(0);
        }

        if (NeoForge.EVENT_BUS.post(new FungalInfectionEnvironmentalBuildupEvent.Pre(entity, buildupData)).isCanceled()) {
            return;
        }
        entity.getData(ModDataAttachments.FUNGAL_INFECTION).tickScaledIncreaseInfectionLevel(buildupData.buildupQuantity());

        NeoForge.EVENT_BUS.post(new FungalInfectionEnvironmentalBuildupEvent.Post(entity, buildupData));

    }

    /**
     * Applies data-map defined infection for the block an entity is standing on.
     * @param entity
     */
    private static void fungalInfectionStandingBuildup(LivingEntity entity) {
        BlockState standingOn = entity.level().getBlockState(entity.blockPosition().below());
        BlockInfectionBuildupData blockInfectionData = standingOn.getBlockHolder().getData(ModDataMaps.BLOCK_INFECTION_BUILDUP);

        /*
         Check for nullity and if we should even be doing buildup for stood upon block.
         If someone wants to adjust block buildup via event but have the base block not buildup infection inherently,
         the correct way to do this is to set onStand to true for the block and not provide a quantity.
        */
        if (blockInfectionData == null || !blockInfectionData.onStand()) {
            return;
        }

        if (NeoForge.EVENT_BUS.post(new FungalInfectionBlockBuildupEvent.Pre(entity, standingOn, blockInfectionData)).isCanceled()) {
            return;
        }

        entity.getData(ModDataAttachments.FUNGAL_INFECTION.get()).increaseInfectionLevel(blockInfectionData.standQuantity());

        // Post infection
        NeoForge.EVENT_BUS.post(new FungalInfectionBlockBuildupEvent.Post(entity, standingOn, blockInfectionData));
    }

    /**
     * Applies config defined universal infection dropoff to the passed entity.
     * @param entity
     */
    private static void fungalInfectionDropoff(LivingEntity entity) {
        if (NeoForge.EVENT_BUS.post(new FungalInfectionDropoffEvent.Pre(entity)).isCanceled()) {
            return;
        }
        // Remove infection from entities at a constant rate.
        if (entity.getData(ModDataAttachments.FUNGAL_INFECTION).getInfectionLevel() > Config.FUNGAL_INFECTION_MINIMUM.get()) {
            entity.setData(
                ModDataAttachments.FUNGAL_INFECTION,
                entity.getData(ModDataAttachments.FUNGAL_INFECTION).tickScaledReduceInfectionLevel(Config.FUNGAL_INFECTION_UNIVERSAL_DROPOFF.getAsInt())
            );
        }

        NeoForge.EVENT_BUS.post(new FungalInfectionDropoffEvent.Post(entity));

    }

    /**
     * Applies a {@link MobEffectInstance} with {@link ModEffects#FUNGUS_INFECTION_EFFECT} to the passed entity. <br>
     * The amplifier/level is determined via a {@link Long2IntFunction} in the FUNGUS_INFECTION_EFFECT class.
     * @param entity
     */
    private static void fungalInfectionApplyEffect(LivingEntity entity) {
        long infectionLevel = entity.getData(ModDataAttachments.FUNGAL_INFECTION).getInfectionLevel();
        int amplifier = FungalInfectionEffect.INFECTION_TO_AMPLIFIER.get(infectionLevel);
        if (amplifier < 0) {
            return;
        }
        MobEffectInstance effect = new MobEffectInstance(
            ModEffects.FUNGUS_INFECTION_EFFECT,
            20 + Config.FUNGAL_INFECTION_PLAYER_TICK_FREQUENCY.get(), // Length in ticks equal to how often we check this
            amplifier,
            true,
            true,
            true
        );

        if (NeoForge.EVENT_BUS.post(new FungalInfectionApplyEffectEvent.Pre(entity, effect)).isCanceled()) {
            return;
        }

        entity.addEffect(effect);

        NeoForge.EVENT_BUS.post(new FungalInfectionApplyEffectEvent.Pre(entity, effect));
    }

    /**
     * Listens to {@link FungalInfectionTickEvent.Pre} and uses the config defined frequency to rate limit its execution.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void fungalInfectionRateLimiter(FungalInfectionTickEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (entity instanceof ServerPlayer && level.getGameTime() % Config.FUNGAL_INFECTION_PLAYER_TICK_FREQUENCY.getAsInt() != 0) {
            event.setCanceled(true);
        }
        else if (level.getGameTime() % Config.FUNGAL_INFECTION_ENTITY_TICK_FREQUENCY.getAsInt() != 0) {
            event.setCanceled(true);
        }
    }

    /**
     * Listens to {@link FungalInfectionTickEvent.Pre} and prevents the following from having their infection ticked.
     * <ul>
     *     <li>Players in spectator.</li>
     *     <li>Players in creative.</li>
     *     <li>Entities tagged with "#hostileworld:fungal_infection_immune"</li>
     * </ul>
     */
    @SubscribeEvent
    public static void fungalInfectionClassLimiter(FungalInfectionTickEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && (serverPlayer.isCreative() || serverPlayer.isSpectator())) {
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof LivingEntity livingEntity && livingEntity.getType().is(TagKeys.Entities.FUNGAL_INFECTION_IMMUNE)) {
            event.setCanceled(true);
        }
    }

    /**
     * Similar to {@link FungalInfectionHandler#fungalInfectionClassLimiter(FungalInfectionTickEvent.Pre)} but fires separately
     * to check that creative players aren't building up infection from breaking blocks.
     */
    @SubscribeEvent
    public static void fungalInfectionClassLimiter(FungalInfectionBlockBuildupEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && event.getBuildupData().onBreak() && (serverPlayer.isCreative() || serverPlayer.isSpectator())) {
            event.setCanceled(true);
        }
    }


}
