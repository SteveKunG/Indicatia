package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.text.ITextComponent;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.stevekunglib.utils.CommonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class AFKCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("afk").requires(requirement -> requirement.hasPermissionLevel(0)).then(Commands.literal("start").executes(requirement ->
                AFKCommand.startAFK(requirement.getSource(), null)
        ).then(Commands.argument("reason", MessageArgument.message()).executes(requirement ->
                AFKCommand.startAFK(requirement.getSource(), MessageArgument.getMessage(requirement, "reason"))
        ))).then(Commands.literal("stop").executes(requirement ->
                AFKCommand.stopAFK(requirement.getSource())
        )).then(Commands.literal("change_reason")
                .then(Commands.argument("reason", MessageArgument.message()).executes(requirement ->
                        AFKCommand.setReason(requirement.getSource(), MessageArgument.getMessage(requirement, "reason"))
                ))).then(Commands.literal("mode").then(Commands.literal("idle").executes(requirement ->
                AFKCommand.changeAFKMode(requirement.getSource(), "idle")
        )).then(Commands.literal("move").executes(requirement ->
                AFKCommand.changeAFKMode(requirement.getSource(), "move")
        )).then(Commands.literal("360").executes(requirement ->
                AFKCommand.changeAFKMode(requirement.getSource(), "360")
        )).then(Commands.literal("360_move").executes(requirement ->
                AFKCommand.changeAFKMode(requirement.getSource(), "360_move")
        ))));
    }

    private static int startAFK(CommandSource source, ITextComponent component)
    {
        if (!IndicatiaEventHandler.isAFK)
        {
            String reason = component == null ? "" : component.createCopy().getUnformattedComponentText();
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

            if (IndicatiaMod.INSTANCE.getConfig().getOrElse("enableAFKMessage", true))
            {
                Minecraft.getInstance().player.sendChatMessage(message + reason);
            }
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.afk.afk_started"));
        }
        return 0;
    }

    private static int stopAFK(CommandSource source)
    {
        if (IndicatiaEventHandler.isAFK)
        {
            IndicatiaEventHandler.isAFK = false;
            IndicatiaEventHandler.afkMoveTicks = 0;

            if (IndicatiaMod.INSTANCE.getConfig().getOrElse("enableAFKMessage", true))
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
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.afk.afk_not_started"));
        }
        return 0;
    }

    private static int setReason(CommandSource source, ITextComponent component)
    {
        if (IndicatiaEventHandler.isAFK)
        {
            String oldReason = IndicatiaEventHandler.afkReason;
            String newReason = component.createCopy().getUnformattedComponentText();
            IndicatiaEventHandler.afkReason = newReason;
            source.sendFeedback(LangUtils.translateComponent("commands.afk.change_afk_reason", oldReason, newReason), false);
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.afk.afk_not_started"));
        }
        return 0;
    }

    private static int changeAFKMode(CommandSource source, String mode)
    {
        if ("idle".equalsIgnoreCase(mode))
        {
            IndicatiaEventHandler.afkMode = "idle";
            IndicatiaEventHandler.afkMoveTicks = 0;
            source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", IndicatiaEventHandler.afkMode), false);
        }
        else if ("move".equalsIgnoreCase(mode))
        {
            IndicatiaEventHandler.afkMode = "move";
            source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", IndicatiaEventHandler.afkMode), false);
        }
        else if ("360".equalsIgnoreCase(mode))
        {
            IndicatiaEventHandler.afkMode = "360";
            source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", IndicatiaEventHandler.afkMode), false);
        }
        else if ("360_move".equalsIgnoreCase(mode))
        {
            IndicatiaEventHandler.afkMode = "360_move";
            source.sendFeedback(LangUtils.translateComponent("commands.afk.set_afk_mode", IndicatiaEventHandler.afkMode), false);
        }
        return 0;
    }
}