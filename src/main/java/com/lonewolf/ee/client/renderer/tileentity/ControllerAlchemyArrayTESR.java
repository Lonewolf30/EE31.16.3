package com.lonewolf.ee.client.renderer.tileentity;

import com.lonewolf.ee.reference.Reference;
import com.lonewolf.ee.tile_entity.TileEntityAlchemyArray;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class ControllerAlchemyArrayTESR extends TileEntityRenderer<TileEntityAlchemyArray>
{
    private final ModelRenderer field_229048_g_;
    ResourceLocation texture;
    RenderType renderType;

    public ControllerAlchemyArrayTESR(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        this.field_229048_g_ = new ModelRenderer(16, 16, 0, 0);
        this.field_229048_g_.addBox(0, 0, 0, 16, 0.1f, 16);
    }

    @Override
    public void render(TileEntityAlchemyArray tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        texture = new ResourceLocation(tileEntityIn.getArrayName().getNamespace(),"textures/array/"+tileEntityIn.getArrayName().getPath()+".png");
        renderType = RenderType.getEntityCutoutNoCull(texture);

        matrixStackIn.push();
        int arraySize = tileEntityIn.getSize();
        int halfArray = Math.floorDiv(arraySize,2);
        matrixStackIn.translate(-halfArray, 0, -halfArray);
        matrixStackIn.scale(arraySize, 1, arraySize);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(renderType);
        int i = OverlayTexture.NO_OVERLAY;
        this.field_229048_g_.render(matrixStackIn, ivertexbuilder, combinedLightIn, i);
        matrixStackIn.pop();

//        tileEntityIn.getArray().render(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
