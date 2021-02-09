package com.lonewolf.ee.network.research_station;

import com.lonewolf.ee.network.ExchangeNetwork;
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

public class PacketUpdateRequestResearchStation {
    private final BlockPos pos;
    private final ResourceLocation dimension;

    public PacketUpdateRequestResearchStation(BlockPos pos, ResourceLocation dimension) {
        this.pos = pos;
        this.dimension = dimension;
    }

    public PacketUpdateRequestResearchStation(TileEntityResearchStation researchStation) {
        this(researchStation.getPos(), Objects.requireNonNull(researchStation.getWorld()).getDimensionKey().getLocation());
    }

    public static void encode(PacketUpdateRequestResearchStation msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.pos);
        buffer.writeResourceLocation(msg.dimension);
    }


    public static PacketUpdateRequestResearchStation decode(PacketBuffer buffer) {
        BlockPos pos = buffer.readBlockPos();
        ResourceLocation dimID = buffer.readResourceLocation();
        return new PacketUpdateRequestResearchStation(pos, dimID);
    }

    public static void handle(PacketUpdateRequestResearchStation msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            RegistryKey<World> worldRegistryKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, msg.dimension);
            World world = Minecraft.getInstance().getIntegratedServer().getWorld(worldRegistryKey);
            assert world != null;
            TileEntityResearchStation te = (TileEntityResearchStation) world.getTileEntity(msg.pos);
            ExchangeNetwork.simpleChannel.send(PacketDistributor.ALL.noArg(), new PacketUpdateResearchStation(te));
        });

        ctx.get().setPacketHandled(true);
    }
}
