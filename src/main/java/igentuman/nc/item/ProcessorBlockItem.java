package igentuman.nc.item;

import igentuman.nc.setup.registration.CreativeTabs;
import igentuman.nc.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.List;

public class ProcessorBlockItem extends BlockItem
{
	public ProcessorBlockItem(Block pBlock, Properties props)
	{
		this(pBlock, props, CreativeTabs.NC_BLOCKS);
	}

	public ProcessorBlockItem(Block pBlock, Properties props, CreativeModeTab group)
	{
		super(pBlock, new Properties().tab(group));
	}

	@Override
	public boolean isRepairable(@Nonnull ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book)
	{
		return false;
	}

	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity)
	{
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level world, List<Component> list, TooltipFlag flag)
	{
		if(stack.hasTag() && stack.getTag().contains("energy")) {
			list.add(new TranslatableComponent("tooltip.nc.content_saved").withStyle(ChatFormatting.GRAY));
		}
		if(asItem().toString().contains("empty") || this.asItem().equals(Items.AIR)) return;
		list.add(TextUtils.applyFormat(new TranslatableComponent("processor.description."+toString()), ChatFormatting.AQUA));
	}
}
