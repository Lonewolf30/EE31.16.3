package com.lonewolf.ee.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public abstract class BlockTileEntityEE extends BlockEE
{
	protected BlockTileEntityEE(String blockName, Properties builder)
	{
		super(blockName, builder);
	}

	public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
}
