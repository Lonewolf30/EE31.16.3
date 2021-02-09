package com.lonewolf.ee.commands;

import com.lonewolf.ee.reference.Reference;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Reference.mod_id)
public class CommandManager
{
	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent evt)
	{
		GetItemTagsCommand.register(evt.getDispatcher());
		GetItemEMCCommand.register(evt.getDispatcher());
		RecalculateEMC.register(evt.getDispatcher());
		ReloadDefaultEMCValues.register(evt.getDispatcher());
	}
}
