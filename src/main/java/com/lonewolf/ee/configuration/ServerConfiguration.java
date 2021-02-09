package com.lonewolf.ee.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;

public class ServerConfiguration
{
	public final ForgeConfigSpec.ConfigValue<ArrayList<String>> blackListItems;
	
	public final ForgeConfigSpec.IntValue chalkMaxSize;
	
	public final ForgeConfigSpec.BooleanValue reloadDefaultEMC;
	public final ForgeConfigSpec.BooleanValue reCalculateEmcValues;
	
	public ServerConfiguration(ForgeConfigSpec.Builder builder)
	{
		builder.comment("Server configuration settings").push("server");
		
		chalkMaxSize = builder.comment("Defines the max chalk drawing size").defineInRange("chalkdrawmaxsize", 9, 0, Integer.MAX_VALUE);
		
		reloadDefaultEMC = builder.comment("Set to true to reset default emc values").define("defaultemc", false);
		
		reCalculateEmcValues = builder.comment("Set to true recalculate all emc values").define("recalc", true);
		
		blackListItems = builder.comment("EMC Blacklisted Items").translation("ee.configuration.emcblacklist").define(
				"blacklistitems", new ArrayList<>());
		
		builder.pop();
	}
}
