package com.lonewolf.ee.exchange;

import net.minecraft.nbt.CompoundNBT;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class OreStack implements Comparable<OreStack>
{
	public static Comparator<OreStack> comparator = (oreStack1, oreStack2) ->
	{
		if (oreStack1 != null && oreStack1.oreName != null)
		{
			if (oreStack2 != null && oreStack2.oreName != null)
			{
				return oreStack1.oreName.equalsIgnoreCase(
						oreStack2.oreName) ? oreStack1.stackSize - oreStack2.stackSize : oreStack1.oreName
								                                                                 .compareToIgnoreCase(
										                                                                 oreStack2.oreName);
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return oreStack2 != null ? 1 : 0;
		}
	};
	public String oreName;
	public int stackSize;
	
	private OreStack()
	{
	}
	
	public OreStack(String oreName)
	{
		this(oreName, 1);
	}
	
	public OreStack(String oreName, int stackSize)
	{
		this.oreName = oreName;
		this.stackSize = stackSize;
	}
	
	public OreStack(OreStack oreStack)
	{
		this.oreName = oreStack.oreName;
		this.stackSize = oreStack.stackSize;
	}
	
	public static boolean compareOreNames(OreStack oreStack1, OreStack oreStack2)
	{
		return oreStack1 != null && oreStack2 != null && oreStack1.oreName != null && oreStack2.oreName != null && oreStack1.oreName
				                                                                                                           .equalsIgnoreCase(
						                                                                                                           oreStack2.oreName);
	}
	
	public static OreStack getOreStackFrom(Object... objects)
	{
		return getOreStackFrom(Arrays.asList(objects));
	}
	
	public static OreStack getOreStackFrom(Collection<?> objects)
	{
		//		Tags.Blocks.ORES.func_230236_b_()
		//		String[] var1 = OreDictionary.getOreNames();
		//		int var2 = var1.length;
		//
		//		for(int var3 = 0; var3 < var2; ++var3) {
		//			String oreName = var1[var3];
		//			if (ComparatorUtils.ITEM_STACK_COLLECTION_COMPARATOR.compare(FilterUtils.filterForItemStacks(objects), OreDictionary.getOres(oreName)) == 0) {
		//				return new OreStack(oreName, 1);
		//			}
		//		}
		//
		return null;
	}
	
	public static int compare(OreStack oreStack1, OreStack oreStack2)
	{
		return comparator.compare(oreStack1, oreStack2);
	}
	
	public static OreStack loadOreStackFromNBT(CompoundNBT nbtTagCompound)
	{
		OreStack oreStack = new OreStack();
		oreStack.readFromNBT(nbtTagCompound);
		return oreStack.oreName != null ? oreStack : null;
	}
	
	public boolean equals(Object object)
	{
		return object instanceof OreStack && comparator.compare(this, (OreStack) object) == 0;
	}
	
	public CompoundNBT writeToNBT(CompoundNBT nbtTagCompound)
	{
		nbtTagCompound.putString("oreName", this.oreName);
		nbtTagCompound.putInt("stackSize", this.stackSize);
		return nbtTagCompound;
	}
	
	public void readFromNBT(CompoundNBT nbtTagCompound)
	{
		this.oreName = nbtTagCompound.getString("oreName");
		this.stackSize = nbtTagCompound.getInt("stackSize");
	}
	
	public String toString()
	{
		return String.format("%sxoreStack.%s", this.stackSize, this.oreName);
	}
	
	public int compareTo(OreStack oreStack)
	{
		return comparator.compare(this, oreStack);
	}
}
