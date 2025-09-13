package net.phoenix492.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HostileWorld.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.DEBUG_MUSHROOM_IGNITER.asItem());
        withExistingParent("red_mycostone", ResourceLocation.parse("hostileworld:block/dummy_red_mycostone"));
        withExistingParent("brown_mycostone", ResourceLocation.parse("hostileworld:block/dummy_brown_mycostone"));
        withExistingParent("mixed_mycostone", ResourceLocation.parse("hostileworld:block/dummy_mixed_mycostone"));
    }
}
