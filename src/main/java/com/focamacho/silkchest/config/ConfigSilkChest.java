package com.focamacho.silkchest.config;

import com.focamacho.silkchest.SilkChest;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ConfigSilkChest {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    //Config Values
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> silkList;

    public static class General {
        public General(final ForgeConfigSpec.Builder builder) {
            builder.push("Silk Chest");

            silkList = builder.comment("Add here any block you wanna keep the items inside it when using silk touch").defineList("silkList", Lists.newArrayList("minecraft:chest", "minecraft:trapped_chest", "minecraft:spawner"),  string -> {
                if(string instanceof String) {

                    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation((String)string));

                    if(block == null) return false;

                    SilkChest.silkBlocks.add(block);
                    return true;
                }

                return false;
            });

            builder.pop();
        }
    }

}