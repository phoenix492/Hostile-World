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
        public static TagKey<Block> SPREADS_FUNGUS = createTag(HostileWorld.MODID, "spreads_fungus");

        private static TagKey<Block> createTag(String namespace, String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, name));
        }
    }
}
