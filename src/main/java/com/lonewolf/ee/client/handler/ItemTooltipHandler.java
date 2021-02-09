package com.lonewolf.ee.client.handler;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.exchange.EnergyValue;
import com.lonewolf.ee.exchange.WrappedStack;
import com.lonewolf.ee.inventory.ContainerAlchenomicon;
import com.lonewolf.ee.util.IOwnable;
import com.lonewolf.ee.util.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber
public class ItemTooltipHandler
{
	@SubscribeEvent
	public static void handleItemToolTip(ItemTooltipEvent event)
	{
		if (isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(
				GLFW.GLFW_KEY_RIGHT_SHIFT) || event.getPlayer() != null && (event.getPlayer().openContainer instanceof ContainerAlchenomicon))
		{
			WrappedStack wrappedStack = WrappedStack.wrap(event.getItemStack());
			EnergyValue energyValue = EquivalentExchange.getEnergyValueManager().getEnergyValue(event.getItemStack());
			EnergyValue stackEnergyValue = EquivalentExchange.getEnergyValueManager().getEnergyValueForStack(wrappedStack, false);
			
			if (energyValue != null)
			{
				if (wrappedStack.getStackSize() > 1)
				{
					event.getToolTip().add(
							new TranslationTextComponent("ee.tooltip.emc.single", energyValue.getValue()));
					event.getToolTip().add(
							new TranslationTextComponent("ee.tooltip.emc.stack", wrappedStack.getStackSize(),
							                             stackEnergyValue.getValue()));
				}
				else
				{
					event.getToolTip().add(
							new TranslationTextComponent("ee.tooltip.emc.single", energyValue.getValue()));
					
					AtomicReference<FluidStack> fluidStack = new AtomicReference<>();
					
					FluidUtil.getFluidContained(event.getItemStack()).ifPresent(fluidStack::set);
					
					if (fluidStack.get() != null)
					{
						EnergyValue value = EquivalentExchange.getEnergyValueManager().getEnergyValue(fluidStack.get());
						
						if (value != null)
						{
							event.getToolTip().add(
									new TranslationTextComponent("ee.tooltip.emc.mB", fluidStack.get().getAmount(), value));
							event.getToolTip().add(
									new TranslationTextComponent("ee.tooltip.emc.container", fluidStack.get().getAmount(), energyValue.getValue() - value.getValue()));
						}
					}
				}
			}
			else
			{
				event.getToolTip().add(new TranslationTextComponent("ee.tooltip.emc.none"));
			}
			
			assert wrappedStack != null;
			if (EquivalentExchange.getExchangeBlackList().isItemBlacklisted(event.getItemStack()))
			{
				event.getToolTip().add(new TranslationTextComponent("ee.tooltip.emc.notlearnable"));
			}
			
			if (EquivalentExchange.getPlayerKnowledgeManager().doesPlayerKnow(Objects.requireNonNull(event.getPlayer())
			                                                                         .getName().getString(),
			                                                                  event.getItemStack()))
			{
				event.getToolTip().add(new TranslationTextComponent("ee.tooltip.learned"));
			}
		}
		
		if (event.getItemStack().getItem() instanceof IOwnable)
		{
			UUID playerUUID = ItemStackUtils.getOwnerUUID(event.getItemStack());
			if (playerUUID != null && UsernameCache.containsUUID(playerUUID))
			{
				event.getToolTip().add(new TranslationTextComponent("ee.tooltip.belongTo",
				                                                    UsernameCache.getLastKnownUsername(playerUUID)));
			}
			else if (ItemStackUtils.getOwnerName(event.getItemStack()) != null)
			{
				event.getToolTip().add(new TranslationTextComponent("ee.tooltip.belongTo",
				                                                    ItemStackUtils.getOwnerName(event.getItemStack())));
			}
		}
	}
	
	private static boolean isKeyDown(int keycode)
	{
		return InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), keycode);
	}
}
