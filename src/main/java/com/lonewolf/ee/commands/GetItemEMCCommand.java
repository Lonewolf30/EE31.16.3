package com.lonewolf.ee.commands;

import com.lonewolf.ee.EquivalentExchange;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.text.StringTextComponent;

import java.util.Arrays;

public class GetItemEMCCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		LiteralCommandNode<CommandSource> literalcommandnode = dispatcher.register(Commands.literal("getItemEMC")
		                                                                                   .requires(
				                                                                                   (p_198816_0_) -> p_198816_0_
						                                                                                                    .hasPermissionLevel(
								                                                                                                    2))
		                                                                                   .then(Commands.argument(
				                                                                                   "target",
				                                                                                   EntityArgument
						                                                                                   .player()))
		                                                                                   .executes(
				                                                                                   GetItemEMCCommand::getItemEMC));
	}
	
	private static int getItemEMC(CommandContext<CommandSource> commandSourceCommandContext)
	{
		CommandSource source = commandSourceCommandContext.getSource();
		PlayerEntity entity = (PlayerEntity) source.getEntity();
		assert entity != null;
		ItemStack item = entity.getHeldItemMainhand();
		entity.sendStatusMessage(
				new StringTextComponent(EquivalentExchange.getEnergyValueManager().getEnergyValue(item).toString()), false);
		
		return 1;
	}
}
