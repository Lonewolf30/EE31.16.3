package com.lonewolf.ee.block;

import com.lonewolf.ee.tile_entity.TileEntityResearchStation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class BlockResearchStation extends BlockTileEntityEE
{
	public BlockResearchStation()
	{
		super("research_station", Properties.create(Material.WOOD));
	}
	
	@Override
	public boolean isVariableOpacity()
	{
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(
			World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
	{
		TileEntityResearchStation te = getTileEntity(worldIn, pos);
		
		te.setOwner(placer.getUniqueID());
	}
	
	
	
	@Override
	public ActionResultType onBlockActivated(
			BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		player.openContainer(new INamedContainerProvider()
		{
			@Override
			public ITextComponent getDisplayName()
			{
				return new TranslationTextComponent("research_station_gui");
			}
			
			@Nullable
			@Override
			public Container createMenu(
					int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
			{
				return getTileEntity(worldIn, pos).createMenu(p_createMenu_1_, p_createMenu_2_);
			}
		});
		return ActionResultType.CONSUME;
	}
	
	public TileEntityResearchStation getTileEntity(World world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityResearchStation)
			return (TileEntityResearchStation) tileEntity;
		else
			return new TileEntityResearchStation();
	}
	
	@Override
	public void spawnAdditionalDrops(
			BlockState state, ServerWorld worldIn, BlockPos pos, ItemStack stack)
	{
		for (ItemStack teItem : getTileEntity(worldIn, pos).getAllItems())
		{
			worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), teItem));
		}
	}
	
	@Override
	public VoxelShape getRenderShape(
			BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return Block.makeCuboidShape(0, 0, 0, 1, 1, 1);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileEntityResearchStation();
	}
}
