package com.lonewolf.ee.item;

import com.lonewolf.ee.reference.Reference;
import com.lonewolf.ee.creative_tab.CreativeTab;
import net.minecraft.item.Item;

public class BaseItem extends Item
{
	public BaseItem(String name)
	{
		this(name, new Properties().setNoRepair());
	}
	
	public BaseItem(String name, Properties properties)
	{
		super(properties.group(CreativeTab.EE_TAB));
		setUnlocalizedName(name);
	}
	
	private void setUnlocalizedName(String name)
	{
		setRegistryName(Reference.mod_id, name);
	}
}
