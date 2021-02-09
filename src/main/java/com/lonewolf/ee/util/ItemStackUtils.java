package com.lonewolf.ee.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class ItemStackUtils
{
	private static final Logger logger = LogManager.getLogger();
	
	public ItemStackUtils() {
	}
	
	public static ItemStack clone(ItemStack itemStack, int stackSize) {
		if (itemStack != null) {
			ItemStack clonedItemStack = itemStack.copy();
			clonedItemStack.setCount(stackSize);
			return clonedItemStack;
		} else {
			return null;
		}
	}
	
	public static boolean equals(ItemStack first, ItemStack second) {
		return ComparatorUtils.ID_COMPARATOR.compare(first, second) == 0;
	}
	
	public static boolean equalsIgnoreStackSize(ItemStack itemStack1, ItemStack itemStack2) {
		return equals(clone(itemStack1, 1), clone(itemStack2, 1));
	}
	
	public static int compare(ItemStack itemStack1, ItemStack itemStack2) {
		return ComparatorUtils.ID_COMPARATOR.compare(itemStack1, itemStack2);
	}
	
	public static String toString(ItemStack itemStack) {
		if (itemStack != null) {
			return itemStack.hasTag() ? String.format("%sxitemStack[%s@%s:%s]", itemStack.getCount(), itemStack.getItem().getRegistryName(), itemStack.getDamage(), itemStack.getTag()) : String.format("%sxitemStack[%s@%s]", itemStack.getCount(), itemStack.getItem().getRegistryName(), itemStack.getDamage());
		} else {
			return "null";
		}
	}
	
	public static void setOwner(ItemStack itemStack, PlayerEntity entityPlayer) {
		setOwnerName(itemStack, entityPlayer);
		setOwnerUUID(itemStack, entityPlayer);
	}
	
	public static String getOwnerName(ItemStack itemStack)
	{
		return NBTHelper.getString(itemStack, "owner");
	}
	
	public static UUID getOwnerUUID(ItemStack itemStack) {
		return NBTHelper.getLong(itemStack, "ownerUUIDMostSig") != null && NBTHelper.getLong(itemStack, "ownerUUIDLeastSig") != null ? new UUID(NBTHelper.getLong(itemStack, "ownerUUIDMostSig"), NBTHelper.getLong(itemStack, "ownerUUIDLeastSig")) : null;
	}
	
	public static void setOwnerUUID(ItemStack itemStack, PlayerEntity entityPlayer) {
		NBTHelper.setLong(itemStack, "ownerUUIDMostSig", entityPlayer.getUniqueID().getMostSignificantBits());
		NBTHelper.setLong(itemStack, "ownerUUIDLeastSig", entityPlayer.getUniqueID().getLeastSignificantBits());
	}
	
	public static void setOwnerName(ItemStack itemStack, PlayerEntity entityPlayer) {
		NBTHelper.setString(itemStack, "owner", entityPlayer.getDisplayName().getString());
	}
}
