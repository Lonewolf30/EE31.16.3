package com.lonewolf.ee.exchange;

import com.lonewolf.ee.configuration.ConfigurationManager;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class ExchangeBlackList
{
	public ExchangeBlackList()
	{
	}
	
	public ArrayList<String> getBlackListedItems()
	{
		return ConfigurationManager.SERVER.blackListItems.get();
	}
	
	public void addBlackListItem(ItemStack itemStack)
	{
		ConfigurationManager.SERVER.blackListItems.get().add(itemStack.getItem().getRegistryName().toString());
	}
	
	public boolean isItemBlacklisted(ItemStack itemStack)
	{
		return ConfigurationManager.SERVER.blackListItems.get().contains(
				itemStack.getItem().getRegistryName().toString());
	}
}
