package com.lonewolf.ee.client.renderer.model;

import com.google.common.collect.ImmutableList;
import com.lonewolf.ee.reference.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AlchemyArrayModel extends Model
{
    private final ModelRenderer coverRight = (new ModelRenderer(512, 512, 0, 0)).addBox(0, 0, 0, 16, 16, 16F);
    private final List<ModelRenderer> bookParts;

    public AlchemyArrayModel() {
        super(RenderType::getEntitySolid);
        this.bookParts = ImmutableList.of(this.coverRight);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.renderAll(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderAll(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bookParts.forEach((p_228248_8_) -> {
            p_228248_8_.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        });
    }
}
