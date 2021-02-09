package com.lonewolf.ee.reference;

import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;

public class DefaultPreEMCValues
{
	public static ArrayList<CompoundNBT> preEMCValues = new ArrayList<CompoundNBT>()
	{
		{
			// Fluids
			add(generate("fluidStack", "lava", 0.064f));
			//			add(generate("fluidstack", "milk",  0.064f));
			add(generate("fluidStack", "water", 0.001f));
			
			// Ores
			add(generate("oreStack", "cobblestone", 1));
			add(generate("oreStack", "dyes", 16));
			add(generate("oreStack", "dusts/redstone", 32));
			add(generate("oreStack", "dusts/glowstone", 384));
			add(generate("oreStack", "dusts/prismarine", 256));
			add(generate("oreStack", "gems/prismarine", 512));
			add(generate("oreStack", "gems/diamond", 8192));
			add(generate("oreStack", "gems/emerald", 8192));
			add(generate("oreStack", "gems/lapis", 864));
			add(generate("oreStack", "gems/quartz", 256));
			add(generate("oreStack", "ingots/gold", 2048));
			add(generate("oreStack", "ingots/iron", 256));
			add(generate("oreStack", "ingots/brick", 64));
			add(generate("oreStack", "ingots/nether_brick", 1));
			add(generate("oreStack", "ores/coal", 32));
			add(generate("oreStack", "ores/diamond", 8192));
			add(generate("oreStack", "ores/emerald", 8192));
			add(generate("oreStack", "ores/gold", 2048));
			add(generate("oreStack", "ores/iron", 256));
			add(generate("oreStack", "ores/lapis", 864));
			add(generate("oreStack", "ores/quartz", 256));
			add(generate("oreStack", "ores/redstone", 32));
			add(generate("oreStack", "logs", 32));
			add(generate("oreStack", "planks", 8));
			add(generate("oreStack", "music_discs", 2048));
			add(generate("oreStack", "sand", 1));
			add(generate("oreStack", "sandstone", 4));
			add(generate("oreStack", "wooden_slabs", 4));
			add(generate("oreStack", "slimeballs", 24));
			add(generate("oreStack", "wooden_stairs", 12));
			add(generate("oreStack", "rods/wooden", 12));
			add(generate("oreStack", "rods/blaze", 1536));
			add(generate("oreStack", "stone", 1));
			add(generate("oreStack", "stone_bricks", 1));
			add(generate("oreStack", "leaves", 1));
			add(generate("oreStack", "saplings", 1));
			add(generate("oreStack", "small_flowers", 16));
			add(generate("oreStack", "tall_flowers", 32));
			add(generate("oreStack", "mushrooms", 32));
			add(generate("oreStack", "netherrack", 1));
			add(generate("oreStack", "end_stones", 1));
			add(generate("oreStack", "coals", 32));
			add(generate("oreStack", "string", 12));
			add(generate("oreStack", "feathers", 48));
			add(generate("oreStack", "gunpowder", 192));
			add(generate("oreStack", "seeds/wheat", 24));
			add(generate("oreStack", "leather", 64));
			add(generate("oreStack", "eggs", 32));
			add(generate("oreStack", "fishes", 24));
			add(generate("oreStack", "bones", 48));
			add(generate("oreStack", "ender_pearls", 1024));
			add(generate("oreStack", "crops/nether_wart", 24));
			add(generate("oreStack", "crops/carrot", 24));
			add(generate("oreStack", "crops/potato", 24));
			add(generate("oreStack", "nether_stars", 24576));

			// Items
			add(generate("itemStack", "minecraft:grass", 1));
			add(generate("itemStack", "minecraft:tall_grass", 1));
			add(generate("itemStack", "minecraft:dirt", 1));
			add(generate("itemStack", "minecraft:grass_block", 1));
			add(generate("itemStack", "minecraft:glass", 1));
			add(generate("itemStack", "minecraft:cobweb", 12));
			add(generate("itemStack", "minecraft:obsidian", 64));
			add(generate("itemStack", "minecraft:ice", 1));
			add(generate("itemStack", "minecraft:cactus", 8));
			add(generate("itemStack", "minecraft:pumpkin", 144));
			add(generate("itemStack", "minecraft:soul_sand", 49));
			add(generate("itemStack", "minecraft:vine", 8));
			add(generate("itemStack", "minecraft:mycelium", 1));
			add(generate("itemStack", "minecraft:lily_pad", 16));
			add(generate("itemStack", "minecraft:chipped_anvil", 5290.667f));
			add(generate("itemStack", "minecraft:damaged_anvil", 2645.333f));
			add(generate("itemStack", "minecraft:terracotta", 256));
			add(generate("itemStack", "minecraft:apple", 24));
			add(generate("itemStack", "minecraft:flint", 4));
			add(generate("itemStack", "minecraft:porkchop", 24));
			add(generate("itemStack", "minecraft:cooked_porkchop", 24));
			add(generate("itemStack", "minecraft:saddle", 192));
			add(generate("itemStack", "minecraft:snowball", 0.25f));
			add(generate("itemStack", "minecraft:clay_ball", 64));
			add(generate("itemStack", "minecraft:sugar_cane", 32));
			add(generate("itemStack", "minecraft:melon_slice", 16));
			add(generate("itemStack", "minecraft:beef", 24));
			add(generate("itemStack", "minecraft:steak", 24));
			add(generate("itemStack", "minecraft:chicken", 24));
			add(generate("itemStack", "minecraft:cooked_chicken", 24));
			add(generate("itemStack", "minecraft:rotten_flesh", 24));
			add(generate("itemStack", "minecraft:ghast_tear", 4096));
			add(generate("itemStack", "minecraft:spider_eye", 128));
			add(generate("itemStack", "minecraft:baked_potato", 24));
			add(generate("itemStack", "minecraft:poisonous_potato", 24));

			add(generate("itemStack", "ee:item_chalk", 130/4f));
		}
	};
	
	private static CompoundNBT generate(String type, String name, float value)
	{
		CompoundNBT base = new CompoundNBT();
		CompoundNBT nameTag = new CompoundNBT();
		nameTag.putString("name", name);
		base.put(type, nameTag);
		base.putFloat("energyValue", value);
		return base;
	}
}
