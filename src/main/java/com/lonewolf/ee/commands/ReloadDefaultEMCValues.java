package com.lonewolf.ee.commands;

import com.lonewolf.ee.EquivalentExchange;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ReloadDefaultEMCValues
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        LiteralCommandNode<CommandSource> literalcommandnode = dispatcher.register(Commands.literal("reloadDefaultEmcValues")
                .requires(
                        (p_198816_0_) -> p_198816_0_
                                .hasPermissionLevel(
                                        2))
                .executes(
                        ReloadDefaultEMCValues::reload));
    }

    private static int reload(CommandContext<CommandSource> commandSourceCommandContext)
    {
        CommandSource source = commandSourceCommandContext.getSource();
        PlayerEntity entity = (PlayerEntity) source.getEntity();
        entity.sendStatusMessage(
                new StringTextComponent("Reloading Default EMC"), false);


        EquivalentExchange.getEnergyValueManager().getExchangeValuesFileManager().getPreExchangeValues().reload();
        entity.sendStatusMessage(
                new StringTextComponent("Recalculating EMC Values"), false);
        EquivalentExchange.getEnergyValueManager().compute();

        entity.sendStatusMessage(
                new StringTextComponent("EMC Values calculated"), false);

        return 1;
    }
}
