package com.lonewolf.ee.exchange;

import com.lonewolf.ee.exchange.EnergyValue;
import net.minecraft.item.ItemStack;

public interface IEnergyValueProvider
{
	EnergyValue getEnergyValue(ItemStack stack);
}
