package com.lonewolf.ee.exchange.data;

import com.lonewolf.ee.util.LogHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;

public class PostExchangeValues extends WorldSavedData
{
	private ArrayList<CompoundNBT> postValues;
	private final Marker postValuesMarker = MarkerManager.getMarker("PostValues");
	
	public PostExchangeValues()
	{
		super("ee_postvalues");
		postValues = new ArrayList<>();
	}
	
	public ArrayList<CompoundNBT> getPostValues()
	{
		return postValues;
	}
	
	public void setPostValues(ArrayList<CompoundNBT> postValues)
	{
		this.postValues = postValues;
		this.markDirty();
	}
	
	public void clearPostValues()
	{
		postValues.clear();
		markDirty();
	}
	
	public void addPostValue(CompoundNBT compoundNBT)
	{
		postValues.add(compoundNBT);
		markDirty();
	}
	
	@Override
	public void read(CompoundNBT nbt)
	{
		ListNBT listNBT = (ListNBT) nbt.get("values");
		postValues = new ArrayList<>();
		
		assert listNBT != null;
		for (INBT inbt : listNBT)
		{
			postValues.add((CompoundNBT) inbt);
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		ListNBT listNBT = new ListNBT();
		
		listNBT.addAll(postValues);
		
		compound.put("values", listNBT);
		
		LogHelper.info(postValuesMarker, "Post EMC Values Write");
		
//		return new CompoundNBT();
		return compound;
	}
}
