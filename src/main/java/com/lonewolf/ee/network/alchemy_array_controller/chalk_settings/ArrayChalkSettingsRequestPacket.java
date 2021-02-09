package com.lonewolf.ee.network.alchemy_array_controller.chalk_settings;

import com.lonewolf.ee.network.ExchangeNetwork;
import com.lonewolf.ee.network.research_station.PacketUpdateRequestResearchStation;
import com.lonewolf.ee.network.research_station.PacketUpdateResearchStation;
import com.lonewolf.ee.tile_entity.TileEntityAlchemyArray;
import com.lonewolf.ee.tile_entity.TileEntityResearchStation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Objects;
import java.util.function.Supplier;

public class ArrayChalkSettingsRequestPacket
{
    private final BlockPos pos;
    private final ResourceLocation dimension;

    public ArrayChalkSettingsRequestPacket(BlockPos pos, ResourceLocation dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public ArrayChalkSettingsRequestPacket(TileEntityAlchemyArray tileEntityAlchemyArray) {
        this(tileEntityAlchemyArray.getPos(), Objects.requireNonNull(tileEntityAlchemyArray.getWorld()).getDimensionKey().getLocation());
    }

    public static void encode(ArrayChalkSettingsRequestPacket msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeResourceLocation(msg.dimension);
    }

    public static ArrayChalkSettingsRequestPacket decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        ResourceLocation dimID = buffer.readResourceLocation();
        return new ArrayChalkSettingsRequestPacket(pos, dimID);
    }

    public static void handle(ArrayChalkSettingsRequestPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            RegistryKey<World> worldRegistryKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, msg.dimension);
            World world = Minecraft.getInstance().getIntegratedServer().getWorld(worldRegistryKey);
            assert world != null;
            if (world.getTileEntity(msg.pos) instanceof TileEntityAlchemyArray) {
                TileEntityAlchemyArray te = (TileEntityAlchemyArray) world.getTileEntity(msg.pos);
                ExchangeNetwork.simpleChannel.send(PacketDistributor.ALL.noArg(), new ArrayChalkSettingsPacketUpdate(te));
                ctx.get().setPacketHandled(true);
            }else
            {
                ctx.get().setPacketHandled(false);
            }
        });
    }
}
