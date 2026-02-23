package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.hostileworld.registration.ModBlocks;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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

    private ConfiguredModel[] mycostoneConfiguredModelArray(String name) {
        ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
        int i = 1;
        final int MODEL_COUNT = 16;

        for (ModelBuilder.FaceRotation rot1 : ModelBuilder.FaceRotation.values()) {
            for (ModelBuilder.FaceRotation rot2 : ModelBuilder.FaceRotation.values()) {
                builder.modelFile(models().getExistingFile(modLoc("block/" + name + "/x_" + rot1.toString().toLowerCase() + "_y_" + rot2.toString().toLowerCase())));
                if (i < MODEL_COUNT) {
                    builder = builder.nextModel();
                }
                i++;
            }

        }
        return builder.build();
    }

    private ConfiguredModel[] mycorestoneConfiguredModelArray(boolean zAxis, boolean xAxis) {
        if (zAxis && xAxis) {
            throw new IllegalArgumentException("One axis at a time (Y is default.)");
        }

        ConfiguredModel.Builder<?> builder = ConfiguredModel.builder();
        int i = 1;
        final int MODEL_COUNT = 16;

        for (ModelBuilder.FaceRotation rot1 : ModelBuilder.FaceRotation.values()) {
            for (ModelBuilder.FaceRotation rot2 : ModelBuilder.FaceRotation.values()) {
                builder.modelFile(models().getExistingFile(modLoc("block/" + "mycorestone" + "/x_" + rot1.toString().toLowerCase() + "_y_" + rot2.toString().toLowerCase())));
                if (zAxis) {
                    builder.rotationX(90);
                } else if (xAxis) {
                    builder.rotationX(90).rotationY(90);
                }
                if (i < MODEL_COUNT) {
                    builder = builder.nextModel();
                }
                i++;
            }

        }
        return builder.build();
    }

    private void mycorestoneBuilder(ConfiguredModel[] xModels, ConfiguredModel[] yModels, ConfiguredModel[] zModels) {
        VariantBlockStateBuilder builder = getVariantBuilder(ModBlocks.MYCORESTONE.get());
        Block mycorestone = ModBlocks.MYCORESTONE.get();
        BlockState mycorestoneDefaultState = mycorestone.defaultBlockState();
        builder.forAllStates(state -> {
            if (state.equals(mycorestoneDefaultState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X))) {
                return xModels;
            } else if (state.equals(mycorestoneDefaultState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y))) {
                return yModels;
            } else {
                return zModels;
            }
        });
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.RED_MYCOSTONE.get(), mycostoneConfiguredModelArray("red_mycostone"));
        simpleBlock(ModBlocks.BROWN_MYCOSTONE.get(), mycostoneConfiguredModelArray("brown_mycostone"));
        simpleBlock(ModBlocks.MIXED_MYCOSTONE.get(), mycostoneConfiguredModelArray("mixed_mycostone"));
        simpleBlock(ModBlocks.NASCENT_AUTOIMMUNE_CLUSTER.get(), mycostoneConfiguredModelArray("nascent_autoimmune_cluster"));
        simpleBlock(ModBlocks.MATURE_AUTOIMMUNE_CLUSTER.get(), mycostoneConfiguredModelArray("mature_autoimmune_cluster"));
        simpleBlock(ModBlocks.MYCORESTONE.get());
        simpleBlock(ModBlocks.MYCOTURF.get(), new ModelFile.UncheckedModelFile(modLoc("block/mycoturf")));

    }

}
