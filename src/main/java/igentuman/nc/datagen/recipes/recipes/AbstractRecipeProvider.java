package igentuman.nc.datagen.recipes.recipes;

import igentuman.nc.datagen.recipes.builder.NcRecipeBuilder;
import igentuman.nc.recipes.ingredient.FluidStackIngredient;
import igentuman.nc.recipes.ingredient.NcIngredient;
import igentuman.nc.recipes.ingredient.creator.IngredientCreatorAccess;
import igentuman.nc.setup.registration.Fuel;
import igentuman.nc.util.TagUtil;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Consumer;

import static igentuman.nc.NuclearCraft.rl;
import static igentuman.nc.setup.registration.Fuel.*;
import static igentuman.nc.setup.registration.NCFluids.ALL_FLUID_ENTRIES;
import static igentuman.nc.setup.registration.NCItems.*;
import static igentuman.nc.util.DataGenUtil.*;
import static net.minecraft.world.item.Items.AIR;
import static net.minecraft.world.item.Items.BARRIER;

public abstract class AbstractRecipeProvider {

    public static String ID;

    public static Consumer<FinishedRecipe> consumer;
    private static List<NcIngredient> input;
    private static List<NcIngredient> output;
    private static double[] params;

    protected static NcIngredient ingredient(Tag.Named<Item> item, int...count) {
        return NcIngredient.of(item, count);
    }

    protected static NcIngredient ingredient(Item item, int...pCount) {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return NcIngredient.stack(stack(item, count));
    }

    protected static NcIngredient blockStack(String name, int...pCount) {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return NcIngredient.stack(stack(blockItem(name), count));
    }

    protected static ItemStack stack(Item item, int count) {
        return new ItemStack(item, count);
    }

    protected static ItemStack stack(Block block, int count) {
        return new ItemStack(block, count);
    }

