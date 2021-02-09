package com.lonewolf.ee.exchange;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class EnergyValue implements Comparable<EnergyValue>
{
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###,###,###,###.###");
	private float energyValue;
	
	public EnergyValue()
	{
		this(0);
	}
	
	public EnergyValue(Number energyValue)
	{
		this.energyValue = energyValue.floatValue();
	}
	
	public static CompoundNBT writeEnergyValueToNBT(EnergyValue energyValue)
	{
		CompoundNBT nbtTagCompound = new CompoundNBT();
		energyValue.writeToNBT(nbtTagCompound);
		return nbtTagCompound;
	}
	
	public static EnergyValue loadEnergyValueFromNBT(CompoundNBT nbtTagCompound)
	{
		if (nbtTagCompound.contains("energyValue"))
		{
			float energyValue = nbtTagCompound.getFloat("energyValue");
			return new EnergyValue(energyValue);
		}
		else
		{
			return null;
		}
	}
	
	public static EnergyValue factor(EnergyValue energyValue, Number factor)
	{
		return Float.compare(factor.floatValue(), 0.0F) != 0 && energyValue != null ? new EnergyValue(
				(BigDecimal.valueOf(energyValue.getValue() * 1.0F / factor.floatValue())).setScale(3, 6)
				                                                                         .floatValue()) : null;
	}
	
	public float getValue()
	{
		if ((this.energyValue % 1) > 0.99)
			return (float) Math.ceil(this.energyValue);
		return this.energyValue;
	}
	
	public ITextComponent getChatComponent()
	{
		return new StringTextComponent("" + this.getValue());
	}
	
	public String toString()
	{
		return DECIMAL_FORMAT.format(this.energyValue);
	}
	
	public boolean equals(Object object)
	{
		return object instanceof EnergyValue && this.compareTo((EnergyValue) object) == 0;
	}
	
	public int compareTo(EnergyValue energyValue)
	{
		return energyValue != null ? Float.compare(this.energyValue, energyValue.getValue()) : -1;
	}
	
	public CompoundNBT writeToNBT(CompoundNBT nbtTagCompound)
	{
		nbtTagCompound.putFloat("energyValue", this.energyValue);
		return nbtTagCompound;
	}
	
	public void readFromNBT(CompoundNBT nbtTagCompound)
	{
		if (nbtTagCompound.contains("energyValue"))
		{
			this.energyValue = nbtTagCompound.getFloat("energyValue");
		}
		
	}
}
