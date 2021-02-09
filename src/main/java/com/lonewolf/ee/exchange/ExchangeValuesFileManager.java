package com.lonewolf.ee.exchange;

import com.google.common.collect.ImmutableSortedMap;
import com.lonewolf.ee.exchange.data.PostExchangeValues;
import com.lonewolf.ee.exchange.data.PreExchangeValues;
import com.lonewolf.ee.util.LogHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
public class ExchangeValuesFileManager
{
	private final PreExchangeValues preExchangeValues;
	private final PostExchangeValues postExchangeValues;
	
	public ExchangeValuesFileManager()
	{
		preExchangeValues = new PreExchangeValues();
		postExchangeValues = new PostExchangeValues();
	}
	
	public PostExchangeValues getPostExchangeValues()
	{
		return postExchangeValues;
	}
	
	public PreExchangeValues getPreExchangeValues()
	{
		return preExchangeValues;
	}
	
	public ImmutableSortedMap<WrappedStack, EnergyValue> getPostValues()
	{
		ImmutableSortedMap.Builder<WrappedStack, EnergyValue> values = ImmutableSortedMap.naturalOrder();
		
		for (CompoundNBT compound : postExchangeValues.getPostValues())
		{
			float energy = compound.getFloat("energyValue");
			WrappedStack stack = null;
			
			if (compound.contains("oreStack"))
			{
				CompoundNBT type = (CompoundNBT) compound.get("oreStack");
				stack = WrappedStack.wrap(new OreStack(type.getString("name")));
			}
			else if (compound.contains("fluidStack"))
			{
				CompoundNBT type = (CompoundNBT) compound.get("fluidStack");
				assert type != null;
				stack = WrappedStack.wrap(new FluidStack(Objects.requireNonNull(
						ForgeRegistries.FLUIDS.getValue(new ResourceLocation(type.getString("name")))), 1));
			}
			else if (compound.contains("itemStack"))
			{
				CompoundNBT type = (CompoundNBT) compound.get("itemStack");
				assert type != null;
				stack = WrappedStack.wrap(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(type.getString("name")))), 1);
			}
			else
			{
				LogHelper.info(compound.toString());
			}
			
			if (stack == null) continue;
			
			values.put(stack, new EnergyValue(energy));
		}
		
		
		return values.build();
	}
	
	public void setPostValues(Map<WrappedStack, EnergyValue> postCalculationStackValueMap)
	{
		for (Map.Entry<WrappedStack, EnergyValue> entry : postCalculationStackValueMap.entrySet())
		{
			WrappedStack wrappedStack = entry.getKey();
			EnergyValue energyValue = entry.getValue();
			
			CompoundNBT nbt = new CompoundNBT();
			CompoundNBT type = new CompoundNBT();
			nbt.putFloat("energyValue", energyValue.getValue());
//			LogHelper.info("Save: " + wrappedStack.toString());
			
			if (wrappedStack.getWrappedObject() instanceof ItemStack)
			{
				ItemStack stack = (ItemStack) wrappedStack.getWrappedObject();
				type.putString("name", stack.getItem().getRegistryName().toString());
				nbt.put("itemStack", type);
			}
			else if (wrappedStack.getWrappedObject() instanceof OreStack)
			{
				OreStack stack = (OreStack) wrappedStack.getWrappedObject();
				type.putString("name", stack.oreName);
				nbt.put("oreStack", type);
			}
			else if (wrappedStack.getWrappedObject() instanceof FluidStack)
			{
				FluidStack stack = (FluidStack) wrappedStack.getWrappedObject();
				type.putString("name", stack.getFluid().getRegistryName().toString());
				nbt.put("fluidStack", type);
			}
			else
			{
				continue;
			}
			
			postExchangeValues.addPostValue(nbt);
		}
	}
	
	public ImmutableSortedMap<WrappedStack, EnergyValue> getPreValues()
	{
		ImmutableSortedMap.Builder<WrappedStack, EnergyValue> values = ImmutableSortedMap.naturalOrder();
		
		for (CompoundNBT compound : preExchangeValues.getValues())
		{
			float energy = compound.getFloat("energyValue");
			WrappedStack stack = null;
			
			if (compound.contains("oreStack"))
			{
				CompoundNBT type = (CompoundNBT) compound.get("oreStack");
				stack = WrappedStack.wrap(new OreStack(type.getString("name")));
			}
			else if (compound.contains("fluidStack"))
			{
				CompoundNBT type = (CompoundNBT) compound.get("fluidStack");
				assert type != null;
				stack = WrappedStack.wrap(new FluidStack(Objects.requireNonNull(
						ForgeRegistries.FLUIDS.getValue(new ResourceLocation(type.getString("name")))), 1));
			}
			else
			{
				CompoundNBT type = (CompoundNBT) compound.get("itemStack");
				assert type != null;
				ResourceLocation itemName = new ResourceLocation(type.getString("name"));
				Item item = ForgeRegistries.ITEMS.getValue(itemName);
				
				if (item.getRegistryName().equals(new ResourceLocation("minecraft", "air"))) continue;
				
				stack = WrappedStack.wrap(new ItemStack(item), 1);
			}
			
			if (stack == null) continue;
			values.put(stack, new EnergyValue(energy));
		}
		
		
		return values.build();
	}
}
