package com.lonewolf.ee.inventory;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.item.ItemAlchenomicon;
import com.lonewolf.ee.registry.ContainerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerResearchStation extends ContainerEE
{
	private PlayerInventory playerInventory;
	private IInventory inventory;
	private IIntArray data;
	
	public ContainerResearchStation(int id, PlayerInventory playerInventory)
	{
		this(id, playerInventory, new Inventory(2), new IntArray(1));
	}
	
	public ContainerResearchStation(int id, PlayerInventory playerInventory, IInventory tileEntityResearchStation, IIntArray data)
	{
		super(ContainerRegistry.containerResearchStationContainerType, id);
		assertInventorySize(tileEntityResearchStation, 2);
		assertIntArraySize(data, 1);
		this.playerInventory = playerInventory;
		this.data = data;
		
		this.addSlot(new Slot(tileEntityResearchStation, 0, 39,39) {
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				if (EquivalentExchange.getExchangeBlackList().isItemBlacklisted(stack))
					return false;

				if (stack.isDamaged())
					return false;

				return !(stack.getItem() instanceof ItemAlchenomicon);
			}
			
			@Override
			public int getSlotStackLimit()
			{
				return 1;
			}
		});
		this.addSlot(new Slot(tileEntityResearchStation, 1, 121,39) {
			@Override
			public int getSlotStackLimit()
			{
				return 1;
			}
			
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() instanceof ItemAlchenomicon;
			}
		});
		
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 10 + j * 18, 107 + i * 18));
			}
		}
		
		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 10 + k * 18, 165));
		}
		
		this.inventory = tileEntityResearchStation;
		this.trackIntArray(data);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
	{
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();
			if (index < this.inventory.getSizeInventory())
			{
				if (!this.mergeItemStack(itemStack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemStack1, 0, this.inventory.getSizeInventory(), false)){
				return ItemStack.EMPTY;
			}
			
			if (itemStack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}
		
		return itemStack;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getData()
	{
		return data.get(0);
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return true;
	}
}
