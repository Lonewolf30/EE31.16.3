package com.lonewolf.ee.client.gui;

import com.lonewolf.ee.inventory.ContainerResearchStation;
import com.lonewolf.ee.reference.Reference;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class GuiResearchStation extends GuiBase<ContainerResearchStation>
{
	public static final ResourceLocation texture = new ResourceLocation(Reference.mod_id, "textures/gui/research/research_station.png");
	public int progression;
	
	public GuiResearchStation(
			ContainerResearchStation screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, new StringTextComponent(" "));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {

	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		progression = (int)((this.container.getData() / 200f) * 41);
		this.minecraft.getTextureManager().bindTexture(texture);
		int i = (this.getScreenWidth() - 256) / 2;
		int j = (this.getScreenHeight() - 256) / 2;
		this.blit(matrixStack, i, j, 0, 0, 256, 234);
		i = (this.getScreenWidth() - 256) / 2 + 107;
		j = (this.getScreenHeight() - 256) / 2 + 85;
		this.blit(matrixStack, i, j, 0, 235, progression, 16);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {

	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
