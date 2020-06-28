package com.focamacho.silkchest.events;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SpawnerPlaceEvents {

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        try {
            if (event.getItemStack().getItem().getRegistryName().equals(new ResourceLocation("minecraft:spawner"))) {
                if (event.getItemStack().hasTag() && event.getItemStack().getTag().contains("silkchest")) {
                    event.setCanceled(true);

                    if (event.getWorld().isRemote) return;

                    World world = event.getWorld();
                    BlockPos pos = event.getPos().offset(event.getFace());
                    Block block = world.getBlockState(pos).getBlock();
                    if (world.isAirBlock(pos) && !world.getServer().isBlockProtected(world, pos, event.getPlayer())) {
                        if(!(event.getEntity() instanceof PlayerEntity) || !((PlayerEntity)event.getEntity()).abilities.isCreativeMode) {
                            event.getItemStack().setCount(event.getItemStack().getCount() - 1);
                        }
                        world.setBlockState(pos, Blocks.SPAWNER.getDefaultState());

                        ((MobSpawnerTileEntity) world.getTileEntity(pos)).getSpawnerBaseLogic().setEntityType(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(event.getItemStack().getTag().getString("silkchest"))));
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
