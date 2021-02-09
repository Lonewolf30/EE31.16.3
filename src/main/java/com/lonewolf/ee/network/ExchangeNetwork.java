package com.lonewolf.ee.network;

import com.lonewolf.ee.network.alchemy_array_controller.chalk_settings.ArrayChalkSettingsPacketUpdate;
import com.lonewolf.ee.network.alchemy_array_controller.chalk_settings.ArrayChalkSettingsRequestPacket;
import com.lonewolf.ee.network.exchange.ExchangePacketUpdate;
import com.lonewolf.ee.network.exchange.ExchangeUpdateRequestPacket;
import com.lonewolf.ee.network.research_station.PacketUpdateRequestResearchStation;
import com.lonewolf.ee.network.research_station.PacketUpdateResearchStation;
import com.lonewolf.ee.reference.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ExchangeNetwork
{
	private static final String protocolVersion = "0";
	public static SimpleChannel simpleChannel;
	
	public static void init()
	{
		simpleChannel = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.mod_id, "network"),
		                                                 () -> protocolVersion, protocolVersion::equals,
		                                                 protocolVersion::equals);
	}
	
	public static void registerPackets()
	{
		simpleChannel.registerMessage(0, PacketUpdateResearchStation.class, PacketUpdateResearchStation::encode,
		                              PacketUpdateResearchStation::decode, PacketUpdateResearchStation::handle);
		simpleChannel.registerMessage(1, PacketUpdateRequestResearchStation.class,
		                              PacketUpdateRequestResearchStation::encode,
		                              PacketUpdateRequestResearchStation::decode,
		                              PacketUpdateRequestResearchStation::handle);
		
		simpleChannel.registerMessage(2, ExchangePacketUpdate.class, ExchangePacketUpdate::encode,
		                              ExchangePacketUpdate::decode, ExchangePacketUpdate::handle);
		simpleChannel.registerMessage(3, ExchangeUpdateRequestPacket.class, ExchangeUpdateRequestPacket::encode,
		                              ExchangeUpdateRequestPacket::decode, ExchangeUpdateRequestPacket::handle);

		simpleChannel.registerMessage(4, ArrayChalkSettingsPacketUpdate.class, ArrayChalkSettingsPacketUpdate::encode,
				ArrayChalkSettingsPacketUpdate::decode, ArrayChalkSettingsPacketUpdate::handle);
		simpleChannel.registerMessage(5, ArrayChalkSettingsRequestPacket.class, ArrayChalkSettingsRequestPacket::encode,
				ArrayChalkSettingsRequestPacket::decode, ArrayChalkSettingsRequestPacket::handle);
	}
	
	public static void sendPacketToSever(Object object)
	{
		simpleChannel.sendToServer(object);
	}
}
