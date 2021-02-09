package com.lonewolf.ee.creative_tab;

import com.lonewolf.ee.registry.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class CreativeTab extends ItemGroup
{
	public static final CreativeTab EE_TAB = new CreativeTab();
	
	public CreativeTab()
	{
		super("ee");
	}
	
	@OnlyIn(Dist.CLIENT)
	@Nonnull
	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ItemRegistry.philosophersStone);
	}
}
