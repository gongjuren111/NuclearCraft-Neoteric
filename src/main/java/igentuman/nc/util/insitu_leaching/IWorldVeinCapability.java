package igentuman.nc.util.insitu_leaching;

import igentuman.nc.recipes.type.OreVeinRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;


public interface IWorldVeinCapability extends INBTSerializable<CompoundNBT> {

    OreVeinRecipe getVeinForChunk(int chunkX, int chunkZ);

    boolean chunkContainsVein(int chunkX, int chunkZ);

    int getBlocksLeft(int chunkX, int chunkZ);

    void mineBlock(int chunkX, int chunkZ);
}
