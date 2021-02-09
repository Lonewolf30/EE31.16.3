package com.lonewolf.ee.configuration;

import com.lonewolf.ee.reference.Reference;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber(modid = Reference.mod_id)
public class ConfigurationManager
{
	public static final ClientConfiguration CLIENT;
	public static final ServerConfiguration SERVER;
	public static final ForgeConfigSpec clientSpec;
	public static final ForgeConfigSpec serverSpec;
	
	static
	{
		final Pair<ClientConfiguration, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(
				ClientConfiguration::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}
	
	static
	{
		final Pair<ServerConfiguration, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(
				ServerConfiguration::new);
		serverSpec = specPair.getRight();
		SERVER = specPair.getLeft();
	}
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent)
	{
		LogManager.getLogger().debug("Loaded forge config file {}", configEvent.getConfig().getFileName());
	}
	
	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading configEvent)
	{
		LogManager.getLogger().debug("Forge config just got changed on the file system!");
	}
}
