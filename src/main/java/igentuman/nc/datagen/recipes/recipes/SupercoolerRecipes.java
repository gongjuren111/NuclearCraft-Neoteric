package igentuman.nc.datagen.recipes.recipes;

import igentuman.nc.content.processors.Processors;
import igentuman.nc.recipes.ingredient.FluidStackIngredient;
import net.minecraft.data.IFinishedRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


public class SupercoolerRecipes extends AbstractRecipeProvider {

    public static void generate(Consumer<IFinishedRecipe> consumer) {
        SupercoolerRecipes.consumer = consumer;
        ID = Processors.SUPERCOOLER;

        for(String gas: Arrays.asList("oxygen", "hydrogen", "nitrogen", "helium")) {
            add(
                    fluidIngredient(gas, 1000),
                    fluidStack("liquid_" + gas, 100)
            );
        }

        add(
                fluidIngredient("slurry_ice", 1000),
                fluidStack("cryotheum", 1000)
        );

        add(
                fluidIngredient("minecraft:water", 1000),
                fluidStack("ice", 500)
        );

        add(
                fluidIngredient("exhaust_steam", 1000),
                fluidStack("condensate_water", 1000)
        );

        add(
                fluidIngredient("condensate_water", 1000),
                fluidStack("technical_water", 1000)
        );

        add(
                fluidIngredient("emergency_coolant_heated", 1000),
                fluidStack("emergency_coolant", 1000)
        );
    }

    protected static void add(FluidStackIngredient input, FluidStack output, double...modifiers) {
        fluidsAndFluids(Arrays.asList(input), Arrays.asList(output), modifiers);
    }
}
