package igentuman.nc.world.dimension;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import static igentuman.nc.NuclearCraft.MODID;

public class Dimensions {

    public static final ResourceKey<Level> WASTELAND = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MODID, "wasteland"));
    public static int WASTELAIND_ID = -4848;

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new ResourceLocation(MODID, "wasteland_chunkgen"),
                WastelandChunkGenerator.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new ResourceLocation(MODID, "biomes"),
                WastelandBiomeProvider.CODEC);
    }
}