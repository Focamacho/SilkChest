package com.focamacho.silkchest;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ModConfig {
	
	public static Configuration config;
	public static ModConfig instance;
	
	public static String[] silkList;
	
	@SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.getModID().equals(SilkChest.MODID)) {
            ModConfig.syncConfig();
        }
    }
	
	public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
	}
	
	public static void syncConfig() {
	
		String category;
		
		category = "Principal";
		config.addCustomCategoryComment(category, "Add here any block you wanna keep the items inside it when using silk touch");
		silkList = config.get(category, "silkList", new String[] {"minecraft:chest", "minecraft:trapped_chest", "ironchest:iron_chest", "minecraft:mob_spawner"}).getStringList();
		
		if(config.hasChanged()){
			config.save();
		}
	}
} 