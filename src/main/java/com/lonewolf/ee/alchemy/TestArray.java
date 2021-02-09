package com.lonewolf.ee.alchemy;

import com.lonewolf.ee.tile_entity.TileEntityAlchemyArray;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestArray extends AlchemyArray
{
    @Override
    public int getMaxSize() {
        return 5;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(TileEntityAlchemyArray tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

    }

    @Override
    public boolean check_craft(World worldIn, PlayerEntity entity, BlockPos blockPos) {
        return false;
    }

    @Override
    public void craft(World worldIn, PlayerEntity entity, BlockPos blockPos) {

    }
}
