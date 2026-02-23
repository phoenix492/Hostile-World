package net.phoenix492.hostileworld.datagen;

import net.phoenix492.hostileworld.registration.ModItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.IMMUNITE_CLUSTER)
            .requires(ModItems.IMMUNITE_SHARD, 9)
            .unlockedBy("has_immunite_shard", has(ModItems.IMMUNITE_SHARD))
            .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.IMMUNITE_SHARD, 9)
            .requires(ModItems.IMMUNITE_CLUSTER)
            .unlockedBy("has_immunite_cluster", has(ModItems.IMMUNITE_CLUSTER))
            .save(recipeOutput);
    }
}
