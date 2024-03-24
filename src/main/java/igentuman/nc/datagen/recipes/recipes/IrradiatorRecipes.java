package igentuman.nc.datagen.recipes.recipes;

import igentuman.nc.content.materials.Materials;
import igentuman.nc.content.processors.Processors;
import igentuman.nc.recipes.ingredient.NcIngredient;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.world.level.block.Blocks.*;


public class IrradiatorRecipes extends AbstractRecipeProvider {

    public static void generate(Consumer<FinishedRecipe> consumer) {
        IrradiatorRecipes.consumer = consumer;
        ID = Processors.IRRADIATOR;

        itemToItem(dustIngredient(Materials.thorium), dustIngredient(Materials.tbp), 1.5D);
        itemsAndFluids(
                List.of(ingredient(Item.byBlock(COBBLESTONE))),
                List.of(ingredient(Item.byBlock(NETHERRACK))),
                List.of(fluidIngredient("redstone", 72)),
                List.of(), 0.5D);
        itemToItem(ingredient(Tags.Items.SAND, 1), NcIngredient.stack(stack(GLOWSTONE, 1)), 3D);
        itemToItem(dustIngredient(Materials.tbp), dustIngredient(Materials.protactinium_233), 2.5D);
        itemToItem(dustIngredient(Materials.bismuth), dustIngredient(Materials.polonium), 2D);
        fluidsAndFluids(List.of(fluidIngredient("lithium", 500)), List.of(fluidIngredient("irradiated_lithium", 500)), 1.5D);
        fluidsAndFluids(List.of(fluidIngredient("boron", 500)), List.of(fluidIngredient("irradiated_boron", 500)), 2.5D);
        fluidsAndFluids(List.of(fluidIngredient(Materials.uranium238, 100)), List.of(fluidIngredient(Materials.uranium235, 100)), 5.5D);
    }
}
