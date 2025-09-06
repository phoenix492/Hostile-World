package net.phoenix492.hostileworld;

import net.phoenix492.registration.ModDataAttachments;
import net.phoenix492.registration.ModEffects;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(HostileWorld.MODID)
public class HostileWorld {

    public static final String MODID = "hostileworld";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean DEBUG = true;


    public HostileWorld(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModDataAttachments.register(modEventBus);
        ModEffects.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

}
