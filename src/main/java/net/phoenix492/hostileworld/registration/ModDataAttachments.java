package net.phoenix492.hostileworld.registration;

import net.phoenix492.hostileworld.data.attachment.EntityFungalInfectionData;
import net.phoenix492.hostileworld.HostileWorld;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, HostileWorld.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<EntityFungalInfectionData>> FUNGAL_INFECTION = ATTACHMENT_TYPES.register(
      "fungal_infection",
        () -> AttachmentType.builder(EntityFungalInfectionData::new).serialize(EntityFungalInfectionData.CODEC).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
