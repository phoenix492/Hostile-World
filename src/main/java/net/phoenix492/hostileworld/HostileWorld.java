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
import net.phoenix492.hostileworld.registration.ModPotions;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

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
        ModGameRules.register();
        modContainer.registerConfig(ModConfig.Type.SERVER, HostileWorldConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

}
