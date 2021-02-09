package com.lonewolf.ee.inventory;

import com.lonewolf.ee.inventory.element.IElementButtonHandler;
import com.lonewolf.ee.inventory.element.IElementTextFieldHandler;
import com.lonewolf.ee.registry.ContainerRegistry;
import com.lonewolf.ee.util.ComparatorUtils;
import com.lonewolf.ee.util.FilterUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ContainerAlchenomicon extends ContainerEE implements IElementButtonHandler, IElementTextFieldHandler
{
	private final InventoryAlchenomicon inventoryAlchenomicon;
	private int pageOffset;
	private int maxPageOffset;
	private String searchTerm;
	private boolean requiresUpdate = false;
	
	public ContainerAlchenomicon(int id, PlayerInventory playerInventory)
	{
		this(id, playerInventory, new InventoryAlchenomicon());
	}
	
	public ContainerAlchenomicon(int id, PlayerInventory playerInventory, InventoryAlchenomicon inventory)
	{
		super(ContainerRegistry.containerAlchenomiconContainerType, id);
		this.inventoryAlchenomicon = inventory;
		this.pageOffset = 0;
		this.maxPageOffset = inventory.getSizeInventory() / 80;
		int i = 0;
		
		int rowIndex, columnIndex;
		for (rowIndex = 0; rowIndex < 8; ++rowIndex)
		{
			for (columnIndex = 0; columnIndex < 5; ++columnIndex)
			{
				this.addSlot(createSlot(this.inventoryAlchenomicon, i++, -20 + columnIndex * 20, rowIndex * 19));
			}
		}
		
		for (rowIndex = 0; rowIndex < 8; ++rowIndex)
		{
			for (columnIndex = 0; columnIndex < 5; ++columnIndex)
			{
				this.addSlot(createSlot(this.inventoryAlchenomicon, i++, 100 + columnIndex * 20, rowIndex * 19));
			}
		}
		
		updateInventory();
	}
	
	private Slot createSlot(IInventory inventory, int slotIndex, int xPos, int yPos)
	{
		return new Slot(inventory, slotIndex, xPos, yPos)
		{
			@Override
			public boolean canTakeStack(PlayerEntity playerIn)
			{
				return false;
			}
			
			@Override
			public boolean isEnabled()
			{
				return !inventory.getStackInSlot(slotIndex).isEmpty();
			}
		};
	}
	
	@Override
	public void handleElementButtonClick(String elementName, int mouseButton)
	{
		if (elementName.equalsIgnoreCase("prev") && mouseButton == 0 && this.pageOffset > 0)
		{
			--this.pageOffset;
			this.updateInventory();
		}
		else if (elementName.equalsIgnoreCase("next") && mouseButton == 0 && this.pageOffset < this.maxPageOffset)
		{
			++this.pageOffset;
			this.updateInventory();
		}
	}
	
	@Override
	public void handleElementTextFieldUpdate(String elementName, String updatedText)
	{
		if (elementName.equalsIgnoreCase("searchField"))
		{
			this.searchTerm = updatedText;
			this.pageOffset = 0;
			this.updateInventory();
		}
	}
	
	public int getInventorySize()
	{
		return inventoryAlchenomicon.getSizeInventory();
	}
	
	public int getKnownTransmutationsCount()
	{
		return this.inventoryAlchenomicon.getSizeInventory();
	}
	
	public int getPageOffset()
	{
		return this.pageOffset;
	}
	
	public int getMaxPageOffset()
	{
		return this.maxPageOffset;
	}
	
	private void updateInventory()
	{
		this.requiresUpdate = true;
		boolean shouldUpdateInventory = false;
		ItemStack[] newInventory = new ItemStack[80];
		List<ItemStack> filteredList = new ArrayList<>(FilterUtils.filterByDisplayName(inventoryAlchenomicon.getKnownTransmutations(), this.searchTerm,
		                                                                                 FilterUtils.NameFilterType.CONTAINS,
		                                                                                 ComparatorUtils.DISPLAY_NAME_COMPARATOR));
		this.maxPageOffset = filteredList.size() / 80;
		if (this.pageOffset > this.maxPageOffset)
		{
			this.pageOffset = 0;
		}
		
		if (this.pageOffset == 0)
		{
			if (filteredList.size() <= 80)
			{
				newInventory = filteredList.toArray(newInventory);
			}
			else
			{
				newInventory = filteredList.subList(0, 80).toArray(newInventory);
			}
			shouldUpdateInventory = true;
		}
		else if (this.pageOffset < this.maxPageOffset)
		{
			newInventory = filteredList.subList(this.pageOffset * 80, (this.pageOffset + 1) * 80).toArray(newInventory);
			shouldUpdateInventory = true;
		}
		else if (this.pageOffset == this.maxPageOffset)
		{
			newInventory = filteredList.subList(this.pageOffset * 80, filteredList.size() - 1).toArray(newInventory);
			shouldUpdateInventory = true;
		}
		
		if (shouldUpdateInventory)
		{
			for (int i = 0; i < 80; ++i)
			{
				this.inventoryAlchenomicon.setInventorySlotContents(i, newInventory[i]);
				this.inventoryAlchenomicon.markDirty();
			}
		}
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return true;
	}
}
