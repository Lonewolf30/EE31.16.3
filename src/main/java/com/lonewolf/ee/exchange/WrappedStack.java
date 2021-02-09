package com.lonewolf.ee.exchange;

import com.lonewolf.ee.util.FluidHelper;
import com.lonewolf.ee.util.ItemStackUtils;
import com.lonewolf.ee.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WrappedStack implements Comparable<WrappedStack>
{
	private final Object wrappedStack;
	private int stackSize;
	public static final Comparator<WrappedStack> COMPARATOR = (wrappedStack1, wrappedStack2) ->
	{
		int compareResult;
		if (wrappedStack1.wrappedStack instanceof ItemStack) {
			if (wrappedStack2.wrappedStack instanceof ItemStack) {
				compareResult = ItemStackUtils.compare((ItemStack)wrappedStack1.wrappedStack, (ItemStack)wrappedStack2.wrappedStack);
				return compareResult == 0 ? wrappedStack1.stackSize - wrappedStack2.stackSize : compareResult;
			} else {
				return 1;
			}
		} else if (wrappedStack1.wrappedStack instanceof OreStack) {
			if (wrappedStack2.wrappedStack instanceof ItemStack) {
				return -1;
			} else if (wrappedStack2.wrappedStack instanceof OreStack) {
				compareResult = OreStack.compare((OreStack)wrappedStack1.wrappedStack, (OreStack)wrappedStack2.wrappedStack);
				return compareResult == 0 ? wrappedStack1.stackSize - wrappedStack2.stackSize : compareResult;
			} else {
				return 1;
			}
		} else if (wrappedStack1.wrappedStack instanceof FluidStack) {
			if (!(wrappedStack2.wrappedStack instanceof ItemStack) && !(wrappedStack2.wrappedStack instanceof OreStack)) {
				if (wrappedStack2.wrappedStack instanceof FluidStack) {
					compareResult = FluidHelper.compare((FluidStack)wrappedStack1.wrappedStack, (FluidStack)wrappedStack2.wrappedStack);
					return compareResult == 0 ? wrappedStack1.stackSize - wrappedStack2.stackSize : compareResult;
				} else {
					return 1;
				}
			} else {
				return -1;
			}
		} else if (wrappedStack1.wrappedStack == null) {
			return wrappedStack2.wrappedStack != null ? -1 : 0;
		} else {
			return 0;
		}
	};
	
	public WrappedStack() {
		this.stackSize = -1;
		this.wrappedStack = null;
	}
	
	private WrappedStack(Object object) {
		if (object instanceof Item) {
			object = new ItemStack((Item)object);
		} else if (object instanceof Block) {
			object = new ItemStack((Block)object);
		} else if (object instanceof Fluid) {
			object = new FluidStack((Fluid)object, 1000);
		}
		
		if (object instanceof ItemStack) {
			if (!((ItemStack)object).hasTag()) {
				this.stackSize = ((ItemStack)object).getCount();
				this.wrappedStack = ItemStackUtils.clone((ItemStack)object, 1);
			} else {
				this.stackSize = -1;
				this.wrappedStack = null;
			}
		} else if (object instanceof OreStack) {
			OreStack oreStack = new OreStack((OreStack)object);
			this.stackSize = oreStack.stackSize;
			oreStack.stackSize = 1;
			this.wrappedStack = oreStack;
		} else if (object instanceof ArrayList) {
			ArrayList<?> objectList = (ArrayList)object;
			OreStack possibleOreStack = OreStack.getOreStackFrom(objectList);
			if (possibleOreStack != null) {
				this.stackSize = possibleOreStack.stackSize;
				possibleOreStack.stackSize = 1;
				this.wrappedStack = possibleOreStack;
			} else {
				this.stackSize = -1;
				this.wrappedStack = null;
			}
		} else if (object instanceof FluidStack) {
			FluidStack fluidStack = ((FluidStack)object).copy();
			this.stackSize = fluidStack.getAmount();
			fluidStack.setAmount(1);
			this.wrappedStack = fluidStack;
		} else if (object instanceof WrappedStack) {
			WrappedStack wrappedStackObject = (WrappedStack)object;
			if (wrappedStackObject.getWrappedObject() != null) {
				this.stackSize = wrappedStackObject.stackSize;
				this.wrappedStack = wrappedStackObject.wrappedStack;
			} else {
				this.stackSize = -1;
				this.wrappedStack = null;
			}
		} else {
			this.stackSize = -1;
			this.wrappedStack = null;
		}
		
	}
	
	private WrappedStack(Object object, int stackSize) {
		if (object instanceof Item) {
			object = new ItemStack((Item)object);
		} else if (object instanceof Block) {
			object = new ItemStack((Block)object);
		} else if (object instanceof Fluid) {
			object = new FluidStack((Fluid)object, 1000);
		}
		
		if (object instanceof ItemStack) {
			this.stackSize = stackSize;
			this.wrappedStack = ItemStackUtils.clone((ItemStack)object, 1);
		} else {
			OreStack possibleOreStack;
			if (object instanceof OreStack) {
				possibleOreStack = new OreStack((OreStack)object);
				this.stackSize = stackSize;
				possibleOreStack.stackSize = 1;
				this.wrappedStack = possibleOreStack;
			} else if (object instanceof ArrayList) {
				possibleOreStack = OreStack.getOreStackFrom((ArrayList)object);
				if (possibleOreStack != null) {
					this.stackSize = stackSize;
					possibleOreStack.stackSize = 1;
					this.wrappedStack = possibleOreStack;
				} else {
					this.stackSize = -1;
					this.wrappedStack = null;
				}
			} else if (object instanceof FluidStack) {
				FluidStack fluidStack = ((FluidStack)object).copy();
				this.stackSize = stackSize;
				if (!fluidStack.isEmpty())
					fluidStack.setAmount(1);
				this.wrappedStack = fluidStack;
			} else if (object instanceof WrappedStack) {
				WrappedStack wrappedStackObject = (WrappedStack)object;
				if (wrappedStackObject.getWrappedObject() != null) {
					this.stackSize = stackSize;
					this.wrappedStack = wrappedStackObject.wrappedStack;
				} else {
					this.stackSize = -1;
					this.wrappedStack = null;
				}
			} else {
				this.stackSize = -1;
				this.wrappedStack = null;
			}
		}
		
	}
	
	public Object getWrappedObject() {
		return this.wrappedStack;
	}
	
	public static boolean canBeWrapped(Object object)
	{
		if (object instanceof WrappedStack) {
			return true;
		} else if (!(object instanceof Item) && !(object instanceof Block)) {
			if (object instanceof ItemStack) {
				return true;
			} else {
				if (object instanceof OreStack) {
					return true;
				}
				
				if (object instanceof List) {
					if (OreStack.getOreStackFrom((List)object) != null) {
						return true;
					}
				} else if (object instanceof Fluid || object instanceof FluidStack) {
					return true;
				}
			}
			
			return false;
		} else {
			return true;
		}
	}
	
	public int getStackSize() {
		return this.stackSize;
	}
	
	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}
	
	public static WrappedStack wrap(Object object) {
		return canBeWrapped(object) ? new WrappedStack(object) : null;
	}
	
	public static WrappedStack wrap(Object object, int stackSize) {
		return canBeWrapped(object) ? new WrappedStack(object, stackSize) : null;
	}
	
	public boolean equals(Object object) {
		return object instanceof WrappedStack && this.compareTo((WrappedStack)object) == 0;
	}
	
	public int compareTo(WrappedStack wrappedStack) {
		return COMPARATOR.compare(this, wrappedStack);
	}
	
	public String toString() {
		
		if (this.wrappedStack instanceof ItemStack) {
			ItemStack itemStack = (ItemStack)this.wrappedStack;
			String unlocalizedName = null;
			
			try {
				if (itemStack.getTag() != null) {
					unlocalizedName = ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString();
				}
				
				if (unlocalizedName == null) {
					unlocalizedName = itemStack.getDisplayName().toString();
				}
			} catch (ArrayIndexOutOfBoundsException var4) {
				unlocalizedName = "no-name";
			}
			
			return itemStack.hasTag() ? String.format("%sxitemStack[%s@%s:%s]", this.stackSize, unlocalizedName, itemStack.getCount(), itemStack.getTag()) : String.format("%sxitemStack[%s@%s]", this.stackSize, unlocalizedName, itemStack.getCount());
		} else if (this.wrappedStack instanceof OreStack) {
			OreStack oreStack = (OreStack)this.wrappedStack;
			return String.format("%sxoreStack[%s]", this.stackSize, oreStack.oreName);
		} else if (this.wrappedStack instanceof FluidStack) {
			FluidStack fluidStack = (FluidStack)this.wrappedStack;
			return String.format("%sxfluidStack[%s]", this.stackSize, fluidStack.getFluid().getRegistryName());
		} else {
			return "null-wrappedstack";
		}
	}
	
	public static WrappedStack fromCompound(CompoundNBT compoundNBT)
	{
		if (compoundNBT == null)
			return null;
		
		int stackSize = compoundNBT.getInt("amount");
		
		switch (compoundNBT.getString("type"))
		{
			case "itemStack": {
				ItemStack itemStack = new ItemStack(null);
				itemStack.deserializeNBT(compoundNBT.getCompound("value"));
				return WrappedStack.wrap(itemStack, stackSize);
			}
			case "fluidStack": {
				FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(compoundNBT.getCompound("value"));
				return WrappedStack.wrap(fluidStack, stackSize);
			}
			
			case "oreStack" : {
				OreStack oreStack = OreStack.loadOreStackFromNBT(compoundNBT.getCompound("value"));
				return WrappedStack.wrap(oreStack, stackSize);
			}
		}
		
		return null;
	}
	
	public CompoundNBT toCompound()
	{
		CompoundNBT nbt = new CompoundNBT();
		CompoundNBT wrapped = new CompoundNBT();
		if (this.wrappedStack instanceof ItemStack)
		{
			wrapped = ((ItemStack) this.wrappedStack).write(new CompoundNBT());
			nbt.putString("type", "itemStack");
		}
		else if(this.wrappedStack instanceof OreStack)
		{
			wrapped = ((OreStack) this.wrappedStack).writeToNBT(new CompoundNBT());
			nbt.putString("type", "oreStack");
		}
		else if (this.wrappedStack instanceof FluidStack)
		{
			wrapped = ((FluidStack) this.wrappedStack).writeToNBT(new CompoundNBT());
			nbt.putString("type", "fluidStack");
		}
		
		nbt.put("value", wrapped);
		nbt.putInt("amount", stackSize);
		
		return nbt;
	}
}
