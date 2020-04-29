package com.focamacho.silkchest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = SilkChest.MODID, name = SilkChest.NAME, version = SilkChest.VERSION)
public class SilkChest
{
    public static final String MODID = "silkchest";
    public static final String NAME = "Silk Chest";
    public static final String VERSION = "1.2";
    
    public static List<IBlockState> SilkBlocks = new ArrayList<IBlockState>();

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        ModConfig.init(new File(event.getModConfigurationDirectory(), "silkChest.cfg"));
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventsHandler());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	//Get Blocks from Config
    	for(String string : ModConfig.silkList) {
    		String[] split = string.split(":");
    		try {
	    		if(split.length > 2) {
	    			if(Block.getBlockFromName(split[0] + ":" + split[1]).getStateFromMeta(Integer.parseInt(split[3])) != null) {
	    				SilkBlocks.add(Block.getBlockFromName(split[0] + ":" + split[1]).getStateFromMeta(Integer.parseInt(split[3])));
	    			}
	    		} else {
	    			if(Block.getBlockFromName(split[0] + ":" + split[1]).getDefaultState() != null) {
	    				SilkBlocks.add(Block.getBlockFromName(split[0] + ":" + split[1]).getDefaultState());
	    			}
	    		}
    		} catch(Exception e) {
    			logger.error(e);
    		}
    	}
    }
}
