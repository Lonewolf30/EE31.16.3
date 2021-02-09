package com.lonewolf.ee.event;

import com.lonewolf.ee.reference.Reference;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.mod_id)
public class PlayerEvents
{
	@SubscribeEvent
	public static void onPlayerLoad(PlayerEvent.PlayerLoggedInEvent event)
	{

	}


}
