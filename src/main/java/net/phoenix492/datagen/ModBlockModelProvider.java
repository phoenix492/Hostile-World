package net.phoenix492.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.phoenix492.hostileworld.HostileWorld;

public class ModBlockModelProvider extends BlockModelProvider {
    public ModBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HostileWorld.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // TODO: switch this to a two-layer model and rotate the fungus veins independently
        cubeAll("red_mycostone", ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "block/red_mycostone"));
    }
}
