package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.stevekung.stevekungslib.utils.CommonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import stevekung.mods.indicatia.config.IndicatiaConfig;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;

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
                        .then(Commands.literal("idle").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), "idle")))
                        .then(Commands.literal("move").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), "move")))
                        .then(Commands.literal("360").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), "360")))
                        .then(Commands.literal("360_move").executes(requirement -> AFKCommand.changeAFKMode(requirement.getSource(), "360_move")))));
    }

    private static int startAFK(CommandSource source, String reason)
    {
        if (!IndicatiaEventHandler.isAFK)
        {
            IndicatiaEventHandler.isAFK = true;
            IndicatiaEventHandler.afkReason = reason;

            if (reason.isEmpty())
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
        if (IndicatiaEventHandler.isAFK)
        {
            IndicatiaEventHandler.isAFK = false;
            IndicatiaEventHandler.afkMoveTicks = 0;

            if (IndicatiaConfig.GENERAL.enableAFKMessage.get())
            {
                if (IndicatiaEventHandler.afkReason.isEmpty())
                {
                    Minecraft.getInstance().player.sendChatMessage(LangUtils.translateComponent("commands.afk.afk_stopped", CommonUtils.ticksToElapsedTime(IndicatiaEventHandler.afkTicks)).getUnformattedComponentText());
                }
                else
                {
                    Minecraft.getInstance().player.sendChatMessage(LangUtils.translateComponent("commands.afk.afk_stopped_with_reason", IndicatiaEventHandler.afkReason, CommonUtils.ticksToElapsedTime(IndicatiaEventHandler.afkTicks)).getUnformattedComponentText());
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
        if (IndicatiaEventHandler.isAFK)
        {
            String oldReason = IndicatiaEventHandler.afkReason;
            IndicatiaEventHandler.afkReason = newReason;
            source.sendFeedback(LangUtils.translateComponent("commands.afk.change_afk_reason", oldReason, newReason), false);
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.afk.afk_not_started"));
            return 1;
        }
    }

    private static int changeAFKMode(CommandSource source, String mode)
    {
        switch (mode)
        {
        case "idle":
            IndicatiaEventHandler.afkMode = "idle";
            IndicatiaEventHandler.afkMoveTicks = 0;
            source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", IndicatiaEventHandler.afkMode), false);
            return 1;
        case "move":
            IndicatiaEventHandler.afkMode = "move";
            source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", IndicatiaEventHandler.afkMode), false);
            return 1;
        case "360":
            IndicatiaEventHandler.afkMode = "360";
            source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", IndicatiaEventHandler.afkMode), false);
            return 1;
        case "360_move":
            IndicatiaEventHandler.afkMode = "360_move";
            source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", IndicatiaEventHandler.afkMode), false);
            return 1;
        }
        return 0;
    }
}