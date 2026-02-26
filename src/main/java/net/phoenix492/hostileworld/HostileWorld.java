package net.phoenix492.hostileworld;

import net.phoenix492.hostileworld.config.HostileWorldConfig;
import net.phoenix492.hostileworld.registration.ModBlocks;
import net.phoenix492.hostileworld.registration.ModCreativeModeTabs;
import net.phoenix492.hostileworld.registration.ModDataAttachments;
import net.phoenix492.hostileworld.registration.ModDataComponents;
import net.phoenix492.hostileworld.registration.ModEffects;
import net.phoenix492.hostileworld.registration.ModFeatures;
import net.phoenix492.hostileworld.registration.ModGameRules;
import net.phoenix492.hostileworld.registration.ModItems;
import net.phoenix492.hostileworld.registration.ModParticles;
import net.phoenix492.hostileworld.registration.ModPotions;
import net.phoenix492.hostileworld.particle.SporeDropperParticle;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(HostileWorld.MODID)
public class HostileWorld {

    public static final String MODID = "hostileworld";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean DEBUG = true;


    public HostileWorld(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Registration
        ModDataAttachments.register(modEventBus);
        ModEffects.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModPotions.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModParticles.register(modEventBus);
        ModGameRules.register();
        modContainer.registerConfig(ModConfig.Type.SERVER, HostileWorldConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    @EventBusSubscriber(Dist.CLIENT)
    public static class ClientSetup {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {

        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.SPORE_DROPPER_PARTICLE.get(), SporeDropperParticle.Provider::new);
        }
    }

}
