package com.lonewolf.ee.tile_entity;

import com.lonewolf.ee.registry.TileEntityRegistry;
import com.lonewolf.ee.tile_entity.TileEntityEE;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class TileEntityEmptyAlchemy extends TileEntityEE
{
    private BlockPos controllerPos;

    public TileEntityEmptyAlchemy()
    {
        super(TileEntityRegistry.emptyAlchemyTileEntityType);
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        CompoundNBT controllerPos = nbt.getCompound("controllerPos");
        int x = controllerPos.getInt("x");
        int y = controllerPos.getInt("y");
        int z = controllerPos.getInt("z");
        this.controllerPos = new BlockPos(x,y,z);
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT controllerPos = new CompoundNBT();
        controllerPos.putInt("x", this.controllerPos.getX());
        controllerPos.putInt("y", this.controllerPos.getY());
        controllerPos.putInt("z", this.controllerPos.getZ());

        compound.put("controllerPos", controllerPos);

        return super.write(compound);
    }
}
