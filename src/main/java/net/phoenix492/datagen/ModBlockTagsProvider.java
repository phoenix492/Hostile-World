package net.phoenix492.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.phoenix492.hostileworld.HostileWorld;
import net.phoenix492.registration.ModBlocks;
import net.phoenix492.util.ModTagKeys;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HostileWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Informational Tags
        tag(ModTagKeys.Blocks.BECOMES_RED_FUNGUS)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_RED_MUSHROOM)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_RED_MYCOSTONE);

        tag(ModTagKeys.Blocks.BECOMES_BROWN_FUNGUS)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_BROWN_MUSHROOM)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_BROWN_MUSHROOM_BLOCK)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_BROWN_MYCOSTONE);

        // Each block added to this requires a mixin, thus it can't be a functional tag.
        tag(ModTagKeys.Blocks.SPREADS_FUNGUS)
            .add(Blocks.MYCELIUM)
            .add(Blocks.MUSHROOM_STEM)
            .add(Blocks.BROWN_MUSHROOM_BLOCK)
            .add(Blocks.RED_MUSHROOM_BLOCK)
            .add(ModBlocks.RED_MYCOSTONE.get())
            .add(ModBlocks.BROWN_MYCOSTONE.get())
            .add(ModBlocks.MIXED_MYCOSTONE.get());

        tag(ModTagKeys.Blocks.BECOMES_MYCOSTONE)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_RED_MYCOSTONE)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_MIXED_MYCOSTONE)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_BROWN_MYCOSTONE);

        tag(ModTagKeys.Blocks.MYCOSTONES)
            .add(ModBlocks.RED_MYCOSTONE.get())
            .add(ModBlocks.BROWN_MYCOSTONE.get())
            .add(ModBlocks.MIXED_MYCOSTONE.get());

        // Functional Tags

        /*
         Set to default turn blocks into red mushroom if they're added to this tag alone.
         Otherwise, informational.
        */
        tag(ModTagKeys.Blocks.BECOMES_FUNGUS)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_RED_FUNGUS)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_BROWN_FUNGUS)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_MUSHROOM_STEM)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_MYCELIUM)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_RED_MUSHROOM)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_BROWN_MUSHROOM)
            .addOptionalTag(ModTagKeys.Blocks.BECOMES_MYCOSTONE)
            .addOptionalTag(ModTagKeys.Blocks.CONSUMED_BY_FUNGUS);

        tag(ModTagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK)
            .add(Blocks.OAK_LEAVES)
            .add(Blocks.ACACIA_LEAVES)
            .add(Blocks.CHERRY_LEAVES)
            .add(Blocks.AZALEA_LEAVES);

        tag(ModTagKeys.Blocks.BECOMES_BROWN_MUSHROOM_BLOCK)
            .addTag(ModTagKeys.Blocks.LEAVES)
            .remove(ModTagKeys.Blocks.BECOMES_RED_MUSHROOM_BLOCK);

        tag(ModTagKeys.Blocks.BECOMES_MUSHROOM_STEM)
            .addTag(ModTagKeys.Blocks.LOGS);

        tag(ModTagKeys.Blocks.BECOMES_MYCELIUM)
            .add(Blocks.DIRT)
            .add(Blocks.GRASS_BLOCK);

        tag(ModTagKeys.Blocks.BECOMES_RED_MUSHROOM)
            .add(Blocks.TALL_GRASS)
            .add(Blocks.FERN)
            .add(Blocks.LARGE_FERN)
            .addOptionalTag(ModTagKeys.Blocks.FLOWERS);

        tag(ModTagKeys.Blocks.BECOMES_BROWN_MUSHROOM)
            .add(Blocks.SHORT_GRASS);

        tag(ModTagKeys.Blocks.BECOMES_RED_MYCOSTONE)
            .add(Blocks.STONE);

        tag(ModTagKeys.Blocks.CONSUMED_BY_FUNGUS)
            .add(Blocks.VINE)
            .add(Blocks.CAVE_VINES);

        tag(ModTagKeys.Blocks.MUSHROOM_FIRE_BURNS)
            .addTag(ModTagKeys.Blocks.MUSHROOM_FIRE_BURNS_TO_DIRT)
            .addTag(ModTagKeys.Blocks.MUSHROOM_FIRE_BURNS_TO_STONE)
            .add(Blocks.RED_MUSHROOM_BLOCK)
            .add(Blocks.BROWN_MUSHROOM_BLOCK)
            .add(Blocks.MUSHROOM_STEM)
            .add(Blocks.BROWN_MUSHROOM)
            .add(Blocks.RED_MUSHROOM);

        tag(ModTagKeys.Blocks.MUSHROOM_FIRE_BURNS_TO_DIRT)
            .add(Blocks.MYCELIUM);

        tag(ModTagKeys.Blocks.MUSHROOM_FIRE_BURNS_TO_STONE)
            .addTag(ModTagKeys.Blocks.MYCOSTONES);


        /*
         Mycelium spreading behavior only works for HugeMushroomBlocks, but this tag also controls which blocks will
         apply infection buildup on entities below. I supposed if you add Stems to it, they'll also spore.
        */
        tag(ModTagKeys.Blocks.DROPS_SPORES)
            .add(Blocks.RED_MUSHROOM_BLOCK)
            .add(Blocks.BROWN_MUSHROOM_BLOCK);

        /*
         When a block tagged with one of these tags spreads to a block that turns into mycostone, these tags are checked
         to determine which mycostone variant to become if the mycostone variant check is enabled in the spread entry.
        */
        tag(ModTagKeys.Blocks.RED_FUNGUS)
            .add(Blocks.RED_MUSHROOM)
            .add(Blocks.RED_MUSHROOM_BLOCK)
            .add(ModBlocks.RED_MYCOSTONE.get());

        tag(ModTagKeys.Blocks.BROWN_FUNGUS)
            .add(Blocks.BROWN_MUSHROOM)
            .add(Blocks.BROWN_MUSHROOM_BLOCK)
            .add(ModBlocks.BROWN_MYCOSTONE.get());

        // Vanilla Fire Tag Parity
        tag(BlockTags.FIRE).add(ModBlocks.MUSHROOM_FIRE.get());
        tag(BlockTags.DRAGON_TRANSPARENT).add(ModBlocks.MUSHROOM_FIRE.get());
        tag(BlockTags.REPLACEABLE).add(ModBlocks.MUSHROOM_FIRE.get());
        tag(BlockTags.ENCHANTMENT_POWER_TRANSMITTER).add(ModBlocks.MUSHROOM_FIRE.get());

    }
}
