package com.lonewolf.ee.client.handler;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.client.renderer.model.AlchemyArrayModel;
import com.lonewolf.ee.item.ItemChalk;
import com.lonewolf.ee.reference.Reference;
import com.lonewolf.ee.settings.ChalkSettings;
import com.lonewolf.ee.settings.ChalkSettingsManager;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Reference.mod_id)
public class DrawBlockHighlightEvent
{
	public static final RenderMaterial TEXTURE_BOOK = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("entity/enchanting_table_book"));
	private static final AlchemyArrayModel modelBook = new AlchemyArrayModel();

	@SubscribeEvent
	public static void drawBlockHighLight(DrawHighlightEvent.HighlightBlock event)
	{
		for (ItemStack stack : event.getInfo().getRenderViewEntity().getHeldEquipment())
		{
			if (stack != null)
			{
				if (stack.getItem() instanceof ItemChalk)
				{
//					drawAlchemyArrayOverlay(event);
				}
			}
		}
	}
	
	private static void drawAlchemyArrayOverlay(DrawHighlightEvent.HighlightBlock event)
	{
		UUID playerID = event.getInfo().getRenderViewEntity().getUniqueID();

		ChalkSettings chalkSettings = EquivalentExchange.getChalkSettingsManager().getChalkSetting(playerID);

//		MatrixStack matrixStackIn = event.getMatrix();
//		matrixStackIn.translate(-event.getInfo().getRenderViewEntity().getPosX(),-event.getInfo().getRenderViewEntity().getPosY(),-event.getInfo().getRenderViewEntity().getPosZ());
//		matrixStackIn.push();
//		BlockPos pos = event.getTarget().getPos();
//		matrixStackIn.translate(pos.getX(), pos.getY()+1, pos.getZ());
////		matrixStackIn.translate(0.5D, 0.75D, 0.5D);
////		matrixStackIn.translate(0.0D, (0.1F + MathHelper.sin(0 * 0.1F) * 0.01F), 0.0D);
//
//		float f2 = 0;
//		matrixStackIn.rotate(Vector3f.YP.rotation(-f2));
//		matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(80.0F));
//		float f3 = MathHelper.lerp(0, 0, 0);
//		float f4 = MathHelper.frac(f3 + 0.25F) * 1.6F - 0.3F;
//		float f5 = MathHelper.frac(f3 + 0.75F) * 1.6F - 0.3F;
//		float f6 = MathHelper.lerp(0, 0, 0);
//		IVertexBuilder ivertexbuilder = TEXTURE_BOOK.getBuffer(event.getBuffers(), RenderType::getEntitySolid);
//		modelBook.renderAll(matrixStackIn, ivertexbuilder, 0, 0, 1.0F, 1.0F, 1.0F, 1.0F);
//		matrixStackIn.pop();

	}
}