    protected static ItemStack stack(String item, int count) {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(item)), count);
    }

    public static ItemStack[] stackArray(ItemStack... stacks) {
        return stacks;
    }

    protected static void doubleToItem(String id, NcIngredient input1, NcIngredient input2, NcIngredient output, double... params) {

        double timeModifier = params.length>0 ? params[0] : 1.0;
        double powerModifier = params.length>1 ? params[1] : 1.0;
        double radiation = params.length>2 ? params[2] : 1.0;
        NcRecipeBuilder.get(id)
                .items(List.of(input1, input2), List.of(output))
                .modifiers(timeModifier, radiation, powerModifier)
                .build(consumer);
    }

    protected static FluidStack fluidStack(Fluid fluid, int amount) {
        try {
            return IngredientCreatorAccess.fluid().from(fluid, amount).getRepresentations().get(0);
        } catch (NullPointerException e) {
            throw new NullPointerException("Fluid " + fluid.getRegistryName().toString() + " does not exist");
        }
    }

    protected static FluidStack fluidStack(String name, int amount) {
        try {
            return IngredientCreatorAccess.fluid().from(name, amount).getRepresentations().get(0);
        } catch (NullPointerException e) {
            System.out.println("Fluid " + name + " does not exist");
        }
        return FluidStack.EMPTY;
    }

    protected static FluidStackIngredient fluidIngredient(String name, int amount) {
        return IngredientCreatorAccess.fluid().from(forgeFluid(name), amount);
    }

    protected static FluidStackIngredient fluidStackIngredient(String name, int amount) {
        return IngredientCreatorAccess.fluid().from(ALL_FLUID_ENTRIES.get(name).getStill(), amount);
    }

    public static void itemToItem(NcIngredient input, NcIngredient output, double...params) {
        double timeModifier = params.length>0 ? params[0] : 1.0;
        double powerModifier = params.length>1 ? params[1] : 1.0;
        double radiation = params.length>2 ? params[2] : 1.0;
        NcRecipeBuilder.get(ID)
                .items(List.of(input), List.of(output))
                .modifiers(timeModifier, radiation, powerModifier)
                .build(consumer);
    }

    public static void itemsToItems(List<NcIngredient> input, List<NcIngredient> output, double...params) {
        double timeModifier = params.length>0 ? params[0] : 1.0;
        double powerModifier = params.length>1 ? params[1] : 1.0;
        double radiation = params.length>2 ? params[2] : 1.0;
        NcRecipeBuilder.get(ID)
                .items(input, output)
                .modifiers(timeModifier, radiation, powerModifier)
                .build(consumer);
    }

    public static void itemsToItemsString(List<NcIngredient> input, List<String> output, double...params) {
        double timeModifier = params.length>0 ? params[0] : 1.0;
        double powerModifier = params.length>1 ? params[1] : 1.0;
        double radiation = params.length>2 ? params[2] : 1.0;
        NcRecipeBuilder.get(ID)
                .itemsString(input, output)
                .modifiers(timeModifier, radiation, powerModifier)
                .build(consumer);
    }

    public static void oreVein(List<NcIngredient> input, NcIngredient output, String nameKey, double...params) {
        double timeModifier = params.length>0 ? params[0] : 1.0;
        double powerModifier = params.length>1 ? params[1] : 1.0;
        double radiation = params.length>2 ? params[2] : 1.0;
        double rarity = params.length>3 ? params[3] : 1.0;
        NcRecipeBuilder.get(ID)
                .items(input, List.of(output))
                .modifiers(timeModifier, radiation, powerModifier, rarity)
                .build(consumer, rl(ID+"/"+nameKey));
    }


    public static void fluidsAndFluids(List<FluidStackIngredient> input, List<FluidStack> output, double...params) {
        double timeModifier = params.length>0 ? params[0] : 1.0;
        double powerModifier = params.length>1 ? params[1] : 1.0;
        double radiation = params.length>2 ? params[2] : 1.0;
        NcRecipeBuilder.get(ID)
                .fluids(input, output)
                .modifiers(timeModifier, radiation, powerModifier)
                .build(consumer);
    }

    public static void coolantRecipe(List<FluidStackIngredient> input, List<FluidStack> output, double coolingRate) {
        NcRecipeBuilder.get(ID)
                .fluids(input, output)
                .modifiers(0,0,0, 0)
                .coolingRate(coolingRate)
                .build(consumer);
    }

    public static void boilingRecipe(List<FluidStackIngredient> input, List<FluidStack> output, double heatRequired) {
        NcRecipeBuilder.get(ID)
                .fluids(input, output)
                .modifiers(0,0,0, 0)
                .heatRequired(heatRequired)
                .useInputForId(true)
                .build(consumer);
    }

    public static void itemsAndFluids(
            List<NcIngredient> inputItems, List<NcIngredient> outputItems,
            List<FluidStackIngredient> inputFluids, List<FluidStack> outputFluids,
            double...params) {
        double timeModifier = params.length>0 ? params[0] : 1.0;
        double powerModifier = params.length>1 ? params[1] : 1.0;
        double radiation = params.length>2 ? params[2] : 1.0;
        NcRecipeBuilder.get(ID)
                .items(inputItems, outputItems)
                .fluids(inputFluids, outputFluids)
                .modifiers(timeModifier, radiation, powerModifier)
                .build(consumer);
    }

    public static Tag.Named<Fluid> forgeFluid(String name) {
        String key = "forge";
        if(name.contains(":")) {
            key = name.split(":")[0];
            name = name.split(":")[1];
        }
        return TagUtil.createFluidTagKey(new ResourceLocation(key, name));
    }

    public static Item blockItem(String name)
    {
        for(String key: List.of(name, "block_"+name, name+"_block")) {
            if(ALL_NC_ITEMS.get(name) != null) {
                return ALL_NC_ITEMS.get(key).get();
            }
        }
        System.out.println("null block: " + name);
        return BARRIER;
    }

    public static Item nuggetItem(String name)
    {
        if(NC_NUGGETS.get(name) == null) {
            System.out.println("null nugget: " + name);
        }
        return NC_NUGGETS.get(name).get();
    }

    public static Item dustItem(String name)
    {
        if(NC_DUSTS.get(name) == null) {
            System.out.println("null dust: " + name);
        }
        return NC_DUSTS.get(name).get();
    }

    public static Tag.Named<Item> dustTag(String name)
    {
        if(DUSTS_TAG.get(name) == null) {
            System.out.println("null dust tag: " + name);
        }
        return DUSTS_TAG.get(name);
    }

    public static NcIngredient dustStack(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return NcIngredient.stack(stack(dustItem(name), count));
    }

    public static NcIngredient nuggetStack(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return NcIngredient.stack(stack(nuggetItem(name), count));
    }

    public static NcIngredient ingotStack(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return NcIngredient.stack(stack(ingotItem(name), count));
    }

    public static NcIngredient gemStack(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return NcIngredient.stack(stack(gemItem(name), count));
    }

    public static NcIngredient plateStack(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return NcIngredient.stack(stack(plateItem(name), count));
    }

    public static NcIngredient isotopeStack(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return NcIngredient.stack(stack(isotopeItem(name), count));
    }

    public static NcIngredient dustIngredient(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return ingredient(forgeDust(name), count);
    }

    public static FluidStackIngredient moltenFuelIngredient(List<String> name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return fluidStackIngredient(fuelItem(name).toString(), count);
    }

    public static NcIngredient fuelIngredient(List<String> name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return ingredient(fuelItem(name), count);
    }

    public static Item fuelItem(List<String> name) {
        if(NC_FUEL.get(name) != null) {
            return NC_FUEL.get(name).get();
        }
        if(NC_DEPLETED_FUEL.get(name) != null) {
            return NC_DEPLETED_FUEL.get(name).get();
        }
        System.out.println("null fuel: " + String.join("-", name));
        return AIR;
    }


    public static NcIngredient isotopeIngredient(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return ingredient(isotopeItem(name), count);
    }

    public static NcIngredient oreIngredient(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return ingredient(forgeOre(name), count);
    }
    public static NcIngredient chunkIngredient(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return ingredient(forgeChunk(name), count);
    }

    public static NcIngredient ingotIngredient(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return ingredient(forgeIngot(name), count);
    }

    public static NcIngredient plateIngredient(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return ingredient(forgePlate(name), count);
    }


    public static NcIngredient gemIngredient(String name, int...pCount)
    {
        int count = 1;
        if(pCount.length > 0) count = pCount[0];
        return ingredient(forgeGem(name), count);
    }

    public static Item isotopeItem(String name)
    {
        if(NC_ISOTOPES.get(name) == null) {
            System.out.println("null isotope: " + name);
        }
        return NC_ISOTOPES.get(name).get();
    }

    public static Item plateItem(String name)
    {
        if(NC_PLATES.get(name) == null) {
            System.out.println("null plate: " + name);
        }
        return NC_PLATES.get(name).get();
    }

    public static Tag.Named<Item> plateTag(String name)
    {
        if(PLATES_TAG.get(name) == null) {
            System.out.println("null plate tag: " + name);
        }
        return PLATES_TAG.get(name);
    }

    public static Item ingotItem(String name)
    {
        if(NC_INGOTS.get(name) == null) {
            System.out.println("null ingot: " + name);
        }
        return NC_INGOTS.get(name).get();
    }

    public static Tag.Named<Item> ingotTag(String name)
    {
        if(INGOTS_TAG.get(name) == null) {
            System.out.println("null ingot tag: " + name);
        }
        return INGOTS_TAG.get(name);
    }

    public static Tag.Named<Item> gemTag(String name) {
        if(GEMS_TAG.get(name) == null) {
            System.out.println("null gem tag: " + name);
        }
        return GEMS_TAG.get(name);
    }

    public static Item gemItem(String name)
    {
        if(NC_GEMS.get(name) == null) {
            System.out.println("null gem: " + name);
        }
        return NC_GEMS.get(name).get();
    }

    public static Item getIsotope(String name, String id, String type)
    {
        if(!type.isEmpty()) {
            type = "_"+type;
        }
        if(!Fuel.NC_ISOTOPES.containsKey(name+"/"+id+type)) {
            for(String isotope: Fuel.NC_ISOTOPES.keySet()) {
                if(isotope.contains(id)) {
                    return  Fuel.NC_ISOTOPES.get(isotope).get();
                }
            }
        }
        return Fuel.NC_ISOTOPES.get(name+"/"+id+type).get();
    }
}
