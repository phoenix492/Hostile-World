package net.phoenix492.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModBlocks;
import net.phoenix492.util.TagKeys;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HostileWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Informational Tags
        tag(TagKeys.Blocks.BECOMES_RED_FUNGUS)
            .addOptionalTag(TagKeys.Blocks.BECOMES_RED_MUSHROOM)
            .addOptionalTag(TagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK)
            .addOptionalTag(TagKeys.Blocks.BECOMES_RED_MYCOSTONE);

        tag(TagKeys.Blocks.BECOMES_BROWN_FUNGUS)
            .addOptionalTag(TagKeys.Blocks.BECOMES_BROWN_MUSHROOM)
            .addOptionalTag(TagKeys.Blocks.BECOMES_BROWN_MUSHROOM_BLOCK)
            .addOptionalTag(TagKeys.Blocks.BECOMES_BROWN_MYCOSTONE);

        // Each block added to this requires a mixin, thus it can't be a functional tag.
        tag(TagKeys.Blocks.SPREADS_FUNGUS)
            .add(Blocks.MYCELIUM)
            .add(Blocks.MUSHROOM_STEM)
            .add(Blocks.BROWN_MUSHROOM_BLOCK)
            .add(Blocks.RED_MUSHROOM_BLOCK)
            .add(ModBlocks.RED_MYCOSTONE.get())
            .add(ModBlocks.BROWN_MYCOSTONE.get())
            .add(ModBlocks.MIXED_MYCOSTONE.get());

        tag(TagKeys.Blocks.BECOMES_MYCOSTONE)
            .addOptionalTag(TagKeys.Blocks.BECOMES_RED_MYCOSTONE)
            .addOptionalTag(TagKeys.Blocks.BECOMES_MIXED_MYCOSTONE)
            .addOptionalTag(TagKeys.Blocks.BECOMES_BROWN_MYCOSTONE);


        // Functional Tags

        /*
         Set to default turn blocks into red mushroom if they're added to this tag alone.
         Otherwise, informational.
        */
        tag(TagKeys.Blocks.BECOMES_FUNGUS)
            .addOptionalTag(TagKeys.Blocks.BECOMES_RED_FUNGUS)
            .addOptionalTag(TagKeys.Blocks.BECOMES_BROWN_FUNGUS)
            .addOptionalTag(TagKeys.Blocks.BECOMES_MUSHROOM_STEM)
            .addOptionalTag(TagKeys.Blocks.BECOMES_MYCELIUM)
            .addOptionalTag(TagKeys.Blocks.BECOMES_RED_MUSHROOM)
            .addOptionalTag(TagKeys.Blocks.BECOMES_BROWN_MUSHROOM)
            .addOptionalTag(TagKeys.Blocks.BECOMES_MYCOSTONE)
            .addOptionalTag(TagKeys.Blocks.CONSUMED_BY_FUNGUS);

        tag(TagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK)
            .add(Blocks.OAK_LEAVES)
            .add(Blocks.ACACIA_LEAVES)
            .add(Blocks.CHERRY_LEAVES)
            .add(Blocks.AZALEA_LEAVES);

        tag(TagKeys.Blocks.BECOMES_BROWN_MUSHROOM_BLOCK)
            .addTag(TagKeys.Blocks.LEAVES)
            .remove(TagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK);

        tag(TagKeys.Blocks.BECOMES_MUSHROOM_STEM)
            .addTag(TagKeys.Blocks.LOGS);

        tag(TagKeys.Blocks.BECOMES_MYCELIUM)
            .add(Blocks.DIRT)
            .add(Blocks.GRASS_BLOCK);

        tag(TagKeys.Blocks.BECOMES_RED_MUSHROOM)
            .add(Blocks.TALL_GRASS)
            .add(Blocks.FERN)
            .add(Blocks.LARGE_FERN)
            .addOptionalTag(TagKeys.Blocks.FLOWERS);

        tag(TagKeys.Blocks.BECOMES_BROWN_MUSHROOM)
            .add(Blocks.SHORT_GRASS);

        tag(TagKeys.Blocks.BECOMES_RED_MYCOSTONE)
            .add(Blocks.STONE);

        tag(TagKeys.Blocks.CONSUMED_BY_FUNGUS)
            .add(Blocks.VINE)
            .add(Blocks.CAVE_VINES)
            .add(Blocks.GLOW_LICHEN);

        tag(TagKeys.Blocks.MUSHROOM_FIRE_BURNS)
            .addTag(TagKeys.Blocks.MUSHROOM_FIRE_BURNS_TO_DIRT)
            .addTag(TagKeys.Blocks.MUSHROOM_FIRE_BURNS_TO_STONE)
            .add(Blocks.RED_MUSHROOM_BLOCK)
            .add(Blocks.BROWN_MUSHROOM_BLOCK)
            .add(Blocks.MUSHROOM_STEM)
            .add(Blocks.BROWN_MUSHROOM)
            .add(Blocks.RED_MUSHROOM);

        tag(TagKeys.Blocks.MUSHROOM_FIRE_BURNS_TO_DIRT)
            .add(Blocks.MYCELIUM);

        tag(TagKeys.Blocks.MUSHROOM_FIRE_BURNS_TO_STONE)
            .add(ModBlocks.RED_MYCOSTONE.get());


        /*
         Mycelium spreading behavior only works for HugeMushroomBlocks, but this tag also controls which blocks will
         apply infection buildup on entities below.
        */
        tag(TagKeys.Blocks.DROPS_SPORES)
            .add(Blocks.RED_MUSHROOM_BLOCK)
            .add(Blocks.BROWN_MUSHROOM_BLOCK);

        // Vanilla Fire Tag Parity
        tag(TagKeys.Blocks.FIRE).add(ModBlocks.MUSHROOM_FIRE.get());
        tag(TagKeys.Blocks.DRAGON_TRANSPARENT).add(ModBlocks.MUSHROOM_FIRE.get());
        tag(TagKeys.Blocks.REPLACEABLE).add(ModBlocks.MUSHROOM_FIRE.get());
        tag(TagKeys.Blocks.ENCHANTMENT_POWER_TRANSMITTER).add(ModBlocks.MUSHROOM_FIRE.get());

    }
}
