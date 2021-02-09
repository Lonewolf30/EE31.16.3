package com.lonewolf.ee.network.exchange;

import com.google.common.collect.ImmutableSortedMap;
import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.exchange.EnergyValue;
import com.lonewolf.ee.exchange.WrappedStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ExchangePacketUpdate
{
	private ImmutableSortedMap<WrappedStack, EnergyValue> values;
	
	public ExchangePacketUpdate(
			ImmutableSortedMap<WrappedStack, EnergyValue> values)
	{
		this.values = values;
	}
	
	public ExchangePacketUpdate()
	{
	}
	
	public static void encode(ExchangePacketUpdate msg, PacketBuffer buffer)
	{
		buffer.writeInt(msg.values.size());
		msg.values.forEach((wrappedStack, energyValue) -> {
			buffer.writeCompoundTag(wrappedStack.toCompound());
			buffer.writeFloat(energyValue.getValue());
		});
	}
	
	public static ExchangePacketUpdate decode(PacketBuffer buffer)
	{
		int count = buffer.readInt();
		ImmutableSortedMap.Builder<WrappedStack, EnergyValue> map = ImmutableSortedMap.<WrappedStack, EnergyValue>naturalOrder();
		for (int i = 0; i < count; i++)
		{
			WrappedStack stack = WrappedStack.fromCompound(buffer.readCompoundTag());
			EnergyValue energy = new EnergyValue(buffer.readFloat());
			if (stack == null)
				continue;
			map.put(stack, energy);
		}
		
		return new ExchangePacketUpdate(map.build());
	}
	
	public static void handle(ExchangePacketUpdate msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			EquivalentExchange.getEnergyValueManager().setPostValues(msg.values);
		});
		ctx.get().setPacketHandled(true);
	}
}
