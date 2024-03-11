package igentuman.nc.datagen.recipes.recipes;

import igentuman.nc.datagen.recipes.builder.NcRecipeBuilder;
import igentuman.nc.recipes.ingredient.FluidStackIngredient;
import net.minecraft.data.IFinishedRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FusionReactorRecipes extends AbstractRecipeProvider {

    public static void generate(Consumer<IFinishedRecipe> consumer) {
        FusionReactorRecipes.consumer = consumer;
        ID = "fusion_core";

        add(
                Arrays.asList(
                        fluidIngredient("deuterium", 250),
                        fluidIngredient("tritium", 250)
                ),
                Arrays.asList(
                        fluidStack("helium", 65),
                        fluidStack("helium", 65),
                        fluidStack("helium", 65),
                        fluidStack("helium", 65)
                ), 20D, 110000D, 1.5D, 816D
        );

        
        add(
                Arrays.asList(
                        fluidIngredient("hydrogen", 1000),
                        fluidIngredient("deuterium", 1000)
                ),
                Arrays.asList(
                        fluidStack("helium_3", 250),
                        fluidStack("helium_3", 250),
                        fluidStack("helium_3", 250),
                        fluidStack("helium_3", 250)
                ), 15D, 110000D, 1.5D, 1245D
        );
        
        add(
                Arrays.asList(
                        fluidIngredient("hydrogen", 1000),
                        fluidIngredient("tritium", 1000)
                ),
                Arrays.asList(
                        fluidStack("helium_3", 250),
                        fluidStack("helium_3", 250),
                        fluidStack("helium_3", 250),
                        fluidStack("helium_3", 250)
                ), 20D, 190000D, 1.5D, 6050D
        );
        
        add(
                Arrays.asList(
                        fluidIngredient("hydrogen", 1000),
                        fluidIngredient("helium_3", 1000)
                ),
                Arrays.asList(
                        fluidStack("helium", 250),
                        fluidStack("helium", 250),
                        fluidStack("helium", 250),
                        fluidStack("helium", 250)
                ), 20D, 150000D, 1.5D, 3339D
        );
        
        add(
                Arrays.asList(
                        fluidIngredient("hydrogen", 1000),
                        fluidIngredient("lithium/6", 144)
                ),
                Arrays.asList(
                        fluidStack("tritium", 500),
                        fluidStack("tritium", 500),
                        fluidStack("helium", 500),
                        fluidStack("helium", 500)
                ), 35D, 135100D, 1.5D,7278D
        );
        
        add(
                Arrays.asList(
                        fluidIngredient("hydrogen", 1000),
                        fluidIngredient("lithium/7", 144)
                ),
                Arrays.asList(
                        fluidStack("helium", 500),
                        fluidStack("helium", 500),
                        fluidStack("helium", 500),
                        fluidStack("helium", 500)
                ), 40D, 133000D, 1.5D, 5071D
        );
        
        add(
                Arrays.asList(
                        fluidIngredient("hydrogen", 1000),
                        fluidIngredient("boron/11", 144)
                ),
                Arrays.asList(
                        fluidStack("helium", 1000*3/4),
                        fluidStack("helium", 1000*3/4),
                        fluidStack("helium", 1000*3/4),
                        fluidStack("helium", 1000*3/4)
                ), 60D, 284400D, 1.5D, 16370D
        );


        add(
                Arrays.asList(
                        fluidIngredient("deuterium", 1000),
                        fluidIngredient("helium_3", 1000)
                ),
                Arrays.asList(
                        fluidStack("hydrogen", 500),
                        fluidStack("hydrogen", 500),
                        fluidStack("helium", 500),
                        fluidStack("helium", 500)
                ), 20D, 150700D, 1.5D, 1156D
        );
        
        add(
                Arrays.asList(
                        fluidIngredient("deuterium", 1000),
                        fluidIngredient("lithium/6", 144)
                ),
                Arrays.asList(
                        fluidStack("helium", 500),
                        fluidStack("helium", 500),
                        fluidStack("helium", 500),
                        fluidStack("helium", 500)
                ), 25D, 225000D, 1.5D, 2632D
        );
        
        add(
                Arrays.asList(
                        fluidIngredient("deuterium", 1000),
                        fluidIngredient("lithium/7", 144)
                ),
                Arrays.asList(
                        fluidStack("helium", 500),
                        fluidStack("helium", 500),
                        fluidStack("helium", 500),
                        fluidStack("helium", 500)
                ), 45D, 229000D, 1.5D, 5034D
        );
        
        add(
                Arrays.asList(
                        fluidIngredient("deuterium", 1000),
                        fluidIngredient("boron/11", 144)
                ),
                Arrays.asList(
                        fluidStack("helium", 1000*6/4),
                        fluidStack("helium", 1000*6/4)
                ), 65D, 226100D, 1.5D, 16883D
        );

        add(
                Arrays.asList(
                        fluidIngredient("tritium", 1000),
                        fluidIngredient("helium_3", 1000)
                ),
                Arrays.asList(
                        fluidStack("helium", 250),
                        fluidStack("helium", 250),
                        fluidStack("helium", 250),
                        fluidStack("helium", 250)
                ), 30D, 209100D, 1.5D, 2604D
        );

        add(
                Arrays.asList(
                        fluidIngredient("tritium", 1000),
                        fluidIngredient("lithium/6", 144)
                ),
                Arrays.asList(
                        fluidStack("helium", 1000),
                        fluidStack("helium", 1000)
                ), 45D, 191500D, 1.5D, 4971D
        );

        add(
                Arrays.asList(
                        fluidIngredient("tritium", 1000),
                        fluidIngredient("lithium/7", 144)
                ),
                Arrays.asList(
                        fluidStack("helium", 1000),
                        fluidStack("helium", 1000)
                ), 50D, 143500D, 1.5D, 5511D
        );

        add(
                Arrays.asList(
                        fluidIngredient("tritium", 1000),
                        fluidIngredient("boron/11", 144)
                ),
                Arrays.asList(
                        fluidStack("helium", 1000*6/4),
                        fluidStack("helium", 1000*6/4)
                ), 70D, 400000D, 1.5D, 33215D
        );

        add(
                Arrays.asList(fluidIngredient("helium_3", 1000),
                        fluidIngredient("lithium/6", 144)
                ),
                Arrays.asList(
                        fluidStack("hydrogen", 500),
                        fluidStack("hydrogen", 500),
                        fluidStack("helium", 1000),
                        fluidStack("helium", 1000)
                ), 45D, 265000D, 1.5D, 9506D
        );

        add(
                Arrays.asList(fluidIngredient("helium_3", 1000),
                        fluidIngredient("lithium/7", 144)
                ),
                Arrays.asList(
                        fluidStack("deuterium", 500),
                        fluidStack("deuterium", 500),
                        fluidStack("helium", 1000),
                        fluidStack("helium", 1000)
                ), 50D, 222700D, 1.5D, 9673D
        );

        add(
                Arrays.asList(fluidIngredient("helium_3", 1000),
                        fluidIngredient("boron/11", 144)
                ),
                Arrays.asList(
                        fluidStack("deuterium", 500),
                        fluidStack("deuterium", 500),
                        fluidStack("helium", 1000*6/4),
                        fluidStack("helium", 1000*6/4)
                ), 70D, 574000D, 1.5D, 29574D
        );

        add(
                Arrays.asList(fluidIngredient("lithium/6", 144*4),
                        fluidIngredient("lithium/7", 144*4)
                ),
                Arrays.asList(
                        fluidStack("helium", 1000*6/2),
                        fluidStack("helium", 1000*6/2)
                ), 65D, 255200D, 1.5D, 14536D
        );

        add(
                Arrays.asList(fluidIngredient("lithium/6", 144*7),
                        fluidIngredient("boron/11", 144*7)
                ),
                Arrays.asList(
                        fluidStack("helium", 1000*2),
                        fluidStack("helium", 1000*2)
                ), 85D, 615700D, 1.5D, 37048D
        );

        add(
                Arrays.asList(fluidIngredient("lithium/7", 144*9),
                        fluidIngredient("boron/11", 144*9)
                ),
                Arrays.asList(
                        fluidStack("helium", 1000*2),
                        fluidStack("helium", 1000*2)
                ), 90D, 700000D, 3.5D, 202000D
        );

    }

    protected static void add(List<FluidStackIngredient> input, List<FluidStack> output, double...modifiers) {
        double timeModifier = modifiers.length>0 ? modifiers[0] : 1.0;
        double powerModifier = modifiers.length>1 ? modifiers[1] : 1.0;
        double radiation = modifiers.length>2 ? modifiers[2] : 1.0;
        double temperature = modifiers.length>3 ? modifiers[3] : 1.0;
        NcRecipeBuilder.get(ID)
                .fluids(input, output)
                .modifiers(timeModifier, radiation, powerModifier)
                .temperature(temperature)
                .build(consumer);
    }
}
