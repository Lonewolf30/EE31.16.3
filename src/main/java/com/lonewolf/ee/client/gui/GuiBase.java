package com.lonewolf.ee.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public abstract class GuiBase<T extends Container> extends ContainerScreen<T>
{
	public GuiBase(T screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	public int getScreenWidth()
	{
		return this.width;
	}
	
	public int getScreenHeight()
	{
		return this.height;
	}
	
	public FontRenderer getFontRender()
	{
		return this.font;
	}
	
}
