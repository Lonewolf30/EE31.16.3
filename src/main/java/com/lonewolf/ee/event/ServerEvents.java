package com.lonewolf.ee.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents
{
	@SubscribeEvent
	public static void onServerStart(FMLServerStartingEvent event)
	{
	
	}
	
	@SubscribeEvent
	public static void onServerStop(FMLServerStoppingEvent event)
	{
	
	}
}
