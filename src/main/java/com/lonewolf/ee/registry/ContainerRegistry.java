package com.lonewolf.ee.registry;

import com.lonewolf.ee.client.gui.GuiResearchStation;
import com.lonewolf.ee.inventory.ContainerResearchStation;
import com.lonewolf.ee.reference.Reference;
import com.lonewolf.ee.client.gui.GuiAlchenomicon;
import com.lonewolf.ee.inventory.ContainerAlchenomicon;
import com.lonewolf.ee.tile_entity.TileEntityResearchStation;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Reference.mod_id)
public class ContainerRegistry
{
	public static ContainerType<ContainerAlchenomicon> containerAlchenomiconContainerType;
	public static ContainerType<ContainerResearchStation> containerResearchStationContainerType;
	
	static
	{
		containerAlchenomiconContainerType = register("container_alchenomicon", ContainerAlchenomicon::new);
		containerResearchStationContainerType = register("container_research_table", ContainerResearchStation::new);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<ContainerType<?>> evt)
	{
		IForgeRegistry<ContainerType<?>> registry = evt.getRegistry();

		ScreenManager.registerFactory(containerAlchenomiconContainerType, GuiAlchenomicon::new);
		ScreenManager.registerFactory(containerResearchStationContainerType, GuiResearchStation::new);
		
		registry.register(containerAlchenomiconContainerType);
		registry.register(containerResearchStationContainerType);
	}
	
	private static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory)
	{
		ContainerType<T> containerType = new ContainerType<T>(factory);
		containerType.setRegistryName(Reference.mod_id, key);
		return containerType;
	}
}
