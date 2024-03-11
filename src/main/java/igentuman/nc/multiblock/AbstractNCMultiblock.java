package igentuman.nc.multiblock;

import igentuman.nc.util.NCBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractNCMultiblock implements INCMultiblock {

    protected Class parentBe;
    public boolean hasToRefresh = true;
    protected int refreshCooldown = 50;
    protected int height;
    protected int width;
    protected int depth;
    protected INCMultiblockController controller;
    public ValidationResult validationResult;
    public HashMap<BlockPos, BlockInfo> cachedBlocks = new HashMap<>();

    public int topCasing = 0;
    public int bottomCasing = 0;
    public int leftCasing = 0;
    public int rightCasing = 0;

    protected boolean outerValid = false;
    public boolean refreshOuterCacheFlag = true;
    public boolean refreshInnerCacheFlag = true;
    public boolean isFormed = false;
    protected boolean innerValid = false;
    protected final List<Block> validOuterBlocks;
    protected final List<Block> validInnerBlocks;
    protected List<BlockPos> controllers = new ArrayList<>();

    protected AbstractNCMultiblock(List<Block> validOuterBlocks, List<Block> validInnerBlocks) {
        this.validOuterBlocks = validOuterBlocks;
        this.validInnerBlocks = validInnerBlocks;
    }

    public BlockInfo getBlockInfo(BlockPos pos) {
        if(cachedBlocks.containsKey(pos)) {
            return cachedBlocks.get(pos);
        }
        TileEntity be = getLevel().getBlockEntity(pos);
        BlockState state = getLevel().getBlockState(pos);
        BlockInfo info = new BlockInfo(state, be);
        cachedBlocks.put(pos, info);
        return info;
    }

    public List<Block> validCornerBlocks() {
        return validOuterBlocks;
    }

    public int height() {
        return height;
    }
    public int width() {
        return width;
    }
    public int depth() {
        return depth;
    }
    public int maxHeight() {
        return 24;
    }
    public int minHeight() {
        return 3;
    }
    public int maxWidth() {
        return 24;
    }
    public int minWidth() {
        return 3;
    }
    public int maxDepth() {
        return 24;
    }
    public int minDepth() {
        return 3;
    }
    public boolean isFormed() {
        return isFormed;
    }
    protected List<BlockPos> allBlocks = new ArrayList<>();

    @Override
    public List<Block> validOuterBlocks() { return validOuterBlocks;  }

    @Override
    public List<Block> validInnerBlocks() { return validInnerBlocks; }

    protected World getLevel() {
        return  controller().controllerBE().getLevel();
    }
    protected BlockPos controllerPos;
    protected BlockPos controllerPos() {
        if(controllerPos == null) {
            controllerPos = controller().controllerBE().getBlockPos();
        }
        return  NCBlockPos.of(controllerPos);
    }

    public BlockPos getBottomLeftBlock() {
        return getLeftPos(leftCasing).below(bottomCasing).relative(getFacing(), -depth+1);
    }

    public BlockPos getCenterBlock() {
        BlockPos bottomLeft = getBottomLeftBlock();
        BlockPos topRight = getTopRightBlock();
        return new BlockPos(
                (bottomLeft.getX() + topRight.getX()) / 2,
                (bottomLeft.getY() + topRight.getY()) / 2,
                (bottomLeft.getZ() + topRight.getZ()) / 2
        );
    }

    public BlockPos getTopRightBlock() {
        return getRightPos(rightCasing).above(topCasing);
    }

    public boolean isValidForOuter(BlockPos pos)
    {
        if(getLevel() == null) return false;
        try {
            return  validOuterBlocks().contains(getLevel().getBlockState(pos).getBlock());
        } catch (NullPointerException ignored) { }
        return false;
    }
    public boolean isValidCorner(BlockPos pos)
    {
        if(getLevel() == null) return false;
        try {
            return  validCornerBlocks().contains(getLevel().getBlockState(pos).getBlock());
        } catch (NullPointerException ignored) { }
        return false;
    }

    public boolean isValidForInner(BlockPos pos)
    {
        if(getLevel() == null) return false;
        try {
            BlockState bs = getLevel().getBlockState(pos);
            if(bs.isAir()) return true;
            return  validInnerBlocks().contains(bs.getBlock());
        } catch (NullPointerException ignored) { }
        return false;
    }

    public int resolveHeight()
    {
        for (int i = 1; i < maxHeight(); i++) {
            if (!isValidForOuter(controllerPos().above(i))) {
                topCasing = i - 1;
                height = i;
                break;
            }
        }
        for (int i = 1; i < maxHeight(); i++) {
            if (!isValidForOuter(controllerPos().below(i))) {
                bottomCasing = i - 1;
                height += i - 1;
                break;
            }
        }

        return height;
    }

    public int resolveWidth()
    {
        for(int i = 1; i<maxWidth(); i++) {
            if(!isValidForOuter(getLeftPos(i))) {
                leftCasing = i-1;
                width = i;
                break;
            }
        }
        for(int i = 1; i<maxWidth(); i++) {
            if(!isValidForOuter(getRightPos(i))) {
                rightCasing = i-1;
                width += i-1;
                break;
            }
        }
        return width;
    }

    public int resolveDepth()
    {
        for(int i = 1; i<maxDepth(); i++) {
            if(!isValidForOuter(getForwardPos(i).above(topCasing))) {
                depth = i;
                break;
            }
        }
        return depth;
    }

    public void resolveDimensions()
    {
        resolveHeight();
        resolveDepth();
        resolveWidth();
    }

    @Override
    public void validateOuter() {
        resolveDimensions();
        if(width < minWidth() || height < minHeight() || depth < minDepth())
        {
            validationResult = ValidationResult.TOO_SMALL;
            return;
        }
        if(width > maxWidth() || height > maxHeight() || depth > maxDepth())
        {
            validationResult = ValidationResult.TOO_BIG;
            return;
        }
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    if(y == 0 || x == 0 || z == 0 || y == height-1 || x == width-1 || z == depth-1) {
                        if (!isValidForOuter(getSidePos(x - leftCasing).above(y - bottomCasing).relative(getFacing(), -z))) {
                            validationResult = ValidationResult.WRONG_OUTER;
                            controller().addErroredBlock(getSidePos(x - leftCasing).above(y - bottomCasing).relative(getFacing(), -z));
                            return;
                        }
                        processOuterBlock(getSidePos(x - leftCasing).above(y - bottomCasing).relative(getFacing(), -z));
                        //validate corner blocks
                        if(((y == 0 || y == height-1) && (z == 0 || z == depth - 1))
                        || ((y == 0 || y == height-1) && (x == 0 || x == width - 1))
                        || ((z == 0 || z == depth-1) && (x == 0 || x == width - 1))
                        ) {
                            if(!isValidCorner(getSidePos(x - leftCasing).above(y - bottomCasing).relative(getFacing(), -z))) {
                                validationResult = ValidationResult.WRONG_CORNER;
                                controller().addErroredBlock(getSidePos(x - leftCasing).above(y - bottomCasing).relative(getFacing(), -z));
                                return;
                            }
                        }
                    }
                }
            }
        }
        if(controllers.size() > 1) {
            validationResult = ValidationResult.TOO_MANY_CONTROLLERS;
            return;
        }
        validationResult = ValidationResult.VALID;
    }

    protected void processOuterBlock(BlockPos pos) {
        attachMultiblock(pos);
        allBlocks.add(new NCBlockPos(pos));
        if(getLevel().getBlockState(pos).getBlock().asItem().toString().contains("controller")) {
            controllers.add(pos);
        }
    }

    public void validateInner() {
        invalidateStats();
        if(!outerValid) return;
        for(int y = 1; y < resolveHeight()-1; y++) {
            for(int x = 1; x < resolveWidth()-1; x++) {
                for (int z = 1; z < resolveDepth()-1; z++) {
                    NCBlockPos toCheck = new NCBlockPos(getSidePos(x - leftCasing).above(y - bottomCasing).relative(getFacing(), -z));
                    if (!isValidForInner(toCheck)) {
                        validationResult = ValidationResult.WRONG_INNER;
                        controller().addErroredBlock(toCheck);
                        return;
                    }
                    processInnerBlock(toCheck.copy());
                }
            }
        }

        validationResult =  ValidationResult.VALID;
    }

    protected boolean processInnerBlock(BlockPos toCheck) {
        allBlocks.add(toCheck);
        return true;
    }

    protected abstract void invalidateStats();

    protected void attachMultiblock(BlockPos pos) {
        attachMultiblock(getLevel().getBlockEntity(pos));
    }

    protected void attachMultiblock(TileEntity be) {
        if(be instanceof IMultiblockAttachable) {
            ((IMultiblockAttachable)be).setMultiblock(this);
        }
    }

    public boolean isLoaded(BlockPos pos)
    {
        return getLevel().isLoaded(pos);
    }

    public void onControllerRemoved() {
        for(BlockPos b: allBlocks) {
            if(!isLoaded(b)) continue;
            TileEntity be = getLevel().getBlockEntity(b);
            if(be instanceof IMultiblockAttachable) {
                ((IMultiblockAttachable) be).setMultiblock(null);
            }
        }
    }

    public BlockPos getForwardPos(int i) {
        return controllerPos().relative(getFacing(), -i);
    }

    public BlockPos getLeftPos(int i)
    {
        return getSidePos(-i);
    }

    public BlockPos getRightPos(int i)
    {
        return getSidePos(i);
    }

    public BlockPos getSidePos(int i) {
        switch (getFacing().ordinal()) {
            case 3:
                return controllerPos().east(i);
            case 5:
                return controllerPos().north(i);
            case 2:
                return controllerPos().west(i);
            case 4:
                return controllerPos().south(i);
        };
        return null;
    }

    protected abstract Direction getFacing();

    @Override
    public void validate() {
        validationResult = ValidationResult.VALID;
        refreshOuterCacheFlag = true;
        refreshInnerCacheFlag = true;
        allBlocks.clear();
        controllers.clear();

        isOuterValid();
        validateInner();
        innerValid = validationResult.isValid;
        //todo more checks before?
        isFormed = outerValid && innerValid;
    }

    public boolean isInnerValid() {
        if(refreshOuterCacheFlag) return false;
        if(refreshInnerCacheFlag) {
            validateInner();
            refreshInnerCacheFlag = !validationResult.isValid;
            innerValid = validationResult.isValid;
        }
        return innerValid;
    }

    public boolean isOuterValid() {
        if(refreshOuterCacheFlag) {
            validateOuter();
            refreshOuterCacheFlag = !validationResult.isValid;
            outerValid = validationResult.isValid;
        }
        return outerValid;
    }

    public INCMultiblockController controller() {
        return controller;
    }

    public void onNeighborChange(BlockState state, BlockPos pos, BlockPos neighbor) {
        //we only update if something changes within the multiblock
        if(shouldRefreshCache(state, pos, neighbor)) {
            hasToRefresh = true;
        }
    }

    private boolean shouldRefreshCache(BlockState state, BlockPos pos, BlockPos neighbor) {
        boolean isInTheList = allBlocks.contains(neighbor);
        TileEntity neighborBe = getLevel().getBlockEntity(neighbor);
        if(!isInTheList) return false; //ignore all blocks outside
        if(neighborBe instanceof IMultiblockAttachable) {
            return ((IMultiblockAttachable)neighborBe).canInvalidateCache();
        }
        return true;
    }

    public void tick() {
        //not letting to spam structure re validation
        if(hasToRefresh) {
            refreshCooldown--;
            if(refreshCooldown <= 0) {
                refreshCooldown = 10;
                refreshOuterCacheFlag = true;
                refreshInnerCacheFlag = true;
                isFormed = false;
                hasToRefresh = false;
            }
        }
    }

    public void onBlockDestroyed(BlockState state, World level, BlockPos pos, Explosion explosion) {
        controller.clearStats();
    }
}
