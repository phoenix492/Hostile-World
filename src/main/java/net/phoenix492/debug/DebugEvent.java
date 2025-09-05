package net.phoenix492.debug;

import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.phoenix492.event.FungalInfectionApplyEffectEvent;
import net.phoenix492.event.FungalInfectionEvent;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModDataAttachments;

/**
 * Debug class for held items printing info and modifying values.
 */
@EventBusSubscriber(modid = HostileWorld.MODID)
public class DebugEvent {
    // TODO: listen in only when compiled in debug mode.
    @SubscribeEvent
    public static void printInfection(FungalInfectionEvent.Pre event) {
        if (event.getEntity().getMainHandItem().getItem().equals(Items.BARRIER)) {
            if (event.getEntity().hasData(ModDataAttachments.FUNGAL_INFECTION)) {
                HostileWorld.LOGGER.info(event.getEntity().getName().getString() + " infection level: " + event.getEntity().getData(ModDataAttachments.FUNGAL_INFECTION).getInfectionLevel());
            }
        }
    }

    @SubscribeEvent
    public static void printEffect(FungalInfectionApplyEffectEvent.Pre event) {
        if (event.getEntity().getMainHandItem().getItem().equals(Items.STRUCTURE_VOID)) {
            HostileWorld.LOGGER.info("Effect Target: " + event.getEntity().getName().getString());
            HostileWorld.LOGGER.info("Effect Applied: " + event.getEffect().toString());
        }
    }

    @SubscribeEvent
    public static void increaseInfection(PlayerTickEvent.Pre event) {
        if (event.getEntity().getMainHandItem().getItem().equals(Items.STRUCTURE_BLOCK)) {
            event.getEntity().getData(ModDataAttachments.FUNGAL_INFECTION).increaseInfectionLevel(1000);
            HostileWorld.LOGGER.info(event.getEntity().getName().getString() + " infection level: " + event.getEntity().getData(ModDataAttachments.FUNGAL_INFECTION).getInfectionLevel());
        }
    }

    @SubscribeEvent
    public static void reduceInfection(PlayerTickEvent.Pre event) {
        if (event.getEntity().getMainHandItem().getItem().equals(Items.SEA_PICKLE)) {
            event.getEntity().getData(ModDataAttachments.FUNGAL_INFECTION).reduceInfectionLevel(1000);
            HostileWorld.LOGGER.info(event.getEntity().getName().getString() + " infection level: " + event.getEntity().getData(ModDataAttachments.FUNGAL_INFECTION).getInfectionLevel());
        }
    }
}
