package com.lonewolf.ee.exchange.data;

import com.lonewolf.ee.configuration.ConfigurationManager;
import com.lonewolf.ee.reference.DefaultPreEMCValues;
import com.lonewolf.ee.util.LogHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;

public class PreExchangeValues extends WorldSavedData
{
	private ArrayList<CompoundNBT> prevalues;
	private final Marker preValuesMarker = MarkerManager.getMarker("PreValues");

	//TODO: Move into common config file.
	public PreExchangeValues()
	{
		super("ee_prevalues");
		this.markDirty();
	}
	
	public ArrayList<CompoundNBT> getValues()
	{
		if (prevalues == null)
			return DefaultPreEMCValues.preEMCValues;
		
		return prevalues;
	}
	
	@Override
	public void read(CompoundNBT nbt)
	{
		LogHelper.info(preValuesMarker, "Pre EMC Read");
		
		ListNBT listNBT = (ListNBT) nbt.get("values");
		prevalues = new ArrayList<>();
		
		for (INBT inbt : listNBT)
		{
			prevalues.add((CompoundNBT) inbt);
		}
		
		if (ConfigurationManager.SERVER.reloadDefaultEMC.get())
		{
			prevalues = DefaultPreEMCValues.preEMCValues;
			ConfigurationManager.SERVER.reloadDefaultEMC.set(false);
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		ListNBT listNBT = new ListNBT();
		
		if (prevalues == null)
			prevalues = DefaultPreEMCValues.preEMCValues;
		
		listNBT.addAll(prevalues);
		
		compound.put("values", listNBT);
		
		LogHelper.info(preValuesMarker, "Pre EMC Values Write");
		
		return compound;
	}

	public void reload()
	{
		prevalues = DefaultPreEMCValues.preEMCValues;
		this.markDirty();
	}
}
