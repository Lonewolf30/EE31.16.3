package com.lonewolf.ee.item;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.client.gui.GuiChalkSettings;
import com.lonewolf.ee.inventory.ContainerAlchenomicon;
import com.lonewolf.ee.inventory.InventoryAlchenomicon;
import com.lonewolf.ee.util.IOwnable;
import com.lonewolf.ee.util.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;

public class ItemAlchenomicon extends BaseItem implements IOwnable
{
	public ItemAlchenomicon()
	{
		super("alchenomicon");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(
			World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (ItemStackUtils.getOwnerUUID(stack) == null)
		{
			ItemStackUtils.setOwner(stack, playerIn);
			playerIn.sendStatusMessage(
					new TranslationTextComponent("misc.ee.owner-set", stack.getDisplayName().getString()), true);
		}
		else
		{
			Minecraft.getInstance().displayGuiScreen(new GuiChalkSettings());
		}
		return ActionResult.resultPass(stack);
	}
	
	private INamedContainerProvider getContainer(ItemStack stack)
	{
		return new INamedContainerProvider()
		{
			@Override
			public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
			{
				return new ContainerAlchenomicon(id, playerInventory, new InventoryAlchenomicon(
						EquivalentExchange.getPlayerKnowledgeManager()
						                  .getKnowledge(ItemStackUtils.getOwnerName(stack))));
			}
			
			@Override
			public ITextComponent getDisplayName()
			{
				return new StringTextComponent("alchenomicon_gui");
			}
		};
	}
}
