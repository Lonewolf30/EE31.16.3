package com.lonewolf.ee.block;

import com.lonewolf.ee.creative_tab.CreativeTab;
import com.lonewolf.ee.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockEE extends Block
{
	private final Item item;
	
	public BlockEE(String blockName, Properties builder)
	{
		super(builder);
		this.setRegistryName(Reference.mod_id, blockName);
		item = new BlockItem(this, new Item.Properties().group(CreativeTab.EE_TAB)).setRegistryName(Reference.mod_id, blockName);
	}
	
	public Item getItem()
	{
		return item;
	}
}
