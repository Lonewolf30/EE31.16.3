package com.lonewolf.ee.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class NBTHelper
{
	private static final Logger logger = LogManager.getLogger();
	
	public NBTHelper()
	{
	}
	
	public static void clearStatefulNBTTags(ItemStack itemStack)
	{
		if (hasKey(itemStack, "craftingGuiOpen"))
		{
			removeTag(itemStack, "craftingGuiOpen");
		}
		else if (hasKey(itemStack, "transmutationGuiOpen"))
		{
			removeTag(itemStack, "transmutationGuiOpen");
		}
		else if (hasKey(itemStack, "alchemicalBagGuiOpen"))
		{
			removeTag(itemStack, "alchemicalBagGuiOpen");
		}
		
	}
	
	public static boolean hasKey(ItemStack itemStack, String keyName)
	{
		return itemStack != null && itemStack.hasTag() && itemStack.getTag().contains(keyName);
	}
	
	public static void removeTag(ItemStack itemStack, String keyName)
	{
		if (itemStack != null && itemStack.getChildTag(keyName) != null && !keyName.isEmpty())
		{
			itemStack.removeChildTag(keyName);
		}
		
	}
	
	public static boolean hasUUID(ItemStack itemStack)
	{
		return getLong(itemStack, "UUIDMostSig") != null && getLong(itemStack, "UUIDLeastSig") != null;
	}
	
	public static UUID getUUID(ItemStack itemStack)
	{
		return hasUUID(itemStack) ? new UUID(getLong(itemStack, "UUIDMostSig"),
		                                     getLong(itemStack, "UUIDLeastSig")) : null;
	}
	
	public static void setUUID(ItemStack itemStack, UUID uuid)
	{
		if (itemStack != null)
		{
			initNBTTagCompound(itemStack);
			if (uuid == null)
			{
				uuid = UUID.randomUUID();
			}
			
			setLong(itemStack, "UUIDMostSig", uuid.getMostSignificantBits());
			setLong(itemStack, "UUIDLeastSig", uuid.getLeastSignificantBits());
		}
		
	}
	
	private static void initNBTTagCompound(ItemStack itemStack)
	{
		if (itemStack != null && itemStack.getTag() == null)
		{
			itemStack.getOrCreateTag();
		}
		
	}
	
	public static String getString(ItemStack itemStack, String keyName)
	{
		return hasKey(itemStack, keyName) ? itemStack.getTag().getString(keyName) : null;
	}
	
	public static void setString(ItemStack itemStack, String keyName, String keyValue)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().putString(keyName, keyValue);
		}
	}
	
	public static Boolean getBoolean(ItemStack itemStack, String keyName)
	{
		if (hasKey(itemStack, keyName))
		{
			return itemStack.getTag().getBoolean(keyName);
		}
		
		return null;
	}
	
	public static void setBoolean(ItemStack itemStack, String keyName, boolean keyValue)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().putBoolean(keyName, keyValue);
		}
		
	}
	
	public static Byte getByte(ItemStack itemStack, String keyName)
	{
		return hasKey(itemStack, keyName) ? itemStack.getTag().getByte(keyName) : null;
	}
	
	public static void setByte(ItemStack itemStack, String keyName, byte keyValue)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().putByte(keyName, keyValue);
		}
		
	}
	
	public static Short getShort(ItemStack itemStack, String keyName)
	{
		return hasKey(itemStack, keyName) ? itemStack.getTag().getShort(keyName) : null;
	}
	
	public static void setShort(ItemStack itemStack, String keyName, short keyValue)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().putShort(keyName, keyValue);
		}
		
	}
	
	public static Integer getInteger(ItemStack itemStack, String keyName)
	{
		return hasKey(itemStack, keyName) ? itemStack.getTag().getInt(keyName) : null;
	}
	
	public static void setInteger(ItemStack itemStack, String keyName, int keyValue)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().putInt(keyName, keyValue);
		}
		
	}
	
	public static Long getLong(ItemStack itemStack, String keyName)
	{
		return hasKey(itemStack, keyName) ? itemStack.getTag().getLong(keyName) : null;
	}
	
	public static void setLong(ItemStack itemStack, String keyName, long keyValue)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().putLong(keyName, keyValue);
		}
		
	}
	
	public static Float getFloat(ItemStack itemStack, String keyName)
	{
		return hasKey(itemStack, keyName) ? itemStack.getTag().getFloat(keyName) : null;
	}
	
	public static void setFloat(ItemStack itemStack, String keyName, float keyValue)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().putFloat(keyName, keyValue);
		}
		
	}
	
	public static Double getDouble(ItemStack itemStack, String keyName)
	{
		return hasKey(itemStack, keyName) ? itemStack.getTag().getDouble(keyName) : null;
	}
	
	public static void setDouble(ItemStack itemStack, String keyName, double keyValue)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().putDouble(keyName, keyValue);
		}
		
	}
	
	public static ListNBT getTagList(ItemStack itemStack, String keyName, int nbtBaseType)
	{
		return hasKey(itemStack, keyName) && itemStack.getTag().get(keyName) instanceof ListNBT ? itemStack.getTag()
		                                                                                                   .getList(
				                                                                                                   keyName,
				                                                                                                   nbtBaseType) : null;
	}
	
	public static void setTagList(ItemStack itemStack, String keyName, ListNBT nbtTagList)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().put(keyName, nbtTagList);
		}
		
	}
	
	public static CompoundNBT getTagCompound(ItemStack itemStack, String keyName)
	{
		return hasKey(itemStack, keyName) ? itemStack.getTag().getCompound(keyName) : null;
	}
	
	public static void setTagCompound(ItemStack itemStack, String keyName, CompoundNBT nbtTagCompound)
	{
		if (itemStack != null && keyName != null && !keyName.isEmpty())
		{
			initNBTTagCompound(itemStack);
			itemStack.getTag().put(keyName, nbtTagCompound);
		}
		
	}
}
