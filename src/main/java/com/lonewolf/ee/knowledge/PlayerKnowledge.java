package com.lonewolf.ee.knowledge;

import com.lonewolf.ee.util.ComparatorUtils;
import com.lonewolf.ee.util.ItemStackUtils;
import net.minecraft.item.ItemStack;

import java.util.*;

public class PlayerKnowledge
{
	private final Set<ItemStack> knownItemStacks;
	
	public PlayerKnowledge() {
		this(Collections.EMPTY_SET);
	}
	
	public PlayerKnowledge(PlayerKnowledge playerKnowledge) {
		this(playerKnowledge.knownItemStacks);
	}
	
	public PlayerKnowledge(Collection<ItemStack> objects) {
		this.knownItemStacks = new TreeSet<>(ComparatorUtils.ID_COMPARATOR);
		if (objects != null) {
			for (ItemStack object : objects)
			{
				this.learn(object);
			}
		}
		
	}
	
	public boolean isKnown(Object object) {
		return object instanceof ItemStack && this.knownItemStacks.contains(ItemStackUtils.clone((ItemStack) object, 1));
	}
	
	public Set<ItemStack> getKnownItemStacks() {
		return this.knownItemStacks;
	}
	
	public void learn(ItemStack object) {
			ItemStack unitItemStack = ItemStackUtils.clone(object, 1);
			this.knownItemStacks.add(unitItemStack);
	}
	
	public void learn(Collection<ItemStack> objects) {
		if (objects != null) {
			for (ItemStack object : objects)
			{
				this.learn(object);
			}
		}
		
	}
	
	public void forget(Object object) {
		if (object instanceof ItemStack) {
			ItemStack unitItemStack = ItemStackUtils.clone((ItemStack)object, 1);
			this.knownItemStacks.remove(unitItemStack);
		}
		
	}
	
	public void forget(Collection<?> objects) {
		if (objects != null) {
			for (Object object : objects)
			{
				this.forget(object);
			}
		}
		
	}
	
	public void forgetAll() {
		this.knownItemStacks.clear();
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		
		for (ItemStack itemStack : this.knownItemStacks)
		{
			stringBuilder.append(String.format("%s, ", ItemStackUtils.toString(itemStack)));
		}
		
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}
