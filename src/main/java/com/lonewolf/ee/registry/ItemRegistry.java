package com.lonewolf.ee.registry;

import com.lonewolf.ee.item.ItemAlchenomicon;
import com.lonewolf.ee.item.ItemChalk;
import com.lonewolf.ee.item.ItemPhilosophersStone;
import com.lonewolf.ee.reference.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Reference.mod_id)
public class ItemRegistry
{
	public static ItemChalk chalk;
	public static ItemPhilosophersStone philosophersStone;
	public static ItemAlchenomicon alchenomicon;
	
	static
	{
		chalk = new ItemChalk();
		philosophersStone = new ItemPhilosophersStone();
		alchenomicon = new ItemAlchenomicon();
	}
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> evt)
	{
		IForgeRegistry<Item> registry = evt.getRegistry();
		
		registry.register(BlockRegistry.researchStation.getItem());
		registry.register(BlockRegistry.ashInfusedStone.getItem());
		registry.register(BlockRegistry.blockChalk.getItem());
		
		registry.register(chalk);
		registry.register(philosophersStone);
		registry.register(alchenomicon);
	}
}
