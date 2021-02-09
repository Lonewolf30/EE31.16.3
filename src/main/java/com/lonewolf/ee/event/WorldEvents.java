package com.lonewolf.ee.event;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.configuration.ConfigurationManager;
import com.lonewolf.ee.reference.Reference;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Reference.mod_id)
public class WorldEvents
{
	public static boolean hasInitialized = false;
	
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event)
	{
		if (!hasInitialized && event.getWorld() instanceof ServerWorld)
		{
			ServerWorld world = (ServerWorld) event.getWorld();
			
			world.getChunkProvider().getSavedData().getOrCreate(
					EquivalentExchange::getPlayerKnowledgeManager, "ee_player_knowledge");

			world.getChunkProvider().getSavedData().getOrCreate(
					EquivalentExchange.getEnergyValueManager().getExchangeValuesFileManager()::getPreExchangeValues,
					"ee_prevalues");
			
			world.getChunkProvider().getSavedData().getOrCreate(
					EquivalentExchange.getEnergyValueManager().getExchangeValuesFileManager()::getPostExchangeValues,
					"ee_postvalues");
			
			EquivalentExchange.getRecipeManager().registerVanillaRecipes(world.getRecipeManager());
			
			if (ConfigurationManager.SERVER.reCalculateEmcValues.get())
			{
				EquivalentExchange.getEnergyValueManager().compute();
				ConfigurationManager.SERVER.reCalculateEmcValues.set(false);
				ConfigurationManager.SERVER.reCalculateEmcValues.save();
			}
			
			hasInitialized = true;
		}
	}
}
