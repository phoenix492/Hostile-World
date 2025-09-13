package net.phoenix492.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModBlocks;

public class ModBlockstateProvider extends BlockStateProvider {

    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HostileWorld.MODID, exFileHelper);

    }

    private ConfiguredModel[] simpleRandomlyRotatingBlockXY(String name) {
        ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
        for (int xRot = 0; xRot <= 270; xRot += 90) {
            for (int yRot = 0; yRot <= 270; yRot += 90) {
                builder.modelFile(models().withExistingParent(name, this.mcLoc("block/cube_all"))).rotationX(xRot).rotationY(yRot);
                if (!(xRot == 270 && yRot == 270)) {
                    builder = builder.nextModel();
                }
            }
        }
        return builder.build();
    }

    private ConfiguredModel[] simpleRandomlyRotatingBlockY(String name) {
        ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
        for (int yRot = 0; yRot <= 270; yRot += 90) {
            builder.modelFile(models().withExistingParent(name, this.mcLoc("block/cube_all"))).rotationY(yRot);
            if (yRot != 270) {
                builder = builder.nextModel();
            }
        }
        return builder.build();
    }

    private ConfiguredModel[] simpleRandomlyRotatingBlockX(String name) {
        ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
        for (int xRot = 0; xRot <= 270; xRot += 90) {
            builder.modelFile(models().withExistingParent(name, this.mcLoc("block/cube_all"))).rotationX(xRot);
            if (xRot != 270) {
                builder = builder.nextModel();
            }
        }
        return builder.build();
    }

    private ConfiguredModel[] mycostoneConfiguredModel(String name) {
        ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
        int i = 0;
        for (ModelBuilder.FaceRotation rot1 : ModelBuilder.FaceRotation.values()) {
            for (ModelBuilder.FaceRotation rot2 : ModelBuilder.FaceRotation.values()) {
                builder.modelFile(models().getExistingFile(modLoc("block/" + name + "/x_"+rot1.toString().toLowerCase()+"_y_"+rot2.toString().toLowerCase())));
                if (i < 15) {
                    builder = builder.nextModel();
                }
                i++;
            }

        }
        return builder.build();
    }


    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.RED_MYCOSTONE.get(), mycostoneConfiguredModel("red_mycostone"));
        simpleBlock(ModBlocks.BROWN_MYCOSTONE.get(), mycostoneConfiguredModel("brown_mycostone"));
        simpleBlock(ModBlocks.MIXED_MYCOSTONE.get(), mycostoneConfiguredModel("mixed_mycostone"));
        simpleBlock(ModBlocks.MYCORESTONE.get(), mycostoneConfiguredModel("mycorestone")); // TODO: Make this rotate when placed.
    }

}
