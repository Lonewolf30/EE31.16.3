package com.lonewolf.ee.inventory;

import com.lonewolf.ee.knowledge.PlayerKnowledge;
import com.lonewolf.ee.util.ComparatorUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.Set;
import java.util.TreeSet;

public class InventoryAlchenomicon implements IInventory
{
	private final Set<ItemStack> knownTransmutations;
	private ItemStack[] inventory = new ItemStack[80];
	
	public InventoryAlchenomicon(PlayerKnowledge knowledge)
	{
		Set<ItemStack> knownTransmutations1;
		knownTransmutations1 = knowledge.getKnownItemStacks();
		if (knownTransmutations1 == null)
		{
			knownTransmutations1 = new TreeSet<>(ComparatorUtils.ID_COMPARATOR);
		}
		
		knownTransmutations = knownTransmutations1;
		this.inventory = knownTransmutations.toArray(this.inventory);
	}
	
	public InventoryAlchenomicon()
	{
		knownTransmutations = new TreeSet<>(ComparatorUtils.ID_COMPARATOR);
		this.inventory = knownTransmutations.toArray(this.inventory);
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
	}
	
	@Override
	public boolean isEmpty()
	{
		return false;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		ItemStack stack = index < this.getSizeInventory() ? this.inventory[index] : ItemStack.EMPTY;
		if (stack == null) stack = ItemStack.EMPTY;
		return stack;
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		ItemStack stack = this.getStackInSlot(index);
		if (stack != null)
		{
			if (stack.getCount() <= count)
			{
				this.setInventorySlotContents(index, ItemStack.EMPTY);
			}
			else
			{
				stack.setCount(stack.getCount() - count);
				if (stack.getCount() == 0) this.setInventorySlotContents(index, ItemStack.EMPTY);
			}
		}
		
		return stack;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (this.getStackInSlot(index) != ItemStack.EMPTY)
		{
			ItemStack stack = this.inventory[index];
			this.inventory[index] = ItemStack.EMPTY;
			return stack;
		}
		else
		{
			return ItemStack.EMPTY;
		}
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		if (index < this.inventory.length)
		{
			this.inventory[index] = stack;
		}
	}
	
	@Override
	public void markDirty()
	{
	
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return true;
	}
	
	@Override
	public void clear()
	{
	}
	
	public Set<ItemStack> getKnownTransmutations() {return this.knownTransmutations;}
}
