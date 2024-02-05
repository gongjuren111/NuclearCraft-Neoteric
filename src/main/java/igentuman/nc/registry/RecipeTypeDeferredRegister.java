package igentuman.nc.registry;

import igentuman.nc.recipes.INcRecipeTypeProvider;
import igentuman.nc.recipes.NcRecipeType;
import igentuman.nc.recipes.type.NcRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RecipeTypeDeferredRegister extends WrappedDeferredRegister<RecipeType<?>> {

    private final List<INcRecipeTypeProvider<?>> recipeTypes = new ArrayList<>();

    public RecipeTypeDeferredRegister(String modid) {
        super(modid, Registry.RECIPE_TYPE_REGISTRY);
    }

    public <RECIPE extends NcRecipe> RecipeTypeRegistryObject<RECIPE> register(String name, Supplier<? extends NcRecipeType<RECIPE>> sup) {
        RecipeTypeRegistryObject<RECIPE> registeredRecipeType = register(name, sup, RecipeTypeRegistryObject::new);
        recipeTypes.add(registeredRecipeType);
        return registeredRecipeType;
    }

}