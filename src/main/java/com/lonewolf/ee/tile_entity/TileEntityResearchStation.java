package com.lonewolf.ee.tile_entity;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.inventory.ContainerResearchStation;
import com.lonewolf.ee.item.ItemAlchenomicon;
import com.lonewolf.ee.knowledge.PlayerKnowledgeManager;
import com.lonewolf.ee.network.ExchangeNetwork;
import com.lonewolf.ee.network.research_station.PacketUpdateRequestResearchStation;
import com.lonewolf.ee.network.research_station.PacketUpdateResearchStation;
import com.lonewolf.ee.registry.TileEntityRegistry;
import com.lonewolf.ee.util.ItemStackUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;

public class TileEntityResearchStation extends TileEntityEE implements IInventory, ITickableTileEntity
{
	public static final int inventorySize = 2;
	public static final int researchSlot = 0;
	public static final int AlchenomiconSlot = 1;
	public int learnTime;
	private PlayerKnowledgeManager manager;
	
	protected final IIntArray data = new IIntArray()
	{
		public int get(int index)
		{
			if (index == 0)
			{
				return TileEntityResearchStation.this.learnTime;
			}
			return 0;
		}
		
		public void set(int index, int value)
		{
			if (index == 0)
			{
				TileEntityResearchStation.this.learnTime = value;
			}
			
		}
		
		public int size()
		{
			return 2;
		}
	};
	public boolean isItemKnown;
	public ItemStackHandler inventory;
	
	public TileEntityResearchStation()
	{
		super(TileEntityRegistry.researchStationTileEntityType);
		inventory = new ItemStackHandler(inventorySize)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				assert world != null;
				if (!world.isRemote())
				{
					ExchangeNetwork.simpleChannel.send(PacketDistributor.NEAR
							                                   .with(() -> new PacketDistributor.TargetPoint(pos.getX(),
							                                                                                 pos.getY(),
							                                                                                 pos.getZ(),
							                                                                                 64,
							                                                                                 world.getDimensionKey())),
					                                   new PacketUpdateResearchStation(TileEntityResearchStation.this));
					isItemKnown = isItemStackKnown();
					TileEntityResearchStation.this.markDirty();
				}
			}
		};
		
		manager = EquivalentExchange.getPlayerKnowledgeManager();
	}
	
	@Override
	public void onLoad()
	{
		assert world != null;
		if (world.isRemote())
		{
			ExchangeNetwork.sendPacketToSever(new PacketUpdateRequestResearchStation(this));
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventorySize;
	}
	
	@Override
	public boolean isEmpty()
	{
		return false;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		ItemStack itemStack = this.getStackInSlot(index);
		if (itemStack != null)
		{
			if (itemStack.getCount() <= count)
			{
				this.setInventorySlotContents(index, ItemStack.EMPTY);
			}
			else
			{
				ItemStack oldStack = itemStack.copy();
				oldStack.setCount(itemStack.getCount() - count);
				itemStack.setCount(count);
				setInventorySlotContents(index, oldStack);
			}
		}
		
		return itemStack;
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		ItemStack stack = getStackInSlot(index);
		setInventorySlotContents(index, ItemStack.EMPTY);
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory.setStackInSlot(index, stack);
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player)
	{
		return true;
	}
	
	@Override
	public void clear()
	{
	
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (EquivalentExchange.getExchangeBlackList().isItemBlacklisted(stack))
			return false;

		if (stack.isDamaged())
			return false;
		
		if (getStackInSlot(index).getCount() >= 1)
			return false;
		
		switch (index)
		{
			case 0:
				return getStackInSlot(AlchenomiconSlot).getItem() instanceof ItemAlchenomicon;
			case 1:
				return stack.getItem() instanceof ItemAlchenomicon;
		}
		
		return true;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
	public void tick()
	{
		if (!world.isRemote())
			research();
	}
	
	public void research()
	{
		if (getStackInSlot(AlchenomiconSlot) != ItemStack.EMPTY && getStackInSlot(researchSlot) != ItemStack.EMPTY)
		{
			if (this.canLearnItemStack())
			{
				++this.learnTime;
				if (this.learnTime == maxLearnTime())
				{
					this.learnTime = 0;
					this.learnItemStack();
				}
				this.markDirty();
			}
			else
			{
				this.learnTime = 0;
			}
			
			this.isItemKnown = this.isItemStackKnown();
		}
		else
		{
			this.learnTime = 0;
		}
	}
	
	private boolean canLearnItemStack()
	{
		ItemStack alchenomicon = getStackInSlot(AlchenomiconSlot);
		String playerName = ItemStackUtils.getOwnerName(alchenomicon);
		return alchenomicon != null && playerName != null && manager.canPlayerLearn(playerName,
		                                                                                                    getStackInSlot(
				                                                                                                    researchSlot));
	}
	
	private boolean isItemStackKnown()
	{
		ItemStack alchenomicon = getStackInSlot(AlchenomiconSlot);
		String playerName = ItemStackUtils.getOwnerName(alchenomicon);
		return alchenomicon != null && playerName != null && manager.doesPlayerKnow(playerName,
		                                                                                                    getStackInSlot(
				                                                                                                    researchSlot));
	}
	
	private void learnItemStack()
	{
		if (this.canLearnItemStack())
		{
			manager.teachPlayer(ItemStackUtils.getOwnerName(getStackInSlot(AlchenomiconSlot)),
			                                            getStackInSlot(researchSlot));
			this.setInventorySlotContents(0, ItemStack.EMPTY);
			this.markDirty();
		}
	}
	
	public Container createMenu(int id, PlayerInventory player)
	{
		ExchangeNetwork.sendPacketToSever(new PacketUpdateRequestResearchStation(this));
		return new ContainerResearchStation(id, player, this, data);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		inventory.deserializeNBT(nbt.getCompound("inventory"));
		learnTime = nbt.getInt("learnTime");
		super.read(state, nbt);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.put("inventory", inventory.serializeNBT());
		compound.putInt("learnTime", learnTime);
		
		return super.write(compound);
	}
	
	public int maxLearnTime()
	{
		return 200;
	}
	
	public ArrayList<ItemStack> getAllItems()
	{
		ArrayList<ItemStack> items = new ArrayList<>();
		
		for (int i = 0; i < inventory.getSlots(); i++)
		{
			items.add(inventory.getStackInSlot(i));
		}
		
		return items;
	}
}
