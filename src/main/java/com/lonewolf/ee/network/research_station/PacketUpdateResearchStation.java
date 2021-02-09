package com.lonewolf.ee.network.research_station;

import com.lonewolf.ee.tile_entity.TileEntityResearchStation;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;

public class PacketUpdateResearchStation
{
	private ItemStack researchItem;
	private ItemStack alchenomiconItem;
	private BlockPos pos;
	private int researchTime;
	
	public PacketUpdateResearchStation(
			ItemStack researchItem, ItemStack alchenomiconItem, BlockPos pos, int researchTime)
	{
		this.researchItem = researchItem;
		this.alchenomiconItem = alchenomiconItem;
		this.pos = pos;
		this.researchTime = researchTime;
	}
	
	public PacketUpdateResearchStation(TileEntityResearchStation te)
	{
		this(te.getStackInSlot(0), te.getStackInSlot(1), te.getPos(), (int) te.learnTime);
	}
	
	public PacketUpdateResearchStation()
	{
	}
	
	public static void encode(PacketUpdateResearchStation msg, PacketBuffer buffer)
	{
		buffer.writeItemStack(msg.researchItem);
		buffer.writeItemStack(msg.alchenomiconItem);
		buffer.writeBlockPos(msg.pos);
		buffer.writeInt(msg.researchTime);
	}
	
	public static PacketUpdateResearchStation decode(PacketBuffer buffer)
	{
		ItemStack researchItem = buffer.readItemStack();
		ItemStack alchenomiconItem = buffer.readItemStack();
		BlockPos pos = buffer.readBlockPos();
		int learnTime = buffer.readInt();
		return new PacketUpdateResearchStation(researchItem,alchenomiconItem,pos,learnTime);
	}
	
	public static void handle(PacketUpdateResearchStation msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			assert Minecraft.getInstance().world != null;
			TileEntityResearchStation te = (TileEntityResearchStation) Minecraft.getInstance().world.getTileEntity(msg.pos);
			if (te != null)
			{
				te.setInventorySlotContents(0, msg.researchItem);
				te.setInventorySlotContents(1, msg.alchenomiconItem);
				te.learnTime = msg.researchTime;
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
