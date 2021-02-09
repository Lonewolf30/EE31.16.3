package com.lonewolf.ee.util;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.exchange.EnergyValue;
import com.lonewolf.ee.exchange.WrappedStack;
import net.minecraft.item.ItemStack;

import java.util.*;

public class FilterUtils
{
	public FilterUtils()
	{
	}
	
	public static Set<ItemStack> filterByDisplayName(Set<ItemStack> itemStacks, String filterString)
	{
		return filterByDisplayName(itemStacks, filterString, FilterUtils.NameFilterType.STARTS_WITH, null);
	}
	
	public static Set<ItemStack> filterByDisplayName(
			Set<ItemStack> itemStacks, String filterString, FilterUtils.NameFilterType filterType)
	{
		return filterByDisplayName(itemStacks, filterString, filterType, null);
	}
	
	public static Set<ItemStack> filterByDisplayName(
			Collection<ItemStack> itemStacks, String filterString, FilterUtils.NameFilterType filterType,
			Comparator<ItemStack> comparator)
	{
		Set<ItemStack> filteredSet = comparator != null ? new TreeSet<ItemStack>(comparator) : new TreeSet<>(
				ComparatorUtils.DISPLAY_NAME_COMPARATOR);
		if (itemStacks != null)
		{
			if (filterString != null && !filterString.isEmpty())
			{
				Iterator<ItemStack> var5 = itemStacks.iterator();
				
				while (true)
				{
					while (var5.hasNext())
					{
						ItemStack itemStack = var5.next();
						String itemDisplayName = itemStack.getDisplayName().getString().toLowerCase();
						if (filterType == FilterUtils.NameFilterType.STARTS_WITH && itemDisplayName.startsWith(
								filterString.toLowerCase()))
						{
							filteredSet.add(itemStack);
						}
						else if (filterType == FilterUtils.NameFilterType.CONTAINS && itemDisplayName.contains(
								filterString.toLowerCase()))
						{
							filteredSet.add(itemStack);
						}
					}
					
					return filteredSet;
				}
			}
			else
			{
				filteredSet.addAll(itemStacks);
			}
		}
		
		return filteredSet;
	}
	
		public static Set<ItemStack> filterByEnergyValue(Collection<ItemStack> itemStacks, Number valueBound) {
			return filterByEnergyValue(itemStacks, (EnergyValue)(new EnergyValue(valueBound.floatValue())), FilterUtils.ValueFilterType.VALUE_LOWER_THAN_BOUND, (Comparator<ItemStack>)null);
		}

		public static Set<ItemStack> filterByEnergyValue(Collection<ItemStack> itemStacks, EnergyValue valueBound) {
			return filterByEnergyValue(itemStacks, (EnergyValue)valueBound, FilterUtils.ValueFilterType.VALUE_LOWER_THAN_BOUND, (Comparator<ItemStack>)null);
		}

		public static Set<ItemStack> filterByEnergyValue(Collection<ItemStack> itemStacks, Number valueBound, FilterUtils.ValueFilterType filterType) {
			return filterByEnergyValue(itemStacks, (EnergyValue)(new EnergyValue(valueBound.floatValue())), filterType, (Comparator<ItemStack>)null);
		}

		public static Set<ItemStack> filterByEnergyValue(Collection<ItemStack> itemStacks, EnergyValue valueBound, FilterUtils.ValueFilterType filterType) {
			return filterByEnergyValue(itemStacks, (EnergyValue)valueBound, filterType, (Comparator<ItemStack>)null);
		}

		public static Set<ItemStack> filterByEnergyValue(Collection<ItemStack> itemStacks, Number valueBound, FilterUtils.ValueFilterType filterType, Comparator<ItemStack> comparator) {
			return filterByEnergyValue(EquivalentExchange.getEnergyValueManager().getEnergyValues(), itemStacks, new EnergyValue(valueBound.floatValue()), filterType, comparator);
		}

		public static Set<ItemStack> filterByEnergyValue(Collection<ItemStack> itemStacks, EnergyValue valueBound, FilterUtils.ValueFilterType filterType, Comparator<ItemStack> comparator) {
			return filterByEnergyValue(EquivalentExchange.getEnergyValueManager().getEnergyValues(), itemStacks, valueBound, filterType, comparator);
		}

		public static Set<ItemStack> filterByEnergyValue(Map<WrappedStack, EnergyValue> valueMap, Collection<ItemStack> itemStacks, EnergyValue valueBound, FilterUtils.ValueFilterType filterType, Comparator<ItemStack> comparator) {
			TreeSet<ItemStack> filteredSet = comparator != null ? new TreeSet<>(comparator) : new TreeSet<>(ComparatorUtils.DISPLAY_NAME_COMPARATOR);
			if (itemStacks != null) {
				if (valueBound == null) {
					filteredSet.addAll(itemStacks);
				} else {
					Iterator<ItemStack> var6 = itemStacks.iterator();

					while(true) {
						while(true) {
							ItemStack itemStack;
							EnergyValue energyValue;
							do {
								do {
									if (!var6.hasNext()) {
										return filteredSet;
									}

									itemStack = var6.next();
									energyValue = EquivalentExchange.getEnergyValueManager().getEnergyValue(valueMap, itemStack, false);
								} while(energyValue == null);
							} while(Float.compare(energyValue.getValue(), 0.0F) <= 0);

							if (filterType == FilterUtils.ValueFilterType.VALUE_LOWER_THAN_BOUND && energyValue.compareTo(valueBound) <= 0) {
								filteredSet.add(itemStack);
							} else if (filterType == FilterUtils.ValueFilterType.VALUE_GREATER_THAN_BOUND && energyValue.compareTo(valueBound) >= 0) {
								filteredSet.add(itemStack);
							}
						}
					}
				}
			}

			return filteredSet;
		}

	public static Collection<ItemStack> filterForItemStacks(Collection<?> objects)
	{
		Set<ItemStack> itemStacks = new TreeSet<>(ComparatorUtils.ID_COMPARATOR);
		Iterator var2 = objects.iterator();
		
		while (var2.hasNext())
		{
			Object object = var2.next();
			if (object instanceof ItemStack)
			{
				itemStacks.add((ItemStack) object);
			}
		}
		
		return itemStacks;
	}
	
	public enum ValueFilterType
	{
		VALUE_GREATER_THAN_BOUND, VALUE_LOWER_THAN_BOUND;
		
		ValueFilterType()
		{
		}
	}
	
	public enum NameFilterType
	{
		STARTS_WITH, CONTAINS;
		
		NameFilterType()
		{
		}
	}
}
