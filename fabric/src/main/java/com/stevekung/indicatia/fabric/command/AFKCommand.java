package com.stevekung.indicatia.fabric.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.stevekung.indicatia.fabric.core.IndicatiaFabric;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.utils.AFKMode;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import com.stevekung.stevekungslib.utils.CommonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.util.StringUtil;

public class AFKCommand
{
    public AFKCommand(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(ClientCommandManager.literal("afk")
                .then(ClientCommandManager.literal("start").executes(requirement -> AFKCommand.startAFK(requirement.getSource(), null))
                        .then(ClientCommandManager.argument("reason", StringArgumentType.greedyString()).executes(requirement -> AFKCommand.startAFK(requirement.getSource(), StringArgumentType.getString(requirement, "reason")))))
                .then(ClientCommandManager.literal("stop").executes(requirement -> AFKCommand.stopAFK(requirement.getSource())))
                .then(ClientCommandManager.literal("change_reason").then(ClientCommandManager.argument("reason", StringArgumentType.greedyString()).executes(requirement -> AFKCommand.setReason(requirement.getSource(), StringArgumentType.getString(requirement, "reason")))))
                .then(ClientCommandManager.literal("mode")
                        .then(ClientCommandManager.literal("idle").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.IDLE)))
                        .then(ClientCommandManager.literal("random_move").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.RANDOM_MOVE)))
                        .then(ClientCommandManager.literal("random_360").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.RANDOM_360)))
                        .then(ClientCommandManager.literal("random_move_360").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), AFKMode.RANDOM_MOVE_360)))));
    }

    private static int startAFK(FabricClientCommandSource source, String reason)
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

            if (IndicatiaFabric.CONFIG.general.enableAFKMessage)
            {
                Minecraft.getInstance().player.chat(message + reason);
            }
        }
        else
        {
            source.sendError(LangUtils.translate("commands.afk.afk_started"));
        }
        return 1;
    }

    private static int stopAFK(FabricClientCommandSource source)
    {
        if (HUDHelper.START_AFK)
        {
            HUDHelper.START_AFK = false;
            HUDHelper.afkMoveTicks = 0;

            if (IndicatiaFabric.CONFIG.general.enableAFKMessage)
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
            source.sendError(LangUtils.translate("commands.afk.afk_not_started"));
        }
        return 1;
    }

    private static int setReason(FabricClientCommandSource source, String newReason)
    {
        if (HUDHelper.START_AFK)
        {
            var oldReason = HUDHelper.AFK_REASON;
            HUDHelper.AFK_REASON = newReason;
            source.sendFeedback(LangUtils.translate("commands.afk.change_afk_reason", oldReason, newReason));
        }
        else
        {
            source.sendError(LangUtils.translate("commands.afk.afk_not_started"));
        }
        return 1;
    }

    private static int changeAFKMode(FabricClientCommandSource source, AFKMode mode)
    {
        HUDHelper.AFK_MODE = mode;
        source.sendFeedback(LangUtils.translate("commands.afk.set_afk_mode", mode));
        return 1;
    }
}