package com.focamacho.silkchest;

import com.focamacho.silkchest.config.ConfigSilkChest;
import com.focamacho.silkchest.events.BlockBreakEvent;
import com.focamacho.silkchest.events.SpawnerPlaceEvents;
import com.focamacho.silkchest.events.TooltipEvent;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod("silkchest")
public class SilkChest {

    private static final Logger logger = LogManager.getLogger();

    public static List<Block> silkBlocks = new ArrayList<Block>();

    public SilkChest() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigSilkChest.spec);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(new BlockBreakEvent());
        MinecraftForge.EVENT_BUS.register(new SpawnerPlaceEvents());
    }

    private void setup(final FMLCommonSetupEvent event) {
        //bug fix
        if(silkBlocks.isEmpty()) {
            ConfigSilkChest.silkList.get().forEach(string -> {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(string));
                if(block != null) SilkChest.silkBlocks.add(block);
            });
        }
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new TooltipEvent());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }

}
