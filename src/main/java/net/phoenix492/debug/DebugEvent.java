package net.phoenix492.debug;

import net.phoenix492.event.FungalInfectionApplyEffectEvent;
import net.phoenix492.event.FungalInfectionEnvironmentalBuildupEvent;
import net.phoenix492.event.FungalInfectionTickEvent;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModDataAttachments;
import net.phoenix492.registration.ModDataMaps;

import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Debug class for held items printing info and modifying values.
 */
@EventBusSubscriber(modid = HostileWorld.MODID)
public class DebugEvent {
    @SubscribeEvent
    public static void printInfection(FungalInfectionTickEvent.Post event) {
        if (!HostileWorld.DEBUG) {
            return;
        }
        if (event.getEntity().getMainHandItem().getItem().equals(Items.BARRIER)) {
            if (event.getEntity().hasData(ModDataAttachments.FUNGAL_INFECTION)) {
                HostileWorld.LOGGER.debug(event.getEntity().getName().getString() + " infection level: " + event.getEntity().getData(ModDataAttachments.FUNGAL_INFECTION).getInfectionLevel());
            }
        }
    }

    @SubscribeEvent
    public static void printEffect(FungalInfectionApplyEffectEvent.Pre event) {
        if (!HostileWorld.DEBUG) {
            return;
        }
        if (event.getEntity().getMainHandItem().getItem().equals(Items.STRUCTURE_VOID)) {
            HostileWorld.LOGGER.debug("Effect Target: " + event.getEntity().getName().getString());
            HostileWorld.LOGGER.debug("Effect Applied: " + event.getEffect().toString());
        }
    }

    @SubscribeEvent
    public static void increaseInfection(FungalInfectionEnvironmentalBuildupEvent.Pre event) {
        if (!HostileWorld.DEBUG) {
            return;
        }
        if (event.getEntity().getMainHandItem().getItem().equals(Items.STRUCTURE_BLOCK)) {
            event.setBuildupData(1000);
            HostileWorld.LOGGER.debug(event.getEntity().getName().getString() + " infection level: " + event.getEntity().getData(ModDataAttachments.FUNGAL_INFECTION).getInfectionLevel());
        }
    }

    @SubscribeEvent
    public static void reduceInfection(FungalInfectionEnvironmentalBuildupEvent.Pre event) {
        if (!HostileWorld.DEBUG) {
            return;
        }
        if (event.getEntity().getMainHandItem().getItem().equals(Items.SEA_PICKLE)) {
            HostileWorld.LOGGER.debug("Reducing infection level by 1000!");
            event.setBuildupData(-1000);
        }
    }

    @SubscribeEvent
    public static void printBiomeInfectionBuildup(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (event.getEntity().getMainHandItem().getItem().equals(Items.COMMAND_BLOCK)) {
            HostileWorld.LOGGER.debug(event.getEntity().getName().getString() + " biome: " + event.getEntity().level().getBiome(event.getEntity().blockPosition()).getRegisteredName());
            HostileWorld.LOGGER.debug(event.getEntity().getName().getString() + " biome buildup: " + event.getEntity().level().getBiome(event.getEntity().blockPosition()).getData(ModDataMaps.ENVIRONMENTAL_INFECTION_BUILDUP));
        }
    }

}
