package com.lonewolf.ee.exchange;

import com.google.common.collect.ImmutableSortedMap;
import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.Settings;
import com.lonewolf.ee.event.EnergyValueEvent;
import com.lonewolf.ee.util.ComparatorUtils;
import com.lonewolf.ee.util.FilterUtils;
import com.lonewolf.ee.util.LogHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class EnergyValueManager
{
	private static final Marker ENERGY_VALUE_MARKER = MarkerManager.getMarker("Energy Value Manager");
	private final Map<WrappedStack, EnergyValue> preCalculationStackValueMap;
	private final ExchangeValuesFileManager exchangeValuesFileManager;
	private Map<WrappedStack, EnergyValue> postCalculationStackValueMap;
	private ImmutableSortedMap<WrappedStack, EnergyValue> stackValueMap;
	private transient SortedSet<WrappedStack> uncomputedStacks;
	private ImmutableSortedMap<EnergyValue, Set<WrappedStack>> valueStackMap;
	private transient boolean valuesNeedRegeneration;
	
	public EnergyValueManager()
	{
		ImmutableSortedMap.Builder<WrappedStack, EnergyValue> stackMapBuilder = new ImmutableSortedMap.Builder<>(
				WrappedStack::compareTo);
		this.stackValueMap = stackMapBuilder.build();
		this.preCalculationStackValueMap = new TreeMap<>();
		this.postCalculationStackValueMap = new TreeMap<>();
		this.uncomputedStacks = new TreeSet<>();
		exchangeValuesFileManager = new ExchangeValuesFileManager();
	}
	
	private static Set<ItemStack> filterForItemStacks(Set<WrappedStack> wrappedStacks)
	{
		TreeSet<ItemStack> itemStacks = new TreeSet<>(ComparatorUtils.ID_COMPARATOR);
		
		for (WrappedStack wrappedStack : wrappedStacks)
		{
			if (wrappedStack.getWrappedObject() instanceof ItemStack)
			{
				itemStacks.add((ItemStack) wrappedStack.getWrappedObject());
			}
			else if (wrappedStack.getWrappedObject() instanceof OreStack)
			{
				ITag<Item> tag = ItemTags.getCollection().getTagByID(
						new ResourceLocation(((OreStack) wrappedStack.getWrappedObject()).oreName));
				for (Item o : tag.getAllElements())
				{
					itemStacks.add(new ItemStack(o));
				}
			}
		}
		
		return itemStacks;
	}
	
	private static Set<ItemStack> getStacksInRange(
			Map<WrappedStack, EnergyValue> valueMap, EnergyValue energyValueBound, boolean isUpperBound)
	{
		Set<ItemStack> itemStacks = filterForItemStacks(valueMap.keySet());
		if (energyValueBound != null)
		{
			return isUpperBound ? FilterUtils.filterByEnergyValue(valueMap, itemStacks, energyValueBound,
			                                                      FilterUtils.ValueFilterType.VALUE_LOWER_THAN_BOUND,
			                                                      ComparatorUtils.ENERGY_VALUE_ITEM_STACK_COMPARATOR) : FilterUtils
					                                                                                                            .filterByEnergyValue(
							                                                                                                            valueMap,
							                                                                                                            itemStacks,
							                                                                                                            energyValueBound,
							                                                                                                            FilterUtils.ValueFilterType.VALUE_GREATER_THAN_BOUND,
							                                                                                                            ComparatorUtils.ENERGY_VALUE_ITEM_STACK_COMPARATOR);
		}
		else
		{
			//noinspection unchecked
			return new TreeSet<ItemStack>(Collections.EMPTY_SET);
		}
	}
	
	public ExchangeValuesFileManager getExchangeValuesFileManager()
	{
		return exchangeValuesFileManager;
	}
	
	public void load()
	{
		Map<WrappedStack, EnergyValue> valueMap = exchangeValuesFileManager.getPreValues();
		
		if (valueMap != null)
		{
			ImmutableSortedMap.Builder<WrappedStack, EnergyValue> stackMappingsBuilder = new ImmutableSortedMap.Builder<>(
					WrappedStack::compareTo);
			valueMap.keySet().stream().filter(
					(wrappedStack) -> wrappedStack != null && wrappedStack.getWrappedObject() != null && valueMap.get(
							wrappedStack) != null).forEach((wrappedStack) ->
							                               {
								                               stackMappingsBuilder.put(wrappedStack,
								                                                        valueMap.get(wrappedStack));
							                               });
			this.stackValueMap = stackMappingsBuilder.build();
			this.calculateValueStackMap();
		}
	}
	
	private void calculateValueStackMap()
	{
		SortedMap<EnergyValue, Set<WrappedStack>> tempValueMap = new TreeMap<>();
		
		for (WrappedStack wrappedStack : this.getEnergyValues().keySet())
		{
			if (wrappedStack != null)
			{
				EnergyValue energyValue = this.getEnergyValues().get(wrappedStack);
				if (energyValue != null)
				{
					if (tempValueMap.containsKey(energyValue))
					{
						tempValueMap.get(energyValue).add(wrappedStack);
					}
					else
					{
						tempValueMap.put(energyValue, new TreeSet<>(Collections.singletonList(wrappedStack)));
					}
				}
			}
		}
		
		this.valueStackMap = ImmutableSortedMap.copyOf(tempValueMap);
	}
	
	public EnergyValue getEnergyValue(Map<WrappedStack, EnergyValue> valueMap, Object object, boolean strict)
	{
		if (!WrappedStack.canBeWrapped(object)) return null;
		
		WrappedStack wrappedStack = WrappedStack.wrap(object, 1);
		Object wrappedObject = wrappedStack.getWrappedObject();
		if (wrappedObject instanceof ItemStack && ((ItemStack) wrappedObject)
				                                          .getItem() instanceof IEnergyValueProvider && !strict)
		{
			EnergyValue energyValue = ((IEnergyValueProvider) ((ItemStack) wrappedObject).getItem()).getEnergyValue(
					(ItemStack) wrappedObject);
			if (energyValue != null && Float.compare(energyValue.getValue(), 0.0F) > 0) return energyValue;
		}
		
		if (valueMap != null && !valueMap.isEmpty())
		{
			EnergyValue minEnergyValue;

			if (wrappedObject instanceof ItemStack && ((ItemStack) wrappedObject).isDamaged())
			{
				ItemStack itemStack = ((ItemStack) wrappedObject).copy();
				Item wrappedItem = itemStack.getItem();
				EnergyValue energyValue = getEnergyValue(wrappedItem);
				float valuePercent = (itemStack.getMaxDamage() - itemStack.getDamage()) / (float)itemStack.getMaxDamage();
				return new EnergyValue(energyValue.getValue() * valuePercent);
			}
			
			if (valueMap.containsKey(wrappedStack))
				return valueMap.get(wrappedStack);
			
			if (!strict)
			{
				ItemStack valuedItemStack;
				if (wrappedObject instanceof ItemStack)
				{
					ItemStack unValuedItemstack = ((ItemStack) wrappedObject).copy();
					minEnergyValue = null;
					
					Collection<ResourceLocation> tags = ItemTags.getCollection().getOwningTags(
							unValuedItemstack.getItem());
					
					if (tags.size() > 0)
					{
						final EnergyValue[] energyValue = {null};
						ResourceLocation[] resourceLocations = tags.toArray(new ResourceLocation[0]);
						HashMap<WrappedStack, EnergyValue> valueHashMap = new HashMap<>();
						
						for (ResourceLocation resourceLocation : resourceLocations)
						{
							String oreName = resourceLocation.getPath();
							WrappedStack oreStack = WrappedStack.wrap(new OreStack(oreName));
							valueHashMap.put(oreStack, valueMap.get(oreStack));
						}
						
						valueHashMap.forEach((wrappedStack1, energyValue1) ->
						                     {
							                     if (energyValue1 == null) return;
							
							                     if (energyValue[0] == null) energyValue[0] = energyValue1;
							
							                     if (energyValue1.getValue() < energyValue[0].getValue())
							                     {
								                     energyValue[0] = energyValue1;
							                     }
						                     });
						return energyValue[0];
					}
					else
					{
						Iterator<WrappedStack> iterator = valueMap.keySet().iterator();
						
						while (true)
						{
							WrappedStack valuedWrappedStack;
							do
							{
								do
								{
									do
									{
										if (!iterator.hasNext())
										{
											return null;
										}
										valuedWrappedStack = iterator.next();
									}
									while (!(valuedWrappedStack.getWrappedObject() instanceof ItemStack));
								}
								while (!ItemStack.areItemsEqualIgnoreDurability((ItemStack) valuedWrappedStack.getWrappedObject(), unValuedItemstack));
								
								
								valuedItemStack = (ItemStack) valuedWrappedStack.getWrappedObject();
							}
							while (!valuedItemStack.isDamaged() && !unValuedItemstack.isDamageable());
							
							EnergyValue energyValue = valueMap.get(valuedWrappedStack);
							if (energyValue.compareTo(minEnergyValue) < 0) minEnergyValue = energyValue;
						}
					}
				}
				else if (wrappedObject instanceof OreStack)
				{
					OreStack oreStack = (OreStack) wrappedObject;
					
					List<Item> itemStacks = new ArrayList<>();
					
					ITag<Item> minecraftItems = ItemTags.getCollection().get(
							new ResourceLocation("minecraft", oreStack.oreName));
					if (minecraftItems != null) itemStacks.addAll(minecraftItems.getAllElements());
					
					ITag<Item> forgeItems = ItemTags.getCollection().get(
							new ResourceLocation("forge", oreStack.oreName));
					if (forgeItems != null) itemStacks.addAll(forgeItems.getAllElements());
					
					ITag<Item> oreItems = ItemTags.getCollection().get(new ResourceLocation(oreStack.oreName));
					if (oreItems != null) itemStacks.addAll(oreItems.getAllElements());
					
					if (!itemStacks.isEmpty())
					{
						EnergyValue energyValue = null;
						boolean allHaveSameValue = true;
						
						Iterator<Item> itemIterator = itemStacks.iterator();
						
						while (true)
						{
							while (itemIterator.hasNext())
							{
								valuedItemStack = new ItemStack(itemIterator.next());
								WrappedStack wrappedItem = WrappedStack.wrap(valuedItemStack);
								if (wrappedItem != null && valueMap.containsKey(wrappedItem))
								{
									if (energyValue == null)
									{
										energyValue = valueMap.get(wrappedItem);
									}
									else if (!energyValue.equals(valueMap.get(wrappedItem)))
									{
										allHaveSameValue = false;
									}
								}
								else
								{
									allHaveSameValue = false;
								}
							}
							
							if (allHaveSameValue) return energyValue;
							
							break;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public void compute()
	{
		valuesNeedRegeneration = false;
		
		// Initialize the "working copy" energy value map
		final Map<WrappedStack, EnergyValue> stackValueMap = new TreeMap<>();
		uncomputedStacks = new TreeSet<>();
		
		// Load in pre calculation value assignments from file
		Map<WrappedStack, EnergyValue> fileValueMap = exchangeValuesFileManager.getPreValues();
		
		for (WrappedStack wrappedStack : fileValueMap.keySet())
		{
			if (wrappedStack != null && wrappedStack.getWrappedObject() != null && fileValueMap.get(
					wrappedStack) != null)
			{
				preCalculationStackValueMap.put(wrappedStack, fileValueMap.get(wrappedStack));
			}
		}
		
		LogHelper.info(ENERGY_VALUE_MARKER, "Loaded {} pre emc values", fileValueMap.size());
		
		// Add in all pre-calculation energy value mappings
		preCalculationStackValueMap.keySet().stream().filter(wrappedStack -> wrappedStack != null && wrappedStack
				                                                                                             .getWrappedObject() != null && preCalculationStackValueMap
						                                                                                                                            .get(wrappedStack) != null)
		                           .forEach(wrappedStack -> stackValueMap.put(wrappedStack, preCalculationStackValueMap
				                                                                                    .get(wrappedStack)));
		
		// Calculate values from the known methods to create items, and the pre-calculation value mappings
		Map<WrappedStack, EnergyValue> computedStackValueMap = calculateStackValueMap(stackValueMap);
		LogHelper.info(ENERGY_VALUE_MARKER, "Calculated {} EMC values", new Object[]{computedStackValueMap.size()});
		for (WrappedStack wrappedStack : computedStackValueMap.keySet())
		{
			stackValueMap.put(wrappedStack, computedStackValueMap.get(wrappedStack));
		}
		
		// Load in post calculation value assignments from file
		fileValueMap = getExchangeValuesFileManager().getPostValues();
		
		if (fileValueMap != null)
		{
			for (WrappedStack wrappedStack : fileValueMap.keySet())
			{
				if (wrappedStack != null && wrappedStack.getWrappedObject() != null && fileValueMap.get(
						wrappedStack) != null)
				{
					postCalculationStackValueMap.put(wrappedStack, fileValueMap.get(wrappedStack));
				}
			}
		}
		
		// Add in all post-calculation energy value mappings
		postCalculationStackValueMap.keySet().stream().filter(wrappedStack -> wrappedStack != null && wrappedStack
				                                                                                              .getWrappedObject() != null && postCalculationStackValueMap
						                                                                                                                             .get(wrappedStack) != null)
		                            .forEach(wrappedStack -> stackValueMap.put(wrappedStack,
		                                                                       postCalculationStackValueMap
				                                                                       .get(wrappedStack)));
		
		// Bake the final calculated energy value maps
		ImmutableSortedMap.Builder<WrappedStack, EnergyValue> stackMappingsBuilder = ImmutableSortedMap.naturalOrder();
		stackMappingsBuilder.putAll(stackValueMap);
		this.stackValueMap = stackMappingsBuilder.build();
		calculateValueStackMap();
		
		// Save the results to disk
		save();
		
		// Report the objects for which we were unable to compute an energy value for
		uncomputedStacks.stream().filter(wrappedStack -> getEnergyValue(stackValueMap, wrappedStack, false) == null)
		                .forEach(wrappedStack -> LogHelper.info(ENERGY_VALUE_MARKER,
		                                                        "Unable to compute an energy value for {}",
		                                                        wrappedStack));
	}
	
	private void save()
	{
		LogHelper.info(ENERGY_VALUE_MARKER, "Energy Values Save");
		exchangeValuesFileManager.getPostExchangeValues().clearPostValues();
		exchangeValuesFileManager.setPostValues(stackValueMap);
	}
	
	private Map<WrappedStack, EnergyValue> calculateStackValueMap(Map<WrappedStack, EnergyValue> stackValueMap)
	{
		LogManager.getLogger().info(ENERGY_VALUE_MARKER, "Beginning energy value calculation with {} values",
		                            new Object[]{stackValueMap.size()});
		long startingTime = System.nanoTime();
		Map<WrappedStack, EnergyValue> computedMap = new TreeMap<>(stackValueMap);
		Map<WrappedStack, EnergyValue> tempComputedMap = new TreeMap<>();
		int passNumber = 0;
		
		long passStartTime;
		label55:
		while ((passNumber == 0 || tempComputedMap.size() != computedMap.size()) && passNumber < 16)
		{
			passStartTime = System.nanoTime();
			++passNumber;
			computedMap.putAll(tempComputedMap);
			tempComputedMap = new TreeMap<>(computedMap);
			Iterator<WrappedStack> var9 = EquivalentExchange.getRecipeManager().getRecipeMappings().keySet().iterator();
			
			label53:
			while (true)
			{
				WrappedStack recipeOutput;
				WrappedStack unitWrappedStack;
				do
				{
					if (!var9.hasNext())
					{
						long passDuration = System.nanoTime() - passStartTime;
						if (Settings.energyValueDebugLoggingEnabled)
						{
							LogHelper.info(ENERGY_VALUE_MARKER,
							               "Pass {}: Calculated {} different values for objects in {} ms",
							               new Object[]{passNumber, tempComputedMap.size(), passDuration / 100000L});
						}
						continue label55;
					}
					
					recipeOutput = var9.next();
					unitWrappedStack = WrappedStack.wrap(recipeOutput, 1);
				}
				while (stackValueMap.containsKey(unitWrappedStack));
				
				Iterator<Set<WrappedStack>> var12 = EquivalentExchange.getRecipeManager().getRecipeMappings().get(
						recipeOutput).iterator();
				
				while (true)
				{
					while (true)
					{
						if (!var12.hasNext())
						{
							continue label53;
						}
						
						Set<WrappedStack> recipeInputs = var12.next();
						EnergyValue currentOutputValue = getEnergyValue(tempComputedMap, unitWrappedStack, false);
						
						EnergyValue computedOutputValue = computeFromInputs(tempComputedMap, recipeOutput,
						                                                    recipeInputs);
						
						if (computedOutputValue != null && computedOutputValue.compareTo(currentOutputValue) < 0)
						{
							this.uncomputedStacks.remove(unitWrappedStack);
							if (Settings.energyValueDebugLoggingEnabled)
							{
								LogManager.getLogger().info(ENERGY_VALUE_MARKER,
								                            "Pass {}: Calculated value {} for object {} with recipe inputs {} and output {}",
								                            new Object[]{passNumber, computedOutputValue, unitWrappedStack, recipeInputs, recipeOutput});
							}
							
							tempComputedMap.put(unitWrappedStack, computedOutputValue);
						}
						else if (computedOutputValue != null)
						{
							this.uncomputedStacks.add(unitWrappedStack);
						}
					}
				}
			}
		}
		
		passStartTime = System.nanoTime() - startingTime;
		LogManager.getLogger().info(ENERGY_VALUE_MARKER,
		                            "Finished energy value calculation - calculated {} new values for objects in {} ms",
		                            new Object[]{computedMap.size() - stackValueMap.size(), passStartTime / 100000L});
		return computedMap;
	}
	
	private EnergyValue computeFromInputs(
			Map<WrappedStack, EnergyValue> valueMap, WrappedStack wrappedOutput, Collection<WrappedStack> wrappedInputs)
	{
		float sumOfValues = 0f;
		
		for (WrappedStack wrappedInput : wrappedInputs)
		{
			EnergyValue inputValue = getEnergyValue(valueMap, wrappedInput, false);
			
			if (inputValue == null) return null;
			
			sumOfValues += inputValue.getValue() * wrappedInput.getStackSize();
		}
		
		return EnergyValue.factor(new EnergyValue(sumOfValues), wrappedOutput.getStackSize());
	}
	
	public void setEnergyValue(Object object, EnergyValue energyValue, Phase phase)
	{
		this.setEnergyValue(object, energyValue, phase, false);
	}
	
	public void setEnergyValue(Object object, EnergyValue energyValue, Phase phase, boolean doRegenValues)
	{
		if (WrappedStack.canBeWrapped(object) && energyValue != null && Float.compare(energyValue.getValue(), 0.0F) > 0)
		{
			WrappedStack wrappedStack = WrappedStack.wrap(object, 1);
			EnergyValue factoredEnergyValue = EnergyValue.factor(energyValue, wrappedStack.getStackSize());
			if (phase == Phase.PRE_CALCULATION)
			{
				if (!FMLJavaModLoadingContext.get().getModEventBus().post(
						new EnergyValueEvent.SetEnergyValueEvent(wrappedStack, factoredEnergyValue,
						                                         Phase.PRE_CALCULATION)))
				{
					this.preCalculationStackValueMap.put(wrappedStack, factoredEnergyValue);
					if (doRegenValues)
					{
						this.compute();
					}
					else
					{
						this.valuesNeedRegeneration = true;
					}
				}
			}
			else if (!FMLJavaModLoadingContext.get().getModEventBus().post(
					new EnergyValueEvent.SetEnergyValueEvent(wrappedStack, factoredEnergyValue,
					                                         Phase.POST_CALCULATION)))
			{
				TreeMap<WrappedStack, EnergyValue> valueMap = new TreeMap(this.stackValueMap);
				valueMap.put(wrappedStack, energyValue);
				ImmutableSortedMap.Builder<WrappedStack, EnergyValue> stackMappingsBuilder = ImmutableSortedMap
						                                                                             .naturalOrder();
				this.stackValueMap = stackMappingsBuilder.putAll(valueMap).build();
				this.postCalculationStackValueMap.put(wrappedStack, factoredEnergyValue);
			}
		}
		
	}
	
	public ImmutableSortedMap<WrappedStack, EnergyValue> getEnergyValues()
	{
		return exchangeValuesFileManager.getPostValues();
	}
	
	public Map<WrappedStack, EnergyValue> getPreCalculationStackValueMap()
	{
		return this.preCalculationStackValueMap;
	}
	
	public Map<WrappedStack, EnergyValue> getPostCalculationStackValueMap()
	{
		return this.postCalculationStackValueMap;
	}
	
	public boolean hasEnergyValue(Object object)
	{
		return this.hasEnergyValue(object, false);
	}
	
	public boolean hasEnergyValue(Object object, boolean strict)
	{
		return this.getEnergyValue(object, strict) != null;
	}
	
	public EnergyValue getEnergyValue(Object object)
	{
		return this.getEnergyValue(object, false);
	}
	
	public EnergyValue getEnergyValue(Object object, boolean strict)
	{
		return getEnergyValue(getEnergyValues(), object, strict);
	}
	
	public EnergyValue getEnergyValueForStack(Object object, boolean strict)
	{
		WrappedStack wrappedObject = WrappedStack.wrap(object);
		EnergyValue energyValue = this.getEnergyValue(object, strict);
		return wrappedObject != null && energyValue != null ? new EnergyValue(
				energyValue.getValue() * (float) wrappedObject.getStackSize()) : null;
	}
	
	public Set<ItemStack> getStacksInRange(Number start, Number finish)
	{
		return this.getStacksInRange(new EnergyValue(start), new EnergyValue(finish));
	}
	
	public Set<ItemStack> getStacksInRange(EnergyValue lowerBound, EnergyValue upperBound)
	{
		Set<ItemStack> filteredItemStacks = new TreeSet<>(ComparatorUtils.ENERGY_VALUE_ITEM_STACK_COMPARATOR);
		Set<ItemStack> greaterThanLowerBound = getStacksInRange(this.getEnergyValues(), lowerBound, false);
		Set<ItemStack> lesserThanUpperBound = getStacksInRange(this.getEnergyValues(), upperBound, true);
		if (!greaterThanLowerBound.isEmpty() && !lesserThanUpperBound.isEmpty())
		{
			for (ItemStack itemStack : greaterThanLowerBound)
			{
				if (lesserThanUpperBound.contains(itemStack))
				{
					filteredItemStacks.add(itemStack);
				}
			}
		}
		else
		{
			if (!greaterThanLowerBound.isEmpty())
			{
				return greaterThanLowerBound;
			}
			
			if (!lesserThanUpperBound.isEmpty())
			{
				return lesserThanUpperBound;
			}
		}
		
		return filteredItemStacks;
	}
	
	public void setPostValues(ImmutableSortedMap<WrappedStack, EnergyValue> values)
	{
		this.postCalculationStackValueMap = values;
	}
	
	public enum Phase
	{
		@Deprecated PRE_ASSIGNMENT, PRE_CALCULATION,
		/**
		 * @deprecated
		 */
		@Deprecated POST_ASSIGNMENT, POST_CALCULATION,
		/**
		 * @deprecated
		 */
		@Deprecated RUNTIME, ALL
	}
	
}
