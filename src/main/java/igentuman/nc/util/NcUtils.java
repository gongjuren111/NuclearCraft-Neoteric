package igentuman.nc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import igentuman.nc.NuclearCraft;
import igentuman.nc.util.math.FloatingLong;
import it.unimi.dsi.fastutil.longs.Long2DoubleArrayMap;
import it.unimi.dsi.fastutil.longs.Long2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class NcUtils {

    public static final float ONE_OVER_ROOT_TWO = (float) (1 / Math.sqrt(2));

    private static final List<UUID> warnedFails = new ArrayList<>();

    //TODO: Evaluate adding an extra optional param to shrink and grow stack that allows for logging if it is mismatched. Defaults to false
    // Deciding on how to implement it into the API will need more thought as we want to keep overriding implementations as simple as
    // possible, and also ideally would use our normal logger instead of the API logger
    public static void logMismatchedStackSize(long actual, long expected) {
        if (expected != actual) {
            NuclearCraft.LOGGER.error("Stack size changed by a different amount ({}) than requested ({}).", actual, expected, new Exception());
        }
    }

    public static void logExpectedZero(FloatingLong actual) {
        if (!actual.isZero()) {
            NuclearCraft.LOGGER.error("Energy value changed by a different amount ({}) than requested (zero).", actual, new Exception());
        }
    }


    public static ItemStack getItemInHand(LivingEntity entity, HumanoidArm side) {
        if (entity instanceof Player player) {
            return getItemInHand(player, side);
        } else if (side == HumanoidArm.RIGHT) {
            return entity.getMainHandItem();
        }
        return entity.getOffhandItem();
    }

    public static ItemStack getItemInHand(Player player, HumanoidArm side) {
        if (player.getMainArm() == side) {
            return player.getMainHandItem();
        }
        return player.getOffhandItem();
    }

    public static int redstoneLevelFromContents(long amount, long capacity) {
        double fractionFull = capacity == 0 ? 0 : amount / (double) capacity;
        return Mth.floor((float) (fractionFull * 14.0F)) + (fractionFull > 0 ? 1 : 0);
    }

    /**
     * Calculates the redstone level based on the percentage of amount stored.
     *
     * @param amount   Amount currently stored
     * @param capacity Total amount that can be stored.
     *
     * @return A redstone level based on the percentage of the amount stored.
     */
    public static int redstoneLevelFromContents(FloatingLong amount, FloatingLong capacity) {
        if (capacity.isZero() || amount.isZero()) {
            return 0;
        }
        return 1 + amount.divide(capacity).multiply(14).intValue();
    }


    /**
     * Checks whether the player is in creative or spectator mode.
     *
     * @param player the player to check.
     *
     * @return true if the player is neither in creative mode, nor in spectator mode.
     */
    public static boolean isPlayingMode(Player player) {
        return !player.isCreative() && !player.isSpectator();
    }

    /**
     * Helper to read the parameter names from the format saved by our annotation processor param name mapper.
     */
    public static List<String> getParameterNames(@Nullable JsonObject classMethods, String method, String signature) {
        if (classMethods != null) {
            JsonObject signatures = classMethods.getAsJsonObject(method);
            if (signatures != null) {
                JsonElement params = signatures.get(signature);
                if (params != null) {
                    if (params.isJsonArray()) {
                        JsonArray paramArray = params.getAsJsonArray();
                        List<String> paramNames = new ArrayList<>(paramArray.size());
                        for (JsonElement param : paramArray) {
                            paramNames.add(param.getAsString());
                        }
                        return Collections.unmodifiableList(paramNames);
                    }
                    return Collections.singletonList(params.getAsString());
                }
            }
        }
        return Collections.emptyList();
    }


}