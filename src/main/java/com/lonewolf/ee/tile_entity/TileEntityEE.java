package com.lonewolf.ee.tile_entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.UsernameCache;

import java.util.UUID;

public class TileEntityEE extends TileEntity
{
	protected Direction direction;
	protected String customName;
	protected UUID owner;
	
	public TileEntityEE(TileEntityType<?> tileEntityTypeIn)
	{
		super(tileEntityTypeIn);
		this.direction = Direction.NORTH;
		this.customName = "";
		this.owner = null;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}
	
	public String getCustomName()
	{
		return customName;
	}
	
	public void setCustomName(String customName)
	{
		this.customName = customName;
	}
	
	public String GetOwnerName()
	{
		return this.owner != null ? UsernameCache.getLastKnownUsername(owner) : "Unknown";
	}
	
	public UUID getOwner()
	{
		return owner;
	}
	
	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}
	
	public void setOwner(PlayerEntity entity)
	{
		this.owner = entity.getUniqueID();
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {

		direction = Direction.byIndex(nbt.getInt("direction"));
		customName = nbt.getString("customName");

		owner = new UUID(nbt.getLong("ownerUpperBits"), nbt.getLong("ownerLowerBits"));

		super.read(state, nbt);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putInt("direction", direction.getIndex());
		compound.putString("customName", customName);
		
		compound.putLong("ownerLowerBits", owner.getLeastSignificantBits());
		compound.putLong("ownerUpperBits", owner.getMostSignificantBits());
		
		return super.write(compound);
	}
}
