package igentuman.nc.block.fission;

import igentuman.nc.block.entity.fission.FissionBE;
import igentuman.nc.multiblock.fission.FissionReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.antlr.v4.runtime.misc.NotNull;;
import javax.annotation.Nullable;

public class FissionBlock extends Block implements EntityBlock {

    public FissionBlock(Properties pProperties) {
        super(pProperties.sound(SoundType.METAL));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    @Deprecated
    public boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentBlockState, @NotNull Direction side) {
        return adjacentBlockState.getBlock().equals(this) && asItem().toString().matches(".*glass|.*cell.*|.*slope.*");
    }

    private String blockEntityCode()
    {
        String code = Registry.BLOCK.getKey(this).getPath();
        if(code.matches(".*reactor_glass|.*reactor_casing.*")) {
            return "fission_casing";
        }
        if(code.matches("graphite.*|beryllium.*")) {
            return "fission_moderator";
        }
        if(code.contains("fuel_cell")) {
            return "fission_reactor_fuel_cell";
        }
        return code;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return FissionReactor.FISSION_BE.get(blockEntityCode()).get().create(pPos, pState);
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return (lvl, pos, blockState, t) -> {
                if (t instanceof FissionBE tile) {
                    tile.tickClient();
                }
            };
        }
        return (lvl, pos, blockState, t)-> {
            if (t instanceof FissionBE tile) {
                tile.tickServer();
            }
        };
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor){
        ((FissionBE)level.getBlockEntity(pos)).onNeighborChange(state,  pos, neighbor);
    }
}
