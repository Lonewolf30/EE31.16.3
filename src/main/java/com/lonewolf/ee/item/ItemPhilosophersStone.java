package com.lonewolf.ee.item;

import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.extensions.IForgeKeybinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class ItemPhilosophersStone extends BaseItem implements IForgeKeybinding
{
	private final int max_charge;
	
	public ItemPhilosophersStone()
	{
		super("stone_philosophers", new Properties().maxStackSize(5).setNoRepair().maxDamage(3));
		this.max_charge = 3;
	}
	
	@Override
	public InputMappings.Input getKey()
	{
		return null;
	}
	
	@Override
	public IKeyConflictContext getKeyConflictContext()
	{
		return null;
	}
	
	@Override
	public void setKeyConflictContext(IKeyConflictContext keyConflictContext)
	{
	
	}
	
	@Override
	public KeyModifier getKeyModifierDefault()
	{
		return null;
	}
	
	@Override
	public KeyModifier getKeyModifier()
	{
		return null;
	}
	
	@Override
	public void setKeyModifierAndCode(KeyModifier keyModifier, InputMappings.Input keyCode)
	{
	
	}
}
