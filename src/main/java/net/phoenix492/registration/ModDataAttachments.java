package net.phoenix492.registration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.phoenix492.attachments.FungalInfectionData;
import net.phoenix492.hostileworld.HostileWorld;

import java.util.function.Supplier;

public class ModDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, HostileWorld.MODID);

    public static final Supplier<AttachmentType<FungalInfectionData>> FUNGAL_INFECTION = ATTACHMENT_TYPES.register(
      "fungal_infection",
        () -> AttachmentType.builder(FungalInfectionData::new).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
