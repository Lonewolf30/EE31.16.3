package com.lonewolf.ee.inventory;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

public abstract class ContainerEE extends Container
{
	protected final int PLAYER_INVENTORY_ROWS = 3;
	protected final int PLAYER_INVENTORY_COLUMNS = 9;
	
	protected ContainerEE(ContainerType<?> type, int id)
	{
		super(type, id);
	}
	
}
