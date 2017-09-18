package stevekung.mods.indicatia.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.handler.CommonHandler;
import stevekung.mods.indicatia.util.JsonUtil;

public class CommandAFK extends ClientCommandBase
{
    @Override
    public String getCommandName()
    {
        return "afk";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtil json = IndicatiaMod.json;

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.afk.usage");
        }
        else
        {
            if ("stop".equalsIgnoreCase(args[0]))
            {
                if (args.length > 1)
                {
                    throw new WrongUsageException("commands.afk.usage");
                }
                if (CommonHandler.isAFK)
                {
                    CommonHandler.isAFK = false;
                    CommonHandler.afkMoveTicks = 0;

                    if (ConfigManager.enableAFKMessage)
                    {
                        IndicatiaMod.MC.thePlayer.sendChatMessage("I'm back! AFK Time is : " + StringUtils.ticksToElapsedTime(CommonHandler.afkTicks) + " minutes");
                    }
                }
                else
                {
                    sender.addChatMessage(json.text("You have not start using /afk command").setChatStyle(json.red()));
                }
            }
            else if ("start".equalsIgnoreCase(args[0]))
            {
                if (!CommonHandler.isAFK)
                {
                    IChatComponent component = ClientCommandBase.getChatComponentFromNthArg(args, 1);
                    String reason = component.createCopy().getUnformattedText();
                    CommonHandler.isAFK = true;
                    CommonHandler.afkReason = reason;

                    if (reason.isEmpty())
                    {
                        reason = "";
                    }
                    else
                    {
                        reason = ", Reason : " + reason;
                    }

                    String message = "AFK for now";

                    if (ConfigManager.enableAFKMessage)
                    {
                        IndicatiaMod.MC.thePlayer.sendChatMessage(message + reason);
                    }
                }
                else
                {
                    sender.addChatMessage(json.text("You have already start /afk command").setChatStyle(json.red()));
                }
            }
            else if ("change_reason".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.afk.usage");
                }

                if (CommonHandler.isAFK)
                {
                    String oldReason = CommonHandler.afkReason;
                    String newReason = ClientCommandBase.getChatComponentFromNthArg(args, 1).createCopy().getUnformattedText();
                    CommonHandler.afkReason = newReason;
                    sender.addChatMessage(json.text("Change AFK Reason from " + oldReason + " to " + newReason));
                }
                else
                {
                    sender.addChatMessage(json.text("You have not start using /afk command").setChatStyle(json.red()));
                }
            }
            else if ("mode".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1 || args.length > 2)
                {
                    throw new WrongUsageException("commands.afk.mode.usage");
                }

                if ("idle".equalsIgnoreCase(args[1]))
                {
                    CommonHandler.afkMode = "idle";
                    CommonHandler.afkMoveTicks = 0;
                    sender.addChatMessage(json.text("Set AFK mode to idle"));
                }
                else if ("move".equalsIgnoreCase(args[1]))
                {
                    CommonHandler.afkMode = "move";
                    sender.addChatMessage(json.text("Set AFK mode to move"));
                }
                else if ("360".equalsIgnoreCase(args[1]))
                {
                    CommonHandler.afkMode = "360";
                    sender.addChatMessage(json.text("Set AFK mode to 360"));
                }
                else if ("360_move".equalsIgnoreCase(args[1]))
                {
                    CommonHandler.afkMode = "360_move";
                    sender.addChatMessage(json.text("Set AFK mode to 360 and move"));
                }
                else
                {
                    throw new WrongUsageException("commands.afk.mode.usage");
                }
            }
            else
            {
                throw new WrongUsageException("commands.afk.usage");
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "start", "stop", "mode", "change_reason");
        }
        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "idle", "move", "360", "360_move");
            }
        }
        return super.addTabCompletionOptions(sender, args, pos);
    }
}