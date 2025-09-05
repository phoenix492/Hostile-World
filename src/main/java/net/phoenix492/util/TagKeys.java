package net.phoenix492.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
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
}
