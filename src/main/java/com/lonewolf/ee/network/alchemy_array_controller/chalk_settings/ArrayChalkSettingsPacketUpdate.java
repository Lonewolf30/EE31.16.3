package com.lonewolf.ee.network.alchemy_array_controller.chalk_settings;

import com.lonewolf.ee.settings.ChalkSettings;
import com.lonewolf.ee.tile_entity.TileEntityAlchemyArray;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ArrayChalkSettingsPacketUpdate
{
    private int array_size;
    private int array_rotation;
    private ResourceLocation array_index;

    private BlockPos blockPos;

    public ArrayChalkSettingsPacketUpdate(int array_size, int array_rotation, ResourceLocation array_index, BlockPos blockPos) {
        this.array_size = array_size;
        this.array_rotation = array_rotation;
        this.array_index = array_index;
        this.blockPos = blockPos;
    }

    public ArrayChalkSettingsPacketUpdate(TileEntityAlchemyArray alchemyArray)
    {
        array_size = alchemyArray.getSize();
        array_rotation = alchemyArray.getSettings().getRotation();
        array_index = alchemyArray.getSettings().getIndex();
        blockPos = alchemyArray.getPos();
    }

    public ArrayChalkSettingsPacketUpdate()
    {
    }

    public static void encode(ArrayChalkSettingsPacketUpdate msg, PacketBuffer buffer)
    {
        buffer.writeInt(msg.array_size);
        buffer.writeInt(msg.array_rotation);
        buffer.writeResourceLocation(msg.array_index);
        buffer.writeBlockPos(msg.blockPos);
    }

    public static ArrayChalkSettingsPacketUpdate decode(PacketBuffer buffer)
    {
        int array_size = buffer.readInt();
        int array_rotation = buffer.readInt();
        ResourceLocation array_index = buffer.readResourceLocation();
        BlockPos blockPos = buffer.readBlockPos();

        return new ArrayChalkSettingsPacketUpdate(array_size, array_rotation, array_index, blockPos);
    }

    public static void handle(ArrayChalkSettingsPacketUpdate msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            assert Minecraft.getInstance().world != null;
            TileEntityAlchemyArray te = (TileEntityAlchemyArray) Minecraft.getInstance().world.getTileEntity(msg.blockPos);
            if (te != null)
            {
                ChalkSettings settings = new ChalkSettings(msg.array_index, msg.array_size, msg.array_rotation);
                te.setSettings(settings);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
