package igentuman.nc.datagen.recipes.recipes;

import igentuman.nc.content.processors.Processors;
import igentuman.nc.recipes.ingredient.FluidStackIngredient;
import igentuman.nc.content.materials.Materials;
import net.minecraft.data.IFinishedRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class CentrifugeRecipes extends AbstractRecipeProvider {

    public static void generate(Consumer<IFinishedRecipe> consumer) {
        CentrifugeRecipes.consumer = consumer;
        ID = Processors.CENTRIFUGE;

        add(
                fluidIngredient("carbon_dioxide", 1000),
                Arrays.asList(
                        fluidStack("carbon_dioxide", 125),
                        fluidStack("carbon_monoxide", 125),
                        fluidStack("carbon", 375),
                        fluidStack("oxygen", 375)
                )
        );

        add(
                fluidIngredient("irradiated_boron", 1000),
                Arrays.asList(
                        fluidStack(Materials.boron10, 500),
                        fluidStack(Materials.boron11, 500)
                ), 1.5D
        );

        add(
                fluidIngredient("irradiated_lithium", 1000),
                Arrays.asList(
                        fluidStack(Materials.lithium6, 250),
                        fluidStack(Materials.lithium7, 250),
                        fluidStack("tritium", 500)
                )
        );

        add(
                fluidIngredient("technical_water", 1000),
                Arrays.asList(
                        fluidStack("deuterium", 750),
                        fluidStack("oxygen", 250)
                ), 1.9D
        );

        add(
                fluidIngredient(Materials.uranium, 160),
                Arrays.asList(
                        fluidStack(Materials.uranium238, 144),
                        fluidStack(Materials.uranium235, 16)
                ), 0.9D
        );

        add(
                fluidIngredient("fissile_fuel", 500),
                Arrays.asList(
                        fluidStack(Materials.uranium238, 300),
                        fluidStack(Materials.uranium235, 80),
                        fluidStack(Materials.uranium233, 80),
                        fluidStack("hydrofluoric_acid", 16),
                        fluidStack("sulfuric_acid", 16)

                ), 1.9D
        );

        add(
                fluidIngredient("nuclear_waste", 50),
                Arrays.asList(
                        fluidStack(Materials.polonium, 5),
                        fluidStack(Materials.plutonium238, 5),
                        fluidStack(Materials.plutonium242, 5),
                        fluidStack(Materials.plutonium241, 5),
                        fluidStack("spent_nuclear_waste", 1)

                ), 15D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "americium", "hea-242", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.americium243, 432), fluidStack(Materials.curium243, 144),
                        fluidStack(Materials.curium246, 288), fluidStack(Materials.berkelium247, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.promethium_147, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "americium", "lea-242", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.americium243, 432), fluidStack(Materials.curium243, 144),
                        fluidStack(Materials.curium246, 432), fluidStack(Materials.berkelium248, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.promethium_147, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "thorium", "tbu", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.uranium233, 144), fluidStack(Materials.uranium238, 720),
                        fluidStack(Materials.neptunium236, 144), fluidStack(Materials.neptunium237, 144),
                        fluidStack(Materials.strontium_90, 144), fluidStack(Materials.caesium_137, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "uranium", "leu-233", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.uranium238, 720), fluidStack(Materials.plutonium241, 144),
                        fluidStack(Materials.plutonium242, 144), fluidStack(Materials.americium243, 144),
                        fluidStack(Materials.strontium_90, 144), fluidStack(Materials.caesium_137, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "uranium", "heu-233", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.uranium235, 144), fluidStack(Materials.uranium238, 288),
                        fluidStack(Materials.plutonium242, 432), fluidStack(Materials.americium243, 144),
                        fluidStack(Materials.strontium_90, 144), fluidStack(Materials.caesium_137, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "uranium", "leu-235", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.uranium238, 576), fluidStack(Materials.plutonium239, 144),
                        fluidStack(Materials.plutonium242, 144), fluidStack(Materials.americium243, 144),
                        fluidStack(Materials.strontium_90, 144), fluidStack(Materials.caesium_137, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "uranium", "heu-235", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.uranium238, 288), fluidStack(Materials.plutonium239, 144),
                        fluidStack(Materials.plutonium242, 432), fluidStack(Materials.americium243, 144),
                        fluidStack(Materials.strontium_90, 144), fluidStack(Materials.caesium_137, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "neptunium", "len-236", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.uranium238, 576), fluidStack(Materials.neptunium237, 144),
                        fluidStack(Materials.plutonium241, 144), fluidStack(Materials.plutonium242, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.caesium_137, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "neptunium", "hen-236", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.uranium238, 576), fluidStack(Materials.plutonium238, 144),
                        fluidStack(Materials.plutonium241, 144), fluidStack(Materials.plutonium242, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.caesium_137, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "plutonium", "lep-239", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.curium246, 576), fluidStack(Materials.americium243, 144),
                        fluidStack(Materials.americium242, 144), fluidStack(Materials.plutonium242, 720),
                        fluidStack(Materials.strontium_90, 144), fluidStack(Materials.caesium_137, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "plutonium", "hep-239", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.americium243, 576), fluidStack(Materials.plutonium238, 144),
                        fluidStack(Materials.plutonium241, 144), fluidStack(Materials.americium242, 144),
                        fluidStack(Materials.caesium_137, 144), fluidStack(Materials.strontium_90, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "plutonium", "lep-241", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.plutonium242, 720), fluidStack(Materials.americium243, 144),
                        fluidStack(Materials.curium246, 144), fluidStack(Materials.berkelium247, 144),
                        fluidStack(Materials.promethium_147, 144), fluidStack(Materials.caesium_137, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "plutonium", "hep-241", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.americium243, 576), fluidStack(Materials.curium243, 144),
                        fluidStack(Materials.americium242, 144), fluidStack(Materials.plutonium241, 144),
                        fluidStack(Materials.strontium_90, 144), fluidStack(Materials.caesium_137, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "mixed", "mix-239", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.uranium238, 576), fluidStack(Materials.plutonium239, 144),
                        fluidStack(Materials.plutonium242, 144), fluidStack(Materials.americium243, 144),
                        fluidStack(Materials.strontium_90, 144), fluidStack(Materials.caesium_137, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "mixed", "mix-241", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.uranium238, 576), fluidStack(Materials.neptunium237, 144),
                        fluidStack(Materials.plutonium241, 144), fluidStack(Materials.plutonium242, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.caesium_137, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "curium", "lecm-243", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.curium246, 576), fluidStack(Materials.curium247, 144),
                        fluidStack(Materials.berkelium247, 288), fluidStack(Materials.berkelium248, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.promethium_147, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "curium", "hecm-243", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.curium245, 432), fluidStack(Materials.curium245, 144),
                        fluidStack(Materials.berkelium247, 288), fluidStack(Materials.berkelium248, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.promethium_147, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "curium", "lecm-245", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.curium246, 576), fluidStack(Materials.curium247, 144),
                        fluidStack(Materials.berkelium247, 288), fluidStack(Materials.californium249, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.europium_155, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "curium", "hecm-245", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.curium246, 432), fluidStack(Materials.curium247, 144),
                        fluidStack(Materials.berkelium247, 288), fluidStack(Materials.californium249, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.europium_155, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "curium", "lecm-247", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.curium246, 720), fluidStack(Materials.berkelium247, 144),
                        fluidStack(Materials.californium249, 144), fluidStack(Materials.berkelium248, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.europium_155, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "curium", "hecm-247", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.californium251, 144), fluidStack(Materials.californium249, 144),
                        fluidStack(Materials.berkelium247, 576), fluidStack(Materials.berkelium248, 144),
                        fluidStack(Materials.molybdenum, 144), fluidStack(Materials.europium_155, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "berkelium", "leb-248", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.berkelium247, 720), fluidStack(Materials.berkelium248, 144),
                        fluidStack(Materials.californium249, 144), fluidStack(Materials.californium251, 144),
                        fluidStack(Materials.ruthenium_106, 144), fluidStack(Materials.promethium_147, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "berkelium", "heb-248", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.berkelium248, 144), fluidStack(Materials.californium249, 144),
                        fluidStack(Materials.californium251, 288), fluidStack(Materials.californium252, 3),
                        fluidStack(Materials.ruthenium_106, 144), fluidStack(Materials.promethium_147, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "californium", "lecf-249", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.californium252, 1152),
                        fluidStack(Materials.ruthenium_106, 144), fluidStack(Materials.promethium_147, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "californium", "hecf-249", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.californium252, 864), fluidStack(Materials.californium250, 288),
                        fluidStack(Materials.ruthenium_106, 144), fluidStack(Materials.promethium_147, 144)
                ),1.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "californium", "lecf-251", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.californium252, 1152),
                        fluidStack(Materials.ruthenium_106, 144), fluidStack(Materials.promethium_147, 144)
                ),0.5D
        );

        add(
                moltenFuelIngredient(Arrays.asList("depleted", "californium", "hecf-251", ""), 1296),
                Arrays.asList(
                        fluidStack(Materials.californium252, 1008),
                        fluidStack(Materials.ruthenium_106, 144), fluidStack(Materials.promethium_147, 144)
                ),1.5D
        );

        for(String material: Materials.slurries()) {
            add(
                    fluidIngredient(material+"_slurry", 1000),
                    Arrays.asList(
                            fluidStack(material+"_clean_slurry", 800),
                            fluidStack("hydrochloric_acid", 50),
                            fluidStack("nitric_acid", 50),
                            fluidStack("calcium_sulfate_solution", 10)
                    )
            );
        }
    }

    protected static void add(FluidStackIngredient input, List<FluidStack> output, double...modifiers) {
        fluidsAndFluids(Arrays.asList(input), output, modifiers);
    }
}
