package com.lonewolf.ee.tile_entity;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.alchemy.AlchemyArray;
import com.lonewolf.ee.alchemy.ArrayManager;
import com.lonewolf.ee.network.ExchangeNetwork;
import com.lonewolf.ee.network.alchemy_array_controller.chalk_settings.ArrayChalkSettingsRequestPacket;
import com.lonewolf.ee.network.research_station.PacketUpdateRequestResearchStation;
import com.lonewolf.ee.registry.TileEntityRegistry;
import com.lonewolf.ee.settings.ChalkSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.system.CallbackI;

public class TileEntityAlchemyArray extends TileEntityEE implements IInventory, ITickableTileEntity
{
    private ChalkSettings settings;
    private AlchemyArray array;
    private Inventory inventory;

    public TileEntityAlchemyArray()
    {
        super(TileEntityRegistry.alchemyArrayTileEntityType);
    }

    public void BreakArray()
    {
        if (settings == null)
            settings = new ChalkSettings();

        int arraySize = settings.getSize();

        int x = pos.getX() - (int)Math.floor(arraySize / 2f);
        int z = pos.getZ() - (int)Math.floor(arraySize / 2f);

        for (int i = x; i < x + arraySize; i++) {
            for (int j = z; j < z + arraySize; j++) {
                BlockPos blockPos = new BlockPos(i, pos.getY(), j);
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 35);
            }
        }
    }

    @Override
    public void onLoad()
    {
        assert world != null;
        if (world.isRemote())
        {
            ExchangeNetwork.sendPacketToSever(new ArrayChalkSettingsRequestPacket(this));
        }
    }

    public void setSettings(ChalkSettings settings)
    {
        this.settings = new ChalkSettings();
        this.settings.setSize(settings.getSize());
        this.settings.setRotation(settings.getRotation());
        this.settings.setIndex(settings.getIndex());

        array = EquivalentExchange.getArrayManager().getArray(settings.getIndex());
    }

    public int getSize()
    {
        if (settings == null)
            settings = new ChalkSettings();

        return settings.getSize();
    }

    public ChalkSettings getSettings()
    {
        return settings;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt)
    {
        if (settings == null)
            settings = new ChalkSettings();
        CompoundNBT chalk_settings = nbt.getCompound("chalk_settings");
        settings.setIndex(new ResourceLocation(chalk_settings.getString("index")));
        settings.setRotation(chalk_settings.getInt("rotation"));
        settings.setSize(chalk_settings.getInt("size"));

        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("index", settings.getIndex().toString());
        nbt.putInt("rotation", settings.getRotation());
        nbt.putInt("size", settings.getSize());
        compound.put("chalk_settings", nbt);
        return super.write(compound);
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {

    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void tick() {

    }

    public AlchemyArray getArray() {
        return array;
    }

    public ResourceLocation getArrayName()
    {
        return settings.getIndex();
    }
}
