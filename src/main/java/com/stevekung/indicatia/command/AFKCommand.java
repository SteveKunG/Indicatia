package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.event.IndicatiaEventHandler;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.utils.AFKMode;
import com.stevekung.stevekungslib.utils.CommonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.command.ClientCommands;
import com.stevekung.stevekungslib.utils.client.command.IClientCommand;
import com.stevekung.stevekungslib.utils.client.command.IClientSuggestionProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.util.StringUtils;

public class AFKCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("afk")
                .then(ClientCommands.literal("start").executes(requirement -> AFKCommand.startAFK(requirement.getSource(), null))
                        .then(ClientCommands.argument("reason", StringArgumentType.greedyString()).executes(requirement -> AFKCommand.startAFK(requirement.getSource(), StringArgumentType.getString(requirement, "reason")))))
                .then(ClientCommands.literal("stop").executes(requirement -> AFKCommand.stopAFK(requirement.getSource())))
                .then(ClientCommands.literal("change_reason").then(ClientCommands.argument("reason", StringArgumentType.greedyString()).executes(requirement -> AFKCommand.setReason(requirement.getSource(), StringArgumentType.getString(requirement, "reason")))))
                .then(ClientCommands.literal("mode")
                        .then(ClientCommands.literal("idle").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.IDLE)))
                        .then(ClientCommands.literal("random_move").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.RANDOM_MOVE)))
                        .then(ClientCommands.literal("random_360").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.RANDOM_360)))
                        .then(ClientCommands.literal("random_move_360").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.RANDOM_MOVE_360)))));
    }

    private static int startAFK(IClientSuggestionProvider source, String reason)
    {
        if (InfoUtils.INSTANCE.isHypixel())
        {
            throw new CommandException(LangUtils.translateComponent("commands.not_allowed_hypixel"));
        }

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

    private static int stopAFK(IClientSuggestionProvider source)
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

    private static int setReason(IClientSuggestionProvider source, String newReason)
    {
        if (IndicatiaEventHandler.START_AFK)
        {
            String oldReason = IndicatiaEventHandler.AFK_REASON;
            IndicatiaEventHandler.AFK_REASON = newReason;
            source.sendFeedback(LangUtils.translateComponent("commands.afk.change_afk_reason", oldReason, newReason));
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.afk.afk_not_started"));
            return 1;
        }
    }

    private static int changeAFKMode(IClientSuggestionProvider source, AFKMode mode)
    {
        IndicatiaEventHandler.AFK_MODE = mode;
        source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", mode));
        return 1;
    }
}