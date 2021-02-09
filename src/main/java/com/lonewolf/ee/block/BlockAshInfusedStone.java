package com.lonewolf.ee.block;

import net.minecraft.block.material.Material;

public class BlockAshInfusedStone extends BlockEE
{
	public BlockAshInfusedStone()
	{
		super("ash_infused_stone", Properties.create(Material.ROCK).hardnessAndResistance(1f));
	}
}
