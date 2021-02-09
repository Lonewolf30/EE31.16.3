package com.lonewolf.ee.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockChalk extends BlockEE
{
	public BlockChalk()
	{
		super("block_chalk", AbstractBlock.Properties.create(Material.CLAY).hardnessAndResistance(0.6F).sound(SoundType.GROUND));
	}
}
