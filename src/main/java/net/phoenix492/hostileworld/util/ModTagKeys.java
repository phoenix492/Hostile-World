package net.phoenix492.hostileworld.util;

import net.phoenix492.hostileworld.HostileWorld;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTagKeys {
    public static class Biomes {
        public final static TagKey<Biome> BUILDS_FUNGAL_INFECTION_SLOW = createTag(HostileWorld.MODID,"builds_fungal_infection_slow");
        public final static TagKey<Biome> BUILDS_FUNGAL_INFECTION_STANDARD = createTag(HostileWorld.MODID,"builds_fungal_infection_standard");
        public final static TagKey<Biome> BUILDS_FUNGAL_INFECTION_FAST = createTag(HostileWorld.MODID,"builds_fungal_infection_fast");
        public final static TagKey<Biome> MYCOFIRE_RESISTANT = createTag(HostileWorld.MODID,"mycofire_resistant");

        public final static TagKey<Biome> IS_MAGICAL = createTag("c", "is_magical");

        private static TagKey<Biome> createTag(String namespace, String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(namespace, name));
        }
    }
    public static class Entities {
        public static TagKey<EntityType<?>> FUNGAL_INFECTION_IMMUNE = createTag(HostileWorld.MODID, "fungal_infection_immune");

        private static TagKey<EntityType<?>> createTag(String namespace, String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(namespace, name));
        }
    }
    public static class Blocks {
        public final static TagKey<Block> RED_FUNGUS = createTag(HostileWorld.MODID, "red_fungus");
        public final static TagKey<Block> BROWN_FUNGUS = createTag(HostileWorld.MODID, "brown_fungus");
        public final static TagKey<Block> SPREADS_FUNGUS = createTag(HostileWorld.MODID, "spreads_fungus");
        public final static TagKey<Block> BECOMES_FUNGUS = createTag(HostileWorld.MODID, "becomes_fungus");
        public final static TagKey<Block> BECOMES_RED_FUNGUS = createTag(HostileWorld.MODID, "becomes_red_fungus");
        public final static TagKey<Block> BECOMES_RED_MUSHROOM_BLOCK = createTag(HostileWorld.MODID, "becomes_red_mushroom_block");
        public final static TagKey<Block> BECOMES_BROWN_FUNGUS = createTag(HostileWorld.MODID, "becomes_brown_fungus");
        public final static TagKey<Block> BECOMES_BROWN_MUSHROOM_BLOCK = createTag(HostileWorld.MODID, "becomes_brown_mushroom_block");
        public final static TagKey<Block> BECOMES_MUSHROOM_STEM = createTag(HostileWorld.MODID, "becomes_mushroom_stem");
        public final static TagKey<Block> BECOMES_MYCELIUM = createTag(HostileWorld.MODID, "becomes_mycelium");
        public final static TagKey<Block> BECOMES_BROWN_MUSHROOM = createTag(HostileWorld.MODID, "becomes_brown_mushroom");
        public final static TagKey<Block> BECOMES_RED_MUSHROOM = createTag(HostileWorld.MODID, "becomes_red_mushroom");
        public final static TagKey<Block> BECOMES_RED_MYCOSTONE = createTag(HostileWorld.MODID, "becomes_red_mycostone");
        public final static TagKey<Block> BECOMES_BROWN_MYCOSTONE = createTag(HostileWorld.MODID, "becomes_brown_mycostone");
        public final static TagKey<Block> BECOMES_MIXED_MYCOSTONE = createTag(HostileWorld.MODID, "becomes_mixed_mycostone");
        public final static TagKey<Block> BECOMES_MYCORESTONE = createTag(HostileWorld.MODID, "becomes_mycorestone");
        public final static TagKey<Block> BECOMES_MYCOSTONE = createTag(HostileWorld.MODID, "becomes_mycostone");
        public final static TagKey<Block> BECOMES_MYCOTURF = createTag(HostileWorld.MODID, "becomes_mycoturf");
        public final static TagKey<Block> BECOMES_NASCENT_AUTOIMMUNE_CLUSTER = createTag(HostileWorld.MODID, "becomes_nascent_autoimmune_cluster");
        public final static TagKey<Block> BECOMES_MATURE_AUTOIMMUNE_CLUSTER = createTag(HostileWorld.MODID, "becomes_mature_autoimmune_cluster");
        public final static TagKey<Block> CONSUMED_BY_FUNGUS = createTag(HostileWorld.MODID, "consumed_by_fungus");
        public final static TagKey<Block> DROPS_SPORES = createTag(HostileWorld.MODID, "drops_spores");
        public final static TagKey<Block> MYCOFIRE_BURNS_TO_DIRT = createTag(HostileWorld.MODID, "mycofire_burns_to_dirt");
        public final static TagKey<Block> MYCOFIRE_BURNS_TO_STONE = createTag(HostileWorld.MODID, "mycofire_burns_to_stone");
        public final static TagKey<Block> MYCOFIRE_BURNS_TO_DEEPSLATE = createTag(HostileWorld.MODID, "mycofire_burns_to_deepslate");
        public final static TagKey<Block> MYCOSTONES = createTag(HostileWorld.MODID, "mycostones");
        public final static TagKey<Block> MYCOSTONE_BLOB_REPLACEABLES = createTag(HostileWorld.MODID, "mycostone_blob_replaceables");

        private static TagKey<Block> createTag(String namespace, String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, name));
        }
    }
}
