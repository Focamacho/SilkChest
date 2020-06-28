package com.focamacho.silkchest.events;

import com.focamacho.silkchest.SilkChest;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockBreakEvent {

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if(event.getState() != null && SilkChest.silkBlocks.contains(event.getState().getBlock())) {
            if(event.getPlayer() != null) {
                ItemStack item = event.getPlayer().getHeldItemMainhand();
                if(EnchantmentHelper.getEnchantments(item).get(Enchantments.SILK_TOUCH) != null) {
                    IWorld world = event.getWorld();
                    BlockPos pos = event.getPos();

                    if(world.getTileEntity(pos) == null) return;

                    //Cancel the Break event
                    event.setCanceled(true);

                    //Get the Block Item
                    ItemStack itemBlock = new ItemStack(ForgeRegistries.ITEMS.getValue(event.getState().getBlock().getRegistryName()));

                    //Save data to NBT
                    CompoundNBT nbt = new CompoundNBT();
                    world.getTileEntity(pos).write(nbt);

                    if(itemBlock.getItem().getRegistryName().equals(new ResourceLocation("minecraft:spawner"))) {
                        CompoundNBT spawnerNbt = new CompoundNBT();
                        spawnerNbt.putString("silkchest", nbt.getCompound("SpawnData").getString("id"));
                        itemBlock.setTag(spawnerNbt);
                    } else {
                        itemBlock.setTagInfo("BlockEntityTag", nbt);
                    }

                    //Remove the Tile-Entity to prevent items from being dropped
                    world.getTileEntity(pos).remove();

                    //Set the Block to Air after removing his tile-entity
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 1);

                    //Drop the new Item with the NBTTag
                    world.addEntity(new ItemEntity(world.getWorld(), pos.getX(), pos.getY(), pos.getZ(), itemBlock));
                }
            }
        }
    }
}
