package igentuman.nc.multiblock.fission;

import igentuman.nc.block.entity.fission.*;
import igentuman.nc.multiblock.AbstractNCMultiblock;
import igentuman.nc.util.NCBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

import java.util.*;

import static igentuman.nc.handler.config.FissionConfig.FISSION_CONFIG;
import static igentuman.nc.util.TagUtil.getBlocksByTagKey;

public class FissionReactorMultiblock extends AbstractNCMultiblock {

    private int irradiationConnections = 0;

    @Override
    public int maxHeight() {
        return FISSION_CONFIG.MAX_SIZE.get();
    }

    @Override
    public int maxWidth() {
        return FISSION_CONFIG.MAX_SIZE.get();
    }

    @Override
    public int maxDepth() {
        return FISSION_CONFIG.MAX_SIZE.get();
    }

    @Override
    public int minHeight() {
        return FISSION_CONFIG.MIN_SIZE.get();
    }

    @Override
    public int minWidth() {return FISSION_CONFIG.MIN_SIZE.get(); }

    @Override
    public int minDepth() { return FISSION_CONFIG.MIN_SIZE.get(); }


    public HashMap<BlockPos, FissionHeatSinkBE> activeHeatSinks = new HashMap<>();
    private List<BlockPos> moderators = new ArrayList<>();
    private List<BlockPos> irradiators = new ArrayList<>();
    public List<BlockPos> heatSinks = new ArrayList<>();
    public List<BlockPos> fuelCells = new ArrayList<>();
    private double heatSinkCooling = 0;

    public FissionReactorMultiblock(FissionControllerBE<?> fissionControllerBE) {
        super(
                getBlocksByTagKey(FissionBlocks.CASING_BLOCKS.getName()),
                getBlocksByTagKey(FissionBlocks.INNER_REACTOR_BLOCKS.getName())
        );
        controller = new FissionReactorController(fissionControllerBE);
    }

    public Map<BlockPos, FissionHeatSinkBE> activeHeatSinks() {
        if(activeHeatSinks.isEmpty()) {
            for(BlockPos hpos: heatSinks) {
                TileEntity be = getLevel().getBlockEntity(hpos);
                if(be instanceof FissionHeatSinkBE) {
                    FissionHeatSinkBE hs = (FissionHeatSinkBE) be;
                    if(hs.isValid(true)) {
                        activeHeatSinks.put(hpos, hs);
                    }
                }
            }
        }
        ((FissionControllerBE<?>)controller().controllerBE()).heatSinksCount = activeHeatSinks.size();
        return activeHeatSinks;
    }

    public static boolean isModerator(BlockPos pos, World world) {
        return  world.getBlockEntity(pos) instanceof FissionModeratorBE;
    }

    public static boolean isIrradiator(BlockPos pos, World world) {
        return  world.getBlockEntity(pos) instanceof FissionIrradiationChamberBE;
    }

    protected boolean isHeatSink(BlockPos pos) {
        return getLevel().getBlockEntity(pos) instanceof FissionHeatSinkBE;
    }

    protected boolean isFuelCell(BlockPos pos) {
        return getLevel().getBlockEntity(pos) instanceof FissionFuelCellBE;
    }

    private boolean isAttachedToFuelCell(BlockPos toCheck) {
        for(Direction d : Direction.values()) {
            if(toCheck instanceof NCBlockPos) {
                ((NCBlockPos) toCheck).revert();
            }
            if(isFuelCell(toCheck.relative(d))) {
                return true;
            }
        }
        return false;
    }

    public void validateInner()
    {
        super.validateInner();
        heatSinkCooling = getHeatSinkCooling(true);
        FissionControllerBE<?> controller = (FissionControllerBE<?>) controller().controllerBE();
        controller.fuelCellsCount = fuelCells.size();
        controller.updateEnergyStorage();
        controller.moderatorsCount = moderators.size();
        controller.irradiationConnections = irradiationConnections;
    }

    @Override
    protected boolean processInnerBlock(BlockPos toCheck) {
        if(isFuelCell(toCheck)) {
            TileEntity be = getLevel().getBlockEntity(toCheck);
            if(be instanceof FissionFuelCellBE) {
                fuelCells.add(toCheck);
                int moderatorAttachments = ((FissionFuelCellBE) be).getAttachedModeratorsCount(true);
                ((FissionControllerBE<?>)controller().controllerBE()).fuelCellMultiplier += countAdjuscentFuelCells((NCBlockPos) toCheck, 3);
                ((FissionControllerBE<?>)controller().controllerBE()).moderatorCellMultiplier += (countAdjuscentFuelCells((NCBlockPos) toCheck, 1)+1)*moderatorAttachments;
                ((FissionControllerBE<?>)controller().controllerBE()).moderatorAttacmentsCount += moderatorAttachments;
            }
        }
        if(isModerator(toCheck, getLevel())) {
            if(isAttachedToFuelCell(toCheck)) {
                moderators.add(toCheck);
            }
        }
        if(isHeatSink(toCheck)) {
            heatSinks.add(toCheck);
        }
        if(isIrradiator(toCheck, getLevel())) {
            irradiators.add(toCheck);
            countIrradiationConnections(toCheck);
        }
        return true;
    }

    private void countIrradiationConnections(BlockPos toCheck) {
        FissionIrradiationChamberBE be = (FissionIrradiationChamberBE) getLevel().getBlockEntity(toCheck);
        assert be != null;
        be.countIrradiationConnections();
        irradiationConnections += be.irradiationConnections;
    }

    private int countAdjuscentFuelCells(NCBlockPos toCheck, int step) {
        int count = 0;
        for (Direction d : Direction.values()) {
            if (isFuelCell(toCheck.revert().relative(d))) {
                count+=step;
                continue;
            }
            if(isModerator(toCheck.revert().relative(d), getLevel())) {
                if(isFuelCell(toCheck.revert().relative(d, 2))) {
                    count += step;
                }
            }
        }
        return count;
    }

    public void invalidateStats()
    {
        controller().clearStats();
        moderators.clear();
        irradiators.clear();
        fuelCells.clear();
        heatSinks.clear();
        activeHeatSinks.clear();
        irradiationConnections = 0;
    }

    protected Direction getFacing() {
        return ((FissionControllerBE<?>)controller().controllerBE()).getFacing();
    }

    public double getHeatSinkCooling(boolean forceCheck) {
        if(refreshInnerCacheFlag || forceCheck) {
            heatSinkCooling = 0;
            for (FissionHeatSinkBE hs : activeHeatSinks().values()) {
                heatSinkCooling += hs.getHeat();
            }
        }
        return heatSinkCooling;
    }

}
