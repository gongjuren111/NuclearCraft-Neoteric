package igentuman.nc.block.entity.processor;

import igentuman.nc.content.processors.Processors;
import igentuman.nc.handler.config.CommonConfig;
import igentuman.nc.recipes.ingredient.FluidStackIngredient;
import igentuman.nc.recipes.ingredient.ItemStackIngredient;
import igentuman.nc.recipes.type.NcRecipe;
import igentuman.nc.util.CustomEnergyStorage;
import igentuman.nc.util.annotation.NBTField;
import igentuman.nc.util.annotation.NothingNullByDefault;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.block.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static igentuman.nc.handler.config.CommonConfig.ENERGY_GENERATION;
import static igentuman.nc.setup.registration.NCProcessors.PROCESSORS_BE;
import static net.minecraftforge.energy.CapabilityEnergy.ENERGY;

public class SteamTurbineBE extends NCProcessorBE<SteamTurbineBE.Recipe> {
    @NBTField
    protected double efficiency = 0.0001;
    public SteamTurbineBE(BlockPos pPos, BlockState pBlockState) {
        super(pPos, pBlockState, Processors.STEAM_TURBINE);
    }

    public SteamTurbineBE() {
        super(PROCESSORS_BE.get(Processors.STEAM_TURBINE).get());
    }

    @Override
    public String getName() {
        return Processors.STEAM_TURBINE;
    }

    @Override
    public void tickServer()
    {
        sendOutPower();
        efficiency -= 0.0001;
        efficiency = Math.max(0.0001, Math.min(10, efficiency));
        if(energyStorage.getEnergyStored()>=energyStorage.getMaxEnergyStored()) {
            return;
        }
        super.tickServer();
    }

    @Override
    protected void processRecipe() {
        if(!hasRecipe()) {
            updateRecipe();
        }
        if(!hasRecipe()) return;

        recipeInfo.process(speedMultiplier());
        efficiency += 0.0004;
        energyStorage.addEnergy((int) (getEnergyTransferPerTick()*recipe.getEnergy()));
    }

    @Override
    public double speedMultiplier()
    {
        return super.speedMultiplier()/efficiency;
    }

    @Override
    protected CustomEnergyStorage createEnergy() {
        //todo read config
        return new CustomEnergyStorage(getEnergyMaxStorage(), 0, getEnergyMaxStorage()) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    public int energyToSend()
    {
        return Math.min(energyStorage.getEnergyStored(), getEnergyTransferPerTick());
    }

    protected void sendOutPower() {
        if(energyStorage.getEnergyStored() == 0) return;
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        if (capacity.get() > 0) {
            for (Direction direction : Direction.values()) {
                TileEntity be = level.getBlockEntity(worldPosition.relative(direction));
                if (be != null) {
                    boolean doContinue = be.getCapability(ENERGY, direction.getOpposite()).map(handler -> {
                                if (handler.canReceive()) {
                                    int received = handler.receiveEnergy(Math.min(capacity.get(), energyToSend()), false);
                                    capacity.addAndGet(-received);
                                    energyStorage.consumeEnergy(received);
                                    setChanged();
                                    return capacity.get() > 0;
                                } else {
                                    return true;
                                }
                            }
                    ).orElse(true);
                    if (!doContinue) {
                        return;
                    }
                }
            }
        }
    }

    protected int getEnergyMaxStorage() {
        return getEnergyTransferPerTick()*10;
    }

    protected int getEnergyTransferPerTick() {
        return ENERGY_GENERATION.STEAM_TURBINE.get();
    }

    @NothingNullByDefault
    public static class Recipe extends NcRecipe {
        public Recipe(ResourceLocation id,
                      ItemStackIngredient[] input, ItemStack[] output,
                      FluidStackIngredient[] inputFluids, FluidStack[] outputFluids,
                      double timeModifier, double powerModifier, double heatModifier, double rarity) {
            super(id, input, output, inputFluids, outputFluids, timeModifier, powerModifier, heatModifier, 1);
        }

        @Override
        public String getCodeId() {
            return Processors.STEAM_TURBINE;
        }
    }
}
