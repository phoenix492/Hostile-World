package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

public class ModBlockModelProvider extends BlockModelProvider {
    public ModBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HostileWorld.MODID, existingFileHelper);
    }

    @CanIgnoreReturnValue
    private BlockModelBuilder[] mycostoneModelBuilder(String blockName) {
        BlockModelBuilder[] models = new BlockModelBuilder[16];
        int i = 0;
        for (ModelBuilder.FaceRotation rot1 : ModelBuilder.FaceRotation.values()) {
            for (ModelBuilder.FaceRotation rot2 : ModelBuilder.FaceRotation.values()) {
                models[i] = getBuilder("block/" + blockName + "/x_"+rot1.toString().toLowerCase()+"_y_"+rot2.toString().toLowerCase());
                models[i].renderType("cutout_mipped");
                models[i].texture("stone", mcLoc("block/stone"));
                models[i].texture("overlay", modLoc("block/"+blockName+"_overlay"));
                models[i].texture("particle", mcLoc("block/stone"));
                models[i].element().allFaces((direction, faceBuilder) -> faceBuilder.uvs(0, 0, 16,16).texture("#stone").cullface(direction));
                models[i].element().allFaces((direction, faceBuilder) -> {
                    if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#overlay").cullface(direction).rotation(rot1);
                    } else {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#overlay").cullface(direction).rotation(rot2);
                    }
                });
                i++;
            }
        }
        return models;
    }

    @CanIgnoreReturnValue
    private BlockModelBuilder[] mycorestoneModelBuilder(String blockName) {
        BlockModelBuilder[] models = new BlockModelBuilder[16];
        int i = 0;
        for (ModelBuilder.FaceRotation rot1 : ModelBuilder.FaceRotation.values()) {
            for (ModelBuilder.FaceRotation rot2 : ModelBuilder.FaceRotation.values()) {
                models[i] = getBuilder("block/" + blockName + "/x_"+rot1.toString().toLowerCase()+"_y_"+rot2.toString().toLowerCase());
                models[i].renderType("cutout_mipped");
                models[i].texture("side", mcLoc("block/deepslate"));
                models[i].texture("top", mcLoc("block/deepslate_top"));
                models[i].texture("overlay", modLoc("block/"+blockName+"_overlay"));
                models[i].texture("particle", mcLoc("block/deepslate"));
                models[i].element().allFaces((direction, faceBuilder) -> {
                    if (direction != Direction.UP && direction != Direction.DOWN) {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#side").cullface(direction);
                    } else {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#top").cullface(direction);
                    }
                });
                models[i].element().allFaces((direction, faceBuilder) -> {
                    if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#overlay").cullface(direction).rotation(rot1);
                    } else {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#overlay").cullface(direction).rotation(rot2);
                    }
                });
                i++;
            }
        }
        return models;
    }

    @Override
    protected void registerModels() {
        cubeAll("dummy_red_mycostone", ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "block/red_mycostone"));
        cubeAll("dummy_brown_mycostone", ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "block/brown_mycostone"));
        cubeAll("dummy_mixed_mycostone", ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "block/mixed_mycostone"));
        cubeAll("dummy_nascent_autoimmune_cluster", ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "block/nascent_autoimmune_cluster"));
        cubeAll("dummy_mature_autoimmune_cluster", ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "block/mature_autoimmune_cluster"));
        cubeAll("dummy_mycorestone", ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, "block/mycorestone"));
        cubeAll("mycoturf", mcLoc("block/mycelium_top"));

        mycostoneModelBuilder("red_mycostone");
        mycostoneModelBuilder("brown_mycostone");
        mycostoneModelBuilder("mixed_mycostone");
        mycostoneModelBuilder("nascent_autoimmune_cluster");
        mycostoneModelBuilder("mature_autoimmune_cluster");
        mycorestoneModelBuilder("mycorestone");

    }
}
