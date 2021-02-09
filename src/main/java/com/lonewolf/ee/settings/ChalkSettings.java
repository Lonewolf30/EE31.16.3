package com.lonewolf.ee.settings;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.configuration.ConfigurationManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class ChalkSettings
{
	private ResourceLocation index;
	private int size;
	private int rotation;
	
	public ChalkSettings()
	{
		this((ResourceLocation) EquivalentExchange.getArrayManager().getKeys().toArray()[0], 1, 0);
	}
	
	public ChalkSettings(ResourceLocation index, int size, int rotation)
	{
		this.index = index;
		this.size = size;
		this.rotation = rotation;
	}
	
	public ResourceLocation getIndex()
	{
		return this.index;
	}
	
	public void setIndex(ResourceLocation index)
	{
		this.index = index;
	}
	
	public int getSize()
	{
		return this.size;
	}
	
	public void setSize(int size)
	{
		if (size < 1)
		{
			this.size = 1;
		}
		else if (size > ConfigurationManager.SERVER.chalkMaxSize.get())
		{
			this.size = ConfigurationManager.SERVER.chalkMaxSize.get();
		}
		else
		{
			this.size = size;
		}
		
	}
	
	public void incrementSize()
	{
		this.setSize(size+= 2);
	}
	
	public void decrementSize()
	{
		this.setSize(size-= 2);
	}
	
	public int getRotation()
	{
		return this.rotation;
	}
	
	public void setRotation(int rotation)
	{
		if (rotation < 0)
		{
			this.rotation = 0;
		}
		else
		{
			this.rotation = rotation % 4;
		}
		
	}
	
	public void rotateClockwise()
	{
		this.rotation = (this.rotation + 1) % 4;
	}
	
	public void rotateCounterClockwise()
	{
		--this.rotation;
		if (this.rotation < 0)
		{
			this.rotation = 3;
		}
		
	}
	
	public void readFromNBT(CompoundNBT nbtTagCompound)
	{
		if (nbtTagCompound != null && nbtTagCompound.contains("chalk_settings") && nbtTagCompound.getCompound(
				"chalk_settings").size() == 10)
		{
			CompoundNBT chalkSettings = nbtTagCompound.getCompound("chalk_settings");
			if (chalkSettings.contains("index"))
			{
				String resource = chalkSettings.getString("index");
				this.index = new ResourceLocation(resource);
			}
			else
			{
				this.index = (ResourceLocation) EquivalentExchange.getArrayManager().getKeys().toArray()[0];
			}
			
			if (chalkSettings.contains("size"))
			{
				this.size = chalkSettings.getInt("size");
				if (this.size < 1)
				{
					this.size = 1;
				}
				else if (this.size > ConfigurationManager.SERVER.chalkMaxSize.get())
				{
					this.size = ConfigurationManager.SERVER.chalkMaxSize.get();
				}
			}
			else
			{
				this.size = 1;
			}
			
			if (chalkSettings.contains("rotation"))
			{
				this.rotation = chalkSettings.getInt("rotation");
				if (this.rotation < 0)
				{
					this.rotation = 0;
				}
				else
				{
					this.rotation %= 4;
				}
			}
			else
			{
				this.rotation = 0;
			}
		}
		else
		{
			this.index = (ResourceLocation) EquivalentExchange.getArrayManager().getKeys().toArray()[0];
			this.size = 1;
			this.rotation = 0;
		}
		
	}
	
	public CompoundNBT writeToNBT(CompoundNBT nbtTagCompound)
	{
		CompoundNBT chalkSettings = new CompoundNBT();
		chalkSettings.putString("index", this.index.toString());
		chalkSettings.putInt("size", this.size);
		chalkSettings.putInt("rotation", this.rotation);
		nbtTagCompound.put("chalk_settings", chalkSettings);
		return nbtTagCompound;
	}
}
