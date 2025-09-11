package net.phoenix492.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModBlocks;

public class ModBlockstateProvider extends BlockStateProvider {

    public ConfiguredModel[] RED_MYCOSTONE_CONFIGURED_MODEL = ConfiguredModel.builder()
        .modelFile(models().withExistingParent("red_mycostone", this.mcLoc("block/cube_all")))
        .nextModel()
        .modelFile(models().withExistingParent("red_mycostone", this.mcLoc("block/cube_all")))
        .rotationY(90)
        .nextModel()
        .modelFile(models().withExistingParent("red_mycostone", this.mcLoc("block/cube_all")))
        .rotationY(180)
        .nextModel()
        .modelFile(models().withExistingParent("red_mycostone", this.mcLoc("block/cube_all")))
        .rotationY(270)
        .build();

    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HostileWorld.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.RED_MYCOSTONE.get(), RED_MYCOSTONE_CONFIGURED_MODEL);
    }
}
