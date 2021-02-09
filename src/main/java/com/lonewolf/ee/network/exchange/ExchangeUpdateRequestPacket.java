package com.lonewolf.ee.network.exchange;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.network.ExchangeNetwork;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class ExchangeUpdateRequestPacket
{
	public ExchangeUpdateRequestPacket()
	{
	}
	
	public static void encode(ExchangeUpdateRequestPacket msg, PacketBuffer buffer)
	{
	}
	
	
	public static ExchangeUpdateRequestPacket decode(PacketBuffer buffer)
	{
		return new ExchangeUpdateRequestPacket();
	}
	
	public static void handle(ExchangeUpdateRequestPacket msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		                      {
			
			                      ExchangeNetwork.simpleChannel.send(PacketDistributor.PLAYER.noArg(),
			                                                         new ExchangePacketUpdate(
					                                                         EquivalentExchange.getEnergyValueManager()
					                                                                           .getEnergyValues()));
		                      });
		
		ctx.get().setPacketHandled(true);
	}
}
