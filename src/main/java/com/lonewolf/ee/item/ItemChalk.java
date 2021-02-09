package com.lonewolf.ee.item;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.client.gui.GuiChalkSettings;
import com.lonewolf.ee.client.handler.KeyBindManager;
import com.lonewolf.ee.configuration.ConfigurationManager;
import com.lonewolf.ee.creative_tab.CreativeTab;
import com.lonewolf.ee.reference.Reference;
import com.lonewolf.ee.registry.BlockRegistry;
import com.lonewolf.ee.settings.ChalkSettings;
import com.lonewolf.ee.tile_entity.TileEntityAlchemyArray;
import com.lonewolf.ee.tile_entity.TileEntityEmptyAlchemy;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;

public class ItemChalk extends BlockItem
{
    private Block block;
    private BlockPos controllerPos;

    public ItemChalk()
    {
        super(BlockRegistry.emptyAlchemy, new Properties().maxDamage((int)Math.pow(ConfigurationManager.SERVER.chalkMaxSize.get(), 2)).group(CreativeTab.EE_TAB));
        this.setRegistryName(Reference.mod_id, "item_chalk");
    }

    private void drawAlchemicalArray(ItemUseContext context)
    {
        int arraySize = arraySize(context.getPlayer());
        BlockPos pos = context.getPos();
        controllerPos = pos.add(context.getFace().getDirectionVec());
        int x = pos.getX() - (int)Math.floor(arraySize / 2f);
        int z = pos.getZ() - (int)Math.floor(arraySize / 2f);

        for (int i = x; i < x + arraySize; i++) {
            for (int j = z; j < z + arraySize; j++) {
                BlockPos blockPos = new BlockPos(i, pos.getY(), j);
                BlockItemUseContext blockItemUseContext = new BlockItemUseContext(regenContext(context, blockPos));
                if (i == context.getPos().getX() && j == context.getPos().getZ()) {
                    block = BlockRegistry.alchemyController;
                }
                else
                    block = BlockRegistry.emptyAlchemy;
                tryPlace(blockItemUseContext);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (isSelected)
        {
            if (EquivalentExchange.getKeyBindManager().chalk_settings.isKeyDown() && Minecraft.getInstance().currentScreen == null)
            {
                Minecraft.getInstance().displayGuiScreen(new GuiChalkSettings());
            }
        }
    }

    public ItemUseContext regenContext(ItemUseContext context, BlockPos pos)
    {
        BlockRayTraceResult result = new BlockRayTraceResult(context.getHitVec(), context.getFace(), pos, context.isInside());

        return new ItemUseContext(context.getPlayer(), context.getHand(), result);
    }

    @Override
    @Nullable
    public BlockItemUseContext getBlockItemUseContext(BlockItemUseContext context) {
        return context;
    }

    private BlockState func_219985_a(BlockPos p_219985_1_, World p_219985_2_, ItemStack p_219985_3_, BlockState p_219985_4_) {
        BlockState blockstate = p_219985_4_;
        CompoundNBT compoundnbt = p_219985_3_.getTag();
        if (compoundnbt != null) {
            CompoundNBT compoundnbt1 = compoundnbt.getCompound("BlockStateTag");
            StateContainer<Block, BlockState> statecontainer = p_219985_4_.getBlock().getStateContainer();
            Iterator var9 = compoundnbt1.keySet().iterator();

            while(var9.hasNext()) {
                String s = (String)var9.next();
                Property<?> property = statecontainer.getProperty(s);
                if (property != null) {
                    String s1 = compoundnbt1.get(s).getString();
                    blockstate = func_219988_a(blockstate, property, s1);
                }
            }
        }

        if (blockstate != p_219985_4_) {
            p_219985_2_.setBlockState(p_219985_1_, blockstate, 2);
        }

        return blockstate;
    }

    private static <T extends Comparable<T>> BlockState func_219988_a(BlockState p_219988_0_, Property<T> p_219988_1_, String p_219988_2_) {
        return p_219988_1_.parseValue(p_219988_2_).map((p_219986_2_) -> (BlockState)p_219988_0_.with(p_219988_1_, p_219986_2_)).orElse(p_219988_0_);
    }

    @Override
    public ActionResultType tryPlace(BlockItemUseContext context) {
        if (!context.canPlace()) {
            return ActionResultType.FAIL;
        } else {
            BlockItemUseContext blockitemusecontext = this.getBlockItemUseContext(context);
            if (blockitemusecontext == null) {
                return ActionResultType.FAIL;
            } else {
                BlockState blockstate = this.getStateForPlacement(blockitemusecontext);
                if (blockstate == null) {
                    return ActionResultType.FAIL;
                } else if (!this.placeBlock(blockitemusecontext, blockstate)) {
                    return ActionResultType.FAIL;
                } else {
                    BlockPos blockpos = blockitemusecontext.getPos();
                    World world = blockitemusecontext.getWorld();
                    PlayerEntity playerentity = blockitemusecontext.getPlayer();
                    ItemStack itemstack = blockitemusecontext.getItem();
                    BlockState blockstate1 = world.getBlockState(blockpos);
                    Block block = blockstate1.getBlock();
                    if (block == blockstate.getBlock()) {
                        blockstate1 = this.func_219985_a(blockpos, world, itemstack, blockstate1);
                        this.onBlockPlaced(blockpos, world, playerentity, itemstack, blockstate1);
                        block.onBlockPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
                        if (playerentity instanceof ServerPlayerEntity) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos, itemstack);
                        }
                    }

                    return ActionResultType.func_233537_a_(world.isRemote);
                }
            }
        }
    }

    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state)
    {
        boolean returning = setTileEntityNBT(worldIn, player, pos, stack);

        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityEmptyAlchemy)
            ((TileEntityEmptyAlchemy)tileentity).setControllerPos(controllerPos);

        if (tileentity instanceof TileEntityAlchemyArray)
            ((TileEntityAlchemyArray) tileentity).setSettings(EquivalentExchange.getChalkSettingsManager().getChalkSetting(player.getUniqueID()));

        return returning;
    }

    protected boolean canPlace(BlockItemUseContext p_195944_1_, BlockState p_195944_2_) {
        PlayerEntity playerentity = p_195944_1_.getPlayer();
        ISelectionContext iselectioncontext = playerentity == null ? ISelectionContext.dummy() : ISelectionContext.forEntity(playerentity);
        return (!this.checkPosition() || p_195944_2_.isValidPosition(p_195944_1_.getWorld(), p_195944_1_.getPos())) && p_195944_1_.getWorld().placedBlockCollides(p_195944_2_, p_195944_1_.getPos(), iselectioncontext);
    }

    protected boolean checkPosition() {
        return true;
    }

    @Nullable
    protected BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = block.getStateForPlacement(context);
        return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
    }

    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        return context.getWorld().setBlockState(context.getPos(), state, 11);
    }

    public int arraySize(PlayerEntity entity) {
        ChalkSettings settings = EquivalentExchange.getChalkSettingsManager().getChalkSetting(entity.getUniqueID());
        return settings.getSize();
    }

    public boolean canUseChalk(ItemUseContext context) {
        int arraySize = arraySize(Objects.requireNonNull(context.getPlayer()));

        BlockPos pos = context.getPos();
        pos = pos.add(context.getFace().getDirectionVec());
        int x = pos.getX() - (int)Math.floor(arraySize / 2f);
        int z = pos.getZ() - (int)Math.floor(arraySize / 2f);

        int count = 0;

        for (int i = x; i < arraySize + x; i++) {
            for (int j = z; j < arraySize + z; j++) {
                BlockPos blockPos = new BlockPos(i, pos.getY(), j);
                Block block = context.getWorld().getBlockState(blockPos).getBlock();
                Block ashStone = context.getWorld().getBlockState(blockPos.add(0,-1,0)).getBlock();
                if (blockPos.equals(context.getPlayer().getPosition()))
                    return false;
                if (block.equals(Blocks.AIR) && ashStone.equals(BlockRegistry.ashInfusedStone))
                    count++;
            }
        }
        return count == (arraySize * arraySize);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (canUseChalk(context)) {
            drawAlchemicalArray(context);
            return ActionResultType.func_233537_a_(context.getWorld().isRemote);
        }

        return ActionResultType.FAIL;
    }
}
