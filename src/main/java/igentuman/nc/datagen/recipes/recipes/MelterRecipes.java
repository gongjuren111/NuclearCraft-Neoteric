package igentuman.nc.datagen.recipes.recipes;

import igentuman.nc.recipes.ingredient.NcIngredient;
import igentuman.nc.content.processors.Processors;
import igentuman.nc.setup.registration.Fuel;
import igentuman.nc.content.materials.Materials;
import igentuman.nc.setup.registration.NCItems;
import igentuman.nc.content.fuel.FuelManager;
import igentuman.nc.content.materials.NCMaterial;
import net.minecraft.data.IFinishedRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.item.Items.OBSIDIAN;
import static net.minecraft.item.Items.SUGAR;

public class MelterRecipes extends AbstractRecipeProvider {

    public static void generate(Consumer<IFinishedRecipe> consumer) {
        MelterRecipes.consumer = consumer;
        ID = Processors.MELTER;
        for(String name: Materials.all().keySet()) {
            NCMaterial material = Materials.all().get(name);
            if(material.fluid && !material.isGas) {
                add(dustIngredient(name), fluidStack(name, 144));
                add(ingotIngredient(name), fluidStack(name, 144));
                add(oreIngredient(name), fluidStack(name, 288));
                add(chunkIngredient(name), fluidStack(name, 216));
            }
        }

        add(dustIngredient(Materials.polonium), fluidStack(Materials.polonium, 144));


        for (String name: Materials.isotopes()) {
            for(String type: new String[] {"", "_ox", "_ni", "_za"}) {
                String key = name+type;
                add(ingredient(Fuel.NC_ISOTOPES.get(key).get()), fluidStack(key, 144));
            }
        }

        for (String name: FuelManager.all().keySet()) {
            for(String subType: FuelManager.all().get(name).keySet()) {
                for (String type : new String[]{"", "za", "ox","ni"}) {

                    List<String> key = Arrays.asList("fuel", name, subType, type);

                    String keyStr = "fuel_"+name +"_"+ subType;
                    if(!type.isEmpty()){
                        keyStr += "_";
                    }
                    keyStr+= type;
                    add(ingredient(Fuel.NC_FUEL.get(key).get()), fluidStack(keyStr, 144));

                    key = Arrays.asList("depleted", name, subType, type);
                    keyStr = "depleted_"+keyStr;
                    add(ingredient(Fuel.NC_DEPLETED_FUEL.get(key).get()), fluidStack(keyStr, 144));
                }
            }
        }

        add(dustIngredient(Materials.sulfur), fluidStack(Materials.sulfur, 144), 0.5D, 3D);
        add(dustIngredient(Materials.sodium_hydroxide), fluidStack(Materials.sodium_hydroxide, 144));
        add(dustIngredient(Materials.potassium_hydroxide), fluidStack(Materials.potassium_hydroxide, 144));
        add(dustIngredient(Materials.arsenic), fluidStack(Materials.arsenic, 144));
        add(gemIngredient(Materials.boron_arsenide), fluidStack(Materials.boron_arsenide, 144));
        add(ingredient(OBSIDIAN), fluidStack(Materials.obsidian, 288), 2D, 2D);

        add(ingredient(NCItems.NC_ITEMS.get("ground_cocoa_nibs").get()), fluidStack("chocolate_liquor", 144), 0.25D, 0.5D);
        add(ingredient(NCItems.NC_ITEMS.get("cocoa_butter").get()), fluidStack("cocoa_butter", 144), 0.25D, 0.5D);
        add(ingredient(NCItems.NC_ITEMS.get("unsweetened_chocolate").get()), fluidStack("unsweetened_chocolate", 144), 0.25D, 0.5D);
        add(ingredient(NCItems.NC_FOOD.get("dark_chocolate").get()), fluidStack("dark_chocolate", 144), 0.25D, 0.5D);
        add(ingredient(NCItems.NC_FOOD.get("milk_chocolate").get()), fluidStack("milk_chocolate", 144), 0.25D, 0.5D);
        add(ingredient(SUGAR), fluidStack("sugar", 144), 0.5D, 0.5D);
        add(ingredient(NCItems.NC_ITEMS.get("gelatin").get()), fluidStack("gelatin", 144), 0.5D, 0.5D);
        add(ingredient(NCItems.NC_FOOD.get("marshmallow").get()), fluidStack("marshmallow", 144), 0.5D, 0.5D);

    }

    protected static void add(NcIngredient inputItem, FluidStack outputFluid, double...modifiers) {
        try {
            itemsAndFluids(Arrays.asList(inputItem), new ArrayList<>(), new ArrayList<>(), Arrays.asList(outputFluid), modifiers);
        } catch(IllegalStateException ignored) {}
    }
}
