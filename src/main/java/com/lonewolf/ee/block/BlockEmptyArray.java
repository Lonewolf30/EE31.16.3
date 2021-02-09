package com.lonewolf.ee.block;

import com.lonewolf.ee.tile_entity.TileEntityAlchemyArray;
import com.lonewolf.ee.tile_entity.TileEntityEmptyAlchemy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockEmptyArray extends BlockTileEntityEE
{
    private final VoxelShape tier1 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public BlockEmptyArray()
    {
        super("empty_alchemy", Properties.create(Material.WOOL).hardnessAndResistance(0.2f).notSolid());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return tier1;
    }

    @Override
    public VoxelShape getShape(
            BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return tier1;
    }

    @Override
    public void onBlockPlacedBy(
            World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        TileEntityEmptyAlchemy te = getTileEntity(worldIn, pos);

        te.setOwner(placer.getUniqueID());
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor)
    {
        if (!(world.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockAshInfusedStone))
        {
            BreakArray(world, pos);
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        BreakArray(worldIn, pos);

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    private void BreakArray(IWorldReader worldIn, BlockPos pos)
    {
        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof TileEntityEmptyAlchemy)
        {
            BlockPos controllerPos = ((TileEntityEmptyAlchemy) te).getControllerPos();
            if (controllerPos == null)
                controllerPos = new BlockPos(0,0,0);
            TileEntity controller = worldIn.getTileEntity(controllerPos);
            if (controller instanceof TileEntityAlchemyArray)
            {
                ((TileEntityAlchemyArray) controller).BreakArray();
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public TileEntityEmptyAlchemy getTileEntity(World world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityEmptyAlchemy)
            return (TileEntityEmptyAlchemy) tileEntity;
        else
            return new TileEntityEmptyAlchemy();
    }

    @Override
    public TileEntityEmptyAlchemy createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityEmptyAlchemy();
    }
}
