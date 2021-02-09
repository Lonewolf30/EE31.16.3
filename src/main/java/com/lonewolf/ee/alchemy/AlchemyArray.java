package com.lonewolf.ee.alchemy;

import com.lonewolf.ee.tile_entity.TileEntityAlchemyArray;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AlchemyArray
{
    public int getNeededInventorySize()
    {
        return 0;
    }

    public abstract int getMaxSize();

    public int getMinSize()
    {
        return 1;
    }

    public boolean rotationMatters()
    {
        return false;
    }

    public abstract void tick();

    public abstract void render(TileEntityAlchemyArray tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn);

    public abstract boolean check_craft(World worldIn, PlayerEntity entity, BlockPos blockPos);

    public abstract void craft(World worldIn, PlayerEntity entity, BlockPos blockPos);
}
