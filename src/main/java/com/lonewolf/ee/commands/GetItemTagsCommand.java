package com.lonewolf.ee.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.text.StringTextComponent;

import java.util.Arrays;

public class GetItemTagsCommand
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		LiteralCommandNode<CommandSource> literalcommandnode = dispatcher.register(Commands.literal("getItemTags")
		                                                                                   .requires(
				                                                                                   (p_198816_0_) -> p_198816_0_
						                                                                                                    .hasPermissionLevel(
								                                                                                                    2))
		                                                                                   .then(Commands.argument(
				                                                                                   "target",
				                                                                                   EntityArgument
						                                                                                   .player()))
		                                                                                   .executes(
				                                                                                   GetItemTagsCommand::getItemTags));
	}
	
	private static int getItemTags(CommandContext<CommandSource> commandSourceCommandContext)
	{
		CommandSource source = commandSourceCommandContext.getSource();
		PlayerEntity entity = (PlayerEntity) source.getEntity();
		Item item = entity.getHeldItemMainhand().getItem();
		entity.sendStatusMessage(
				new StringTextComponent(Arrays.toString(ItemTags.getCollection().getOwningTags(item).toArray())),
				false);
		
		return 1;
	}
}
