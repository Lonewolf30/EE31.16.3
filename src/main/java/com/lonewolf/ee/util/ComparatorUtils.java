package com.lonewolf.ee.util;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.exchange.WrappedStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

public class ComparatorUtils
{
	public static final Comparator<Collection<ItemStack>> ITEM_STACK_COLLECTION_COMPARATOR = (o1, o2) ->
	{
		if (o1 != null && o2 != null)
		{
			if (o1.size() == o2.size())
			{
				if (o1.containsAll(o2))
				{
					return o2.containsAll(o1) ? 0 : 1;
				}
				else
				{
					return -1;
				}
			}
			else
			{
				return o1.size() - o2.size();
			}
		}
		else if (o1 != null)
		{
			return -1;
		}
		else
		{
			return o2 != null ? 1 : 0;
		}
	};
	public static final Comparator<String> STRING_COMPARATOR = String::compareToIgnoreCase;
	
	public static final Comparator<Set<WrappedStack>> WRAPPED_STACK_SET_COMPARATOR = new Comparator<Set<WrappedStack>>()
	{
		public int compare(Set<WrappedStack> collection1, Set<WrappedStack> collection2)
		{
			if (collection1 != null && collection2 != null)
			{
				if (collection1.size() == collection2.size())
				{
					if (collection1.containsAll(collection2))
					{
						return collection2.containsAll(collection1) ? 0 : 1;
					}
					else
					{
						return -1;
					}
				}
				else
				{
					return collection1.size() - collection2.size();
				}
			}
			else if (collection1 != null)
			{
				return -1;
			}
			else
			{
				return collection2 != null ? 1 : 0;
			}
		}
	};
	
	public static final Comparator<ItemStack> ID_COMPARATOR = (itemStack1, itemStack2) ->
	{
		if (itemStack1 != null && itemStack2 != null)
		{
			// Sort on id
			if (Item.getIdFromItem(itemStack1.getItem()) - Item.getIdFromItem(itemStack2.getItem()) == 0)
			{
				// Sort on item
				if (itemStack1.getItem() == itemStack2.getItem())
				{
					
					// Then sort on NBT
					if (itemStack1.hasTag() && itemStack2.hasTag())
					{
						if (itemStack1.isDamageable() && itemStack2.isDamageable())
							return itemStack1.getDamage() - itemStack2.getDamage();
							
						// Then sort on stack size
						if (ItemStack.areItemStackTagsEqual(itemStack1, itemStack2))
						{
							return (itemStack1.getCount() - itemStack2.getCount());
						}
						else
						{
							return itemStack1.getTag().toString().compareTo(itemStack2.getTag().toString());
						}
					}
					else if (!(itemStack1.hasTag()) && itemStack2.hasTag())
					{
						return -1;
					}
					else if (itemStack1.hasTag() && !(itemStack2.hasTag()))
					{
						return 1;
					}
					else
					{
						return (itemStack1.getCount() - itemStack2.getCount());
					}
				}
				else
				{
					return itemStack1.getItem().getDisplayName(itemStack1).getUnformattedComponentText()
					                 .compareToIgnoreCase(itemStack2.getItem().getDisplayName(itemStack2)
					                                                .getUnformattedComponentText());
				}
			}
			else
			{
				return Item.getIdFromItem(itemStack1.getItem()) - Item.getIdFromItem(itemStack2.getItem());
			}
		}
		else if (itemStack1 != null)
		{
			return -1;
		}
		else if (itemStack2 != null)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	};
	public static final Comparator<ItemStack> ENERGY_VALUE_ITEM_STACK_COMPARATOR = (itemStack1, itemStack2) ->
	{
		if (itemStack1 != null && itemStack2 != null)
		{
			return EquivalentExchange.getEnergyValueManager().hasEnergyValue(itemStack1) && EquivalentExchange
					                                                                                .getEnergyValueManager()
					                                                                                .hasEnergyValue(
							                                                                                itemStack2) ? Float.compare(
					EquivalentExchange.getEnergyValueManager().getEnergyValue(itemStack1).getValue(),
					EquivalentExchange.getEnergyValueManager().getEnergyValue(itemStack2)
					                  .getValue()) : ComparatorUtils.ID_COMPARATOR.compare(itemStack1, itemStack2);
		}
		else if (itemStack1 != null)
		{
			return -1;
		}
		else
		{
			return itemStack2 != null ? 1 : 0;
		}
	};
	public static final Comparator<ItemStack> DISPLAY_NAME_COMPARATOR = (itemStack1, itemStack2) ->
	{
		if (itemStack1 != null && itemStack2 != null)
		{
			return itemStack1.getDisplayName().getString().equalsIgnoreCase(
					itemStack2.getDisplayName().getString()) ? ComparatorUtils.ID_COMPARATOR.compare(itemStack1,
			                                                                                         itemStack2) : itemStack1
					                                                                                                       .getDisplayName()
					                                                                                                       .getString()
					                                                                                                       .compareToIgnoreCase(
							                                                                                                       itemStack2
									                                                                                                       .getDisplayName()
									                                                                                                       .getString());
		}
		else if (itemStack1 != null)
		{
			return -1;
		}
		else
		{
			return itemStack2 != null ? 1 : 0;
		}
	};
	
	public ComparatorUtils()
	{
	}
}
