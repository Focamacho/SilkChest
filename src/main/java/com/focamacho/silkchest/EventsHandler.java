package com.focamacho.silkchest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

public class EventsHandler {

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		for(IBlockState silk : SilkChest.SilkBlocks) {
			if(event.getState().getBlock() == silk.getBlock()) {
				if(Block.getStateId(event.getState()) == Block.getStateId(event.getState())) {
					tileHandling(event);
					break;
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void itemToolTip(ItemTooltipEvent event) {
		if(event.getItemStack().hasTagCompound() && event.getItemStack().getTagCompound().hasKey("BlockEntityTag")) {
			NonNullList<ItemStack> itemsList = NonNullList.withSize(200, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(event.getItemStack().getTagCompound().getCompoundTag("BlockEntityTag"), itemsList);
			int i = 0;
			int j = 0;
            for(ItemStack item : itemsList) {
				if(!item.isEmpty()) {
					if(i < 5) {
						i++;
						event.getToolTip().add(item.getDisplayName() + " x" + item.getCount());
					} else {
						j++;
					}
				}
			}
			if(i >= 5) event.getToolTip().add(TextFormatting.ITALIC + new TextComponentTranslation("container.shulkerBox.more").getFormattedText().replace("%s",  Integer.toString(j - i)));
		}
	}
	
	public static void tileHandling(BlockEvent.BreakEvent event) {
		ItemStack item = event.getPlayer().getHeldItemMainhand();
		if(item != null && item.isItemEnchanted()) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
			if(enchantments.get(Enchantments.SILK_TOUCH) != null) {
				World world = event.getWorld();
				BlockPos pos = event.getPos();
				
				if(world.getTileEntity(pos) == null) return;
				
				//Cancel the Break event
				event.setCanceled(true);
				
				RayTraceResult raytrace = new RayTraceResult(event.getPlayer());
				
				//Get the Block Item
				ItemStack itemBlock = event.getState().getBlock().getPickBlock(event.getState(), raytrace, world, pos, event.getPlayer());
				
				//Save data to NBT
				NBTTagCompound nbtItems = new NBTTagCompound();
				world.getTileEntity(pos).writeToNBT(nbtItems);
				itemBlock.setTagInfo("BlockEntityTag", nbtItems);
				
				//Remove the Tile-Entity to prevent items from being dropped
				world.removeTileEntity(pos);
				
				//Set the Block to Air after removing his tile-entity
				world.setBlockToAir(pos);
				
				//Drop the new Item with the NBTTag
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemBlock));
			}
		}
	}
	
}
