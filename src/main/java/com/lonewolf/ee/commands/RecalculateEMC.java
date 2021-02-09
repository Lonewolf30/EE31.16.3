package com.lonewolf.ee.commands;

import com.lonewolf.ee.EquivalentExchange;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class RecalculateEMC
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        LiteralCommandNode<CommandSource> literalcommandnode = dispatcher.register(Commands.literal("recalculateEMCValues")
                .requires(
                        (p_198816_0_) -> p_198816_0_
                                .hasPermissionLevel(
                                        2))
                .executes(
                        RecalculateEMC::recalculate));
    }

    private static int recalculate(CommandContext<CommandSource> commandSourceCommandContext)
    {
        CommandSource source = commandSourceCommandContext.getSource();
        PlayerEntity entity = (PlayerEntity) source.getEntity();
        entity.sendStatusMessage(
                new StringTextComponent("Recalculating EMC"), false);
        EquivalentExchange.getEnergyValueManager().compute();

        return 1;
    }
}
