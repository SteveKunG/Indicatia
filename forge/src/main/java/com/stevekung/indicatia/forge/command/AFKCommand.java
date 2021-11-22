package com.stevekung.indicatia.forge.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.stevekung.indicatia.forge.config.IndicatiaConfig;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.utils.AFKMode;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import com.stevekung.stevekunglib.forge.utils.client.command.ClientCommands;
import com.stevekung.stevekunglib.forge.utils.client.command.IClientCommand;
import com.stevekung.stevekunglib.forge.utils.client.command.IClientSharedSuggestionProvider;
import com.stevekung.stevekunglib.utils.CommonUtils;
import com.stevekung.stevekunglib.utils.LangUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.util.StringUtil;

public class AFKCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSharedSuggestionProvider> dispatcher)
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

    private static int startAFK(IClientSharedSuggestionProvider source, String reason)
    {
        if (InfoUtils.INSTANCE.isHypixel())
        {
            throw new CommandRuntimeException(LangUtils.translate("commands.not_allowed_hypixel"));
        }

        if (!HUDHelper.START_AFK)
        {
            HUDHelper.START_AFK = true;
            HUDHelper.AFK_REASON = reason;

            if (StringUtil.isNullOrEmpty(reason))
            {
                reason = "";
            }
            else
            {
                reason = ", " + LangUtils.translate("commands.afk.reason").getString() + ": " + reason;
            }

            var message = LangUtils.translate("commands.afk.afk_now").getString();

            if (IndicatiaConfig.GENERAL.enableAFKMessage.get())
            {
                Minecraft.getInstance().player.chat(message + reason);
            }
        }
        else
        {
            source.sendErrorMessage(LangUtils.translate("commands.afk.afk_started"));
        }
        return 1;
    }

    private static int stopAFK(IClientSharedSuggestionProvider source)
    {
        if (HUDHelper.START_AFK)
        {
            HUDHelper.START_AFK = false;
            HUDHelper.afkMoveTicks = 0;

            if (IndicatiaConfig.GENERAL.enableAFKMessage.get())
            {
                if (StringUtil.isNullOrEmpty(HUDHelper.AFK_REASON))
                {
                    Minecraft.getInstance().player.chat(LangUtils.translate("commands.afk.afk_stopped", CommonUtils.ticksToElapsedTime(HUDHelper.afkTicks)).getString());
                }
                else
                {
                    Minecraft.getInstance().player.chat(LangUtils.translate("commands.afk.afk_stopped_with_reason", HUDHelper.AFK_REASON, CommonUtils.ticksToElapsedTime(HUDHelper.afkTicks)).getString());
                }
            }
        }
        else
        {
            source.sendErrorMessage(LangUtils.translate("commands.afk.afk_not_started"));
        }
        return 1;
    }

    private static int setReason(IClientSharedSuggestionProvider source, String newReason)
    {
        if (HUDHelper.START_AFK)
        {
            var oldReason = HUDHelper.AFK_REASON;
            HUDHelper.AFK_REASON = newReason;
            source.sendFeedback(LangUtils.translate("commands.afk.change_afk_reason", oldReason, newReason));
        }
        else
        {
            source.sendErrorMessage(LangUtils.translate("commands.afk.afk_not_started"));
        }
        return 1;
    }

    private static int changeAFKMode(IClientSharedSuggestionProvider source, AFKMode mode)
    {
        HUDHelper.AFK_MODE = mode;
        source.sendFeedback(LangUtils.translate("commands.afk.set_afk_mode", mode));
        return 1;
    }
}