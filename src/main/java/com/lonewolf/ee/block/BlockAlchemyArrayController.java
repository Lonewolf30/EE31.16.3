package com.lonewolf.ee.block;

import com.lonewolf.ee.registry.BlockRegistry;
import com.lonewolf.ee.tile_entity.TileEntityAlchemyArray;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAlchemyArrayController extends BlockTileEntityEE
{
    private final VoxelShape tier1 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    public BlockAlchemyArrayController()
    {
        super("alchemy_array_controller", Properties.create(Material.CARPET).hardnessAndResistance(0.2f).notSolid());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return tier1;
    }

    @Override
    public VoxelShape getShape(
            BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return tier1;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityAlchemyArray)
        {
            ((TileEntityAlchemyArray) te).BreakArray();
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onBlockPlacedBy(
            World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        TileEntityAlchemyArray te = getTileEntity(worldIn, pos);

        te.setOwner(placer.getUniqueID());
    }

    public TileEntityAlchemyArray getTileEntity(World world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityAlchemyArray)
            return (TileEntityAlchemyArray) tileEntity;
        else
            return new TileEntityAlchemyArray();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public TileEntityAlchemyArray createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityAlchemyArray();
    }
}
