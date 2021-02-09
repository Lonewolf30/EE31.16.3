package com.lonewolf.ee.client.gui;

import com.lonewolf.ee.reference.Reference;
import com.lonewolf.ee.inventory.ContainerAlchenomicon;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiAlchenomicon extends GuiBase<ContainerAlchenomicon>
{
	public static final ResourceLocation texture = new ResourceLocation(Reference.mod_id,
			"textures/gui/alchenomicon.png");
	private final TextFieldWidget search;
	
	public GuiAlchenomicon(
			ContainerAlchenomicon screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, new StringTextComponent(" "));
		
		search = new TextFieldWidget(getFontRender(), 0, 0, 14, 14, new TranslationTextComponent("ee.gui.searchbox"));
		
		search.setEnableBackgroundDrawing(false);
		search.setFocused2(false);
		search.setCanLoseFocus(true);

		addListener(search);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.minecraft.getTextureManager().bindTexture(texture);
		int i = (this.getScreenWidth() - 256) / 2;
		int j = (this.getScreenHeight() - 201) / 2;
		this.blit(matrixStack, i, j, 0, 0, 256, 201);

		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void init(Minecraft minecraft, int width, int height) {
		String s = this.search.getText();
		this.search.setText(s);
		super.init(minecraft, width, height);
	}

	@Override
	public ITextComponent getTitle() {
		return new StringTextComponent(" ");
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {

	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y)
	{

	}

	@Override
	public FontRenderer getFontRender()
	{
		return font;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
