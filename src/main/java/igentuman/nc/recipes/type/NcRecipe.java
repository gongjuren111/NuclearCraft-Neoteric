package igentuman.nc.recipes.type;

import igentuman.nc.recipes.AbstractRecipe;
import igentuman.nc.recipes.ingredient.FluidStackIngredient;
import igentuman.nc.recipes.ingredient.ItemStackIngredient;
import igentuman.nc.util.annotation.NothingNullByDefault;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

import static igentuman.nc.compat.GlobalVars.CATALYSTS;
import static igentuman.nc.compat.GlobalVars.RECIPE_CLASSES;

@NothingNullByDefault
public abstract class NcRecipe extends AbstractRecipe {

    public final double rarityModifier;

    public NcRecipe(
            ResourceLocation id,
            ItemStackIngredient[] inputItems,
            ItemStack[] outputItems,
            FluidStackIngredient[] inputFluids,
            FluidStack[] outputFluids,
            double timeModifier,
            double powerModifier,
            double radiationModifier,
            double rarityModifier
    ) {

        super(id);
        this.inputItems = inputItems;
        this.outputItems = outputItems;
        this.inputFluids = inputFluids;
        this.outputFluids = outputFluids;

        this.timeModifier = timeModifier;
        this.powerModifier = powerModifier;
        this.radiationModifier = radiationModifier;
        this.rarityModifier = rarityModifier;
        CATALYSTS.put(codeId, Arrays.asList(getToastSymbol()));
        RECIPE_CLASSES.put(codeId, getClass());
    }


    public NcRecipe(
            ResourceLocation id,
            ItemStackIngredient[] inputItems,
            ItemStack[] outputItems,
            double timeModifier,
            double powerModifier,
            double radiationModifier,
            double rarityModifier
    ) {

        this(id, inputItems, outputItems, new FluidStackIngredient[0], new FluidStack[0], timeModifier, powerModifier, radiationModifier, rarityModifier);
    }

    public NcRecipe(
            ResourceLocation id,
            FluidStackIngredient[] inputFluids,
            FluidStack[] outputFluids,
            double timeModifier,
            double powerModifier,
            double radiationModifier,
            double rarityModifier
    ) {
            this(id, new ItemStackIngredient[0], new ItemStack[0], inputFluids, outputFluids, timeModifier, powerModifier, radiationModifier, rarityModifier);
    }


    @Override
    public void write(PacketBuffer buffer) {

        buffer.writeInt(inputItems.length);
        for (ItemStackIngredient input : inputItems) {
            input.write(buffer);
        }

        buffer.writeInt(outputItems.length);
        for (ItemStack output : outputItems) {
            output = output == null ? ItemStack.EMPTY : output;
            buffer.writeItem(output);
        }

        buffer.writeInt(inputFluids.length);
        for (FluidStackIngredient input : inputFluids) {
            input.write(buffer);
        }

        buffer.writeInt(outputFluids.length);
        for (FluidStack output : outputFluids) {
            output = output == null ? FluidStack.EMPTY : output;
            buffer.writeFluidStack(output);
        }

        buffer.writeDouble(timeModifier);
        buffer.writeDouble(powerModifier);
        buffer.writeDouble(radiationModifier);
    }
}