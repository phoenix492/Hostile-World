package net.phoenix492.hostileworld;

import net.phoenix492.registration.ModBlocks;
import net.phoenix492.registration.ModCreativeModeTabs;
import net.phoenix492.registration.ModDataAttachments;
import net.phoenix492.registration.ModDataComponents;
import net.phoenix492.registration.ModEffects;
import net.phoenix492.registration.ModItems;

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
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

}
