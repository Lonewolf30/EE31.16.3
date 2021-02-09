package com.lonewolf.ee.util;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.rmi.registry.Registry;
import java.util.Comparator;

public class FluidHelper
{
	public static final Comparator<FluidStack> COMPARATOR = new Comparator<FluidStack>() {
		public int compare(FluidStack fluidStack1, FluidStack fluidStack2) {
			if (fluidStack1 != null && fluidStack2 != null) {
				if (fluidStack1.getFluid() != null && fluidStack2.getFluid() != null) {
					if (fluidStack1.getFluid().getRegistryName() != null && fluidStack2.getFluid().getRegistryName() != null) {
						if (fluidStack1.getFluid().getRegistryName().toString().equalsIgnoreCase(fluidStack2.getFluid().getRegistryName().toString())) {
							if (fluidStack1.getAmount() == fluidStack2.getAmount()) {
								if (fluidStack1.getTag() != null && fluidStack2.getTag() != null) {
									return fluidStack1.getTag().hashCode() - fluidStack2.getTag().hashCode();
								} else if (fluidStack1.getTag() != null) {
									return -1;
								} else {
									return fluidStack2.getTag() != null ? 1 : 0;
								}
							} else {
								return fluidStack1.getAmount() - fluidStack2.getAmount();
							}
						} else {
							return fluidStack1.getFluid().getRegistryName().toString().compareToIgnoreCase(fluidStack2.getFluid().getRegistryName().toString());
						}
					} else if (fluidStack1.getFluid().getRegistryName() != null) {
						return -1;
					} else {
						return fluidStack2.getFluid().getRegistryName() != null ? 1 : 0;
					}
				} else if (fluidStack1.getFluid() != null) {
					return -1;
				} else {
					return fluidStack2.getFluid() != null ? 1 : 0;
				}
			} else if (fluidStack1 != null) {
				return -1;
			} else {
				return fluidStack2 != null ? 1 : 0;
			}
		}
	};
	
	public FluidHelper() {
	}
	
	public static void registerFluids() {
//
//		if (!FluidRegistry.isFluidRegistered("milk")) {
//			Fluid milk = (new Fluid("milk")).setUnlocalizedName("ee3.milk");
//			if (FluidRegistry.registerFluid(milk)) {
//				FluidContainerRegistry.registerFluidContainer(new FluidStack(milk, 1000), new ItemStack(Items.field_151117_aB), new ItemStack(Items.field_151133_ar));
//			}
//		}
//
	}
	
	public static int compare(FluidStack fluidStack1, FluidStack fluidStack2) {
		return COMPARATOR.compare(fluidStack1, fluidStack2);
	}
	
	public static String toString(FluidStack fluidStack) {
		return fluidStack != null ? String.format("%sxfluidStack.%s", fluidStack.getAmount(), fluidStack.getFluid().getRegistryName()) : "fluidStack[null]";
	}
}
