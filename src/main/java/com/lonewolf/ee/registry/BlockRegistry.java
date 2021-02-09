package com.lonewolf.ee.registry;

import com.lonewolf.ee.block.*;
import com.lonewolf.ee.reference.Reference;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Reference.mod_id)
public class BlockRegistry
{
	public static BlockResearchStation researchStation;
	public static BlockAshInfusedStone ashInfusedStone;
	public static BlockChalk blockChalk;
    public static BlockEmptyArray emptyAlchemy;
    public static BlockAlchemyArrayController alchemyController;

    static
	{
		researchStation = new BlockResearchStation();
		ashInfusedStone = new BlockAshInfusedStone();
		blockChalk = new BlockChalk();
		emptyAlchemy = new BlockEmptyArray();
		alchemyController = new BlockAlchemyArrayController();
	}
	
	@SubscribeEvent
	public static void BlockRegister(RegistryEvent.Register<Block> evt)
	{
		IForgeRegistry<Block> registry = evt.getRegistry();
		
		registry.register(researchStation);
		registry.register(ashInfusedStone);
		registry.register(blockChalk);
		registry.register(emptyAlchemy);
		registry.register(alchemyController);
	}
}
