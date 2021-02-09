package com.lonewolf.ee.event;

import com.lonewolf.ee.exchange.EnergyValue;
import com.lonewolf.ee.exchange.EnergyValueManager;
import net.minecraftforge.eventbus.api.Event;

public class EnergyValueEvent extends Event
{
	public final Object object;
	public final EnergyValueManager.Phase phase;
	
	public EnergyValueEvent(Object object, EnergyValueManager.Phase phase) {
		this.object = object;
		this.phase = phase;
	}
	
	public boolean isCancelable() {
		return true;
	}
	
	public static class RemoveEnergyValueEvent extends EnergyValueEvent
	{
		public RemoveEnergyValueEvent(Object object, EnergyValueManager.Phase phase)
		{
			super(object, phase);
		}
	}
	
	public static class SetEnergyValueEvent extends EnergyValueEvent {
		public final EnergyValue newEnergyValue;
		
		public SetEnergyValueEvent(Object object, EnergyValue newEnergyValue, EnergyValueManager.Phase phase) {
			super(object, phase);
			this.newEnergyValue = newEnergyValue;
		}
	}
}
