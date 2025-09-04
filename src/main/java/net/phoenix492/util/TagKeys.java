package net.phoenix492.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.phoenix492.hostileworld.HostileWorld;

public class TagKeys {
    public static class Biomes {
        public static TagKey<Biome> TICKS_FUNGAL_INFECTION = createTag("ticks_fungal_infection");

        private static TagKey<Biome> createTag(String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HostileWorld.MODID, name));
        }
    }
}
