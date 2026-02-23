package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.registration.ModItems;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HostileWorld.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.APOCALYPTIC_MUSHROOM_IGNITER.asItem());
        basicItem(ModItems.IMMUNITE_CLUSTER.asItem());
        basicItem(ModItems.IMMUNITE_SHARD.asItem());
        withExistingParent("strong_mushroom_igniter", ResourceLocation.parse("hostileworld:item/apocalyptic_mushroom_igniter"));
        withExistingParent("weak_mushroom_igniter", ResourceLocation.parse("hostileworld:item/apocalyptic_mushroom_igniter"));
        withExistingParent("normal_mushroom_igniter", ResourceLocation.parse("hostileworld:item/apocalyptic_mushroom_igniter"));
        withExistingParent("red_mycostone", ResourceLocation.parse("hostileworld:block/dummy_red_mycostone"));
        withExistingParent("brown_mycostone", ResourceLocation.parse("hostileworld:block/dummy_brown_mycostone"));
        withExistingParent("mixed_mycostone", ResourceLocation.parse("hostileworld:block/dummy_mixed_mycostone"));
        withExistingParent("mycorestone", ResourceLocation.parse("hostileworld:block/dummy_mycorestone"));
        withExistingParent("mycoturf", ResourceLocation.parse("hostileworld:block/mycoturf"));
        withExistingParent("nascent_autoimmune_cluster", ResourceLocation.parse("hostileworld:block/dummy_nascent_autoimmune_cluster"));
        withExistingParent("mature_autoimmune_cluster", ResourceLocation.parse("hostileworld:block/dummy_mature_autoimmune_cluster"));
    }
}
