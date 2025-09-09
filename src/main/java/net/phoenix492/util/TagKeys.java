package net.phoenix492.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.phoenix492.hostileworld.HostileWorld;

public class TagKeys {
    public static class Biomes {
        public static TagKey<Biome> BUILDS_FUNGAL_INFECTION_SLOW = createTag(HostileWorld.MODID,"builds_fungal_infection_slow");
        public static TagKey<Biome> BUILDS_FUNGAL_INFECTION_STANDARD = createTag(HostileWorld.MODID,"builds_fungal_infection_standard");
        public static TagKey<Biome> BUILDS_FUNGAL_INFECTION_FAST = createTag(HostileWorld.MODID,"builds_fungal_infection_fast");
        public static TagKey<Biome> IS_MAGICAL = createTag("c", "is_magical");

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
        public static TagKey<Block> LEAVES = createTag("minecraft", "leaves");
        public static TagKey<Block> LOGS = createTag("minecraft", "logs");
        public static TagKey<Block> FLOWERS = createTag("minecraft", "flowers");
        public static TagKey<Block> REPLACEABLE = createTag("minecraft", "replaceable");

        // Parity with vanilla fire tags
        public static TagKey<Block> FIRE = createTag("minecraft", "fire");
        public static TagKey<Block> DRAGON_TRANSPARENT = createTag("minecraft", "dragon_transparent");
        public static TagKey<Block> ENCHANTMENT_POWER_TRANSMITTER = createTag("minecraft", "enchantment_power_transmitter");

        public static TagKey<Block> SPREADS_FUNGUS = createTag(HostileWorld.MODID, "spreads_fungus");
        public static TagKey<Block> BECOMES_FUNGUS = createTag(HostileWorld.MODID, "becomes_fungus");
        public static TagKey<Block> BECOMES_RED_FUNGUS = createTag(HostileWorld.MODID, "becomes_red_fungus");
        public static TagKey<Block> BECOMES_RED_MUSHROOM_BLOCK = createTag(HostileWorld.MODID, "becomes_red_mushroom_block");
        public static TagKey<Block> BECOMES_BROWN_FUNGUS = createTag(HostileWorld.MODID, "becomes_brown_fungus");
        public static TagKey<Block> BECOMES_BROWN_MUSHROOM_BLOCK = createTag(HostileWorld.MODID, "becomes_brown_mushroom_block");
        public static TagKey<Block> BECOMES_MUSHROOM_STEM = createTag(HostileWorld.MODID, "becomes_mushroom_stem");
        public static TagKey<Block> BECOMES_MYCELIUM = createTag(HostileWorld.MODID, "becomes_mycelium");
        public static TagKey<Block> BECOMES_BROWN_MUSHROOM = createTag(HostileWorld.MODID, "becomes_brown_mushroom");
        public static TagKey<Block> BECOMES_RED_MUSHROOM = createTag(HostileWorld.MODID, "becomes_red_mushroom");
        public static TagKey<Block> CONSUMED_BY_FUNGUS = createTag(HostileWorld.MODID, "consumed_by_fungus");
        public static TagKey<Block> DROPS_SPORES = createTag(HostileWorld.MODID, "drops_spores");
        public static TagKey<Block> MUSHROOM_FIRE_BURNS = createTag(HostileWorld.MODID, "mushroom_fire_burns");

        private static TagKey<Block> createTag(String namespace, String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, name));
        }
    }
}
