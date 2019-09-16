package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.event.IndicatiaEventHandler;
import com.stevekung.indicatia.utils.AFKMode;
import com.stevekung.stevekungslib.utils.CommonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.StringUtils;

public class AFKCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("afk").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(Commands.literal("start").executes(requirement -> AFKCommand.startAFK(requirement.getSource(), null))
                        .then(Commands.argument("reason", StringArgumentType.greedyString()).executes(requirement -> AFKCommand.startAFK(requirement.getSource(), StringArgumentType.getString(requirement, "reason")))))
                .then(Commands.literal("stop").executes(requirement -> AFKCommand.stopAFK(requirement.getSource())))
                .then(Commands.literal("change_reason").then(Commands.argument("reason", StringArgumentType.greedyString()).executes(requirement -> AFKCommand.setReason(requirement.getSource(), StringArgumentType.getString(requirement, "reason")))))
                .then(Commands.literal("mode")
                        .then(Commands.literal("idle").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.IDLE)))
                        .then(Commands.literal("random_move").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.RANDOM_MOVE)))
                        .then(Commands.literal("random_360").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.RANDOM_360)))
                        .then(Commands.literal("random_move_360").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.RANDOM_MOVE_360)))));
    }

    private static int startAFK(CommandSource source, String reason)
    {
        if (!IndicatiaEventHandler.START_AFK)
        {
            IndicatiaEventHandler.START_AFK = true;
            IndicatiaEventHandler.AFK_REASON = reason;

            if (StringUtils.isNullOrEmpty(reason))
            {
                reason = "";
            }
            else
            {
                reason = ", " + LangUtils.translate("commands.afk.reason", reason);
            }

            String message = LangUtils.translate("commands.afk.afk_now");

            if (IndicatiaConfig.GENERAL.enableAFKMessage.get())
            {
                Minecraft.getInstance().player.sendChatMessage(message + reason);
            }
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.afk.afk_started"));
            return 1;
        }
    }

    private static int stopAFK(CommandSource source)
    {
        if (IndicatiaEventHandler.START_AFK)
        {
            IndicatiaEventHandler.START_AFK = false;
            IndicatiaEventHandler.afkMoveTicks = 0;

            if (IndicatiaConfig.GENERAL.enableAFKMessage.get())
            {
                if (IndicatiaEventHandler.AFK_REASON.isEmpty())
                {
                    Minecraft.getInstance().player.sendChatMessage(LangUtils.translateComponent("commands.afk.afk_stopped", CommonUtils.ticksToElapsedTime(IndicatiaEventHandler.afkTicks)).getUnformattedComponentText());
                }
                else
                {
                    Minecraft.getInstance().player.sendChatMessage(LangUtils.translateComponent("commands.afk.afk_stopped_with_reason", IndicatiaEventHandler.AFK_REASON, CommonUtils.ticksToElapsedTime(IndicatiaEventHandler.afkTicks)).getUnformattedComponentText());
                }
            }
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.afk.afk_not_started"));
            return 1;
        }
    }

    private static int setReason(CommandSource source, String newReason)
    {
        if (IndicatiaEventHandler.START_AFK)
        {
            String oldReason = IndicatiaEventHandler.AFK_REASON;
            IndicatiaEventHandler.AFK_REASON = newReason;
            source.sendFeedback(LangUtils.translateComponent("commands.afk.change_afk_reason", oldReason, newReason), false);
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.afk.afk_not_started"));
            return 1;
        }
    }

    private static int changeAFKMode(CommandSource source, AFKMode mode)
    {
        IndicatiaEventHandler.AFK_MODE = mode;
        source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", mode), false);
        return 1;
    }
}