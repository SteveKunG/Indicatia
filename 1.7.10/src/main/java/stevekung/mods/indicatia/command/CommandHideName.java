package stevekung.mods.indicatia.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.HideNameData;
import stevekung.mods.indicatia.util.JsonUtil;

public class CommandHideName extends ClientCommandBase
{
    @Override
    public String getCommandName()
    {
        return "inhidename";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtil json = IndicatiaMod.json;

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.hidename.usage");
        }
        else
        {
            if ("add".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.hidename.usage");
                }
                if (!HideNameData.getHideNameList().contains(args[1]))
                {
                    HideNameData.getHideNameList().add(args[1]);
                    ExtendedConfig.save();
                }
                else
                {
                    sender.addChatMessage(json.text(args[1] + " is already added!").setChatStyle(json.red()));
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.hidename.usage");
                }
                if (HideNameData.getHideNameList().contains(args[1]))
                {
                    HideNameData.getHideNameList().remove(args[1]);
                    ExtendedConfig.save();
                }
                else
                {
                    sender.addChatMessage(json.text(args[1] + " is already removed!").setChatStyle(json.red()));
                }
            }
            else
            {
                throw new WrongUsageException("commands.hidename.usage");
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove");
        }
        if (args.length == 2 && !args[0].equals("add"))
        {
            return ClientCommandBase.getListOfStringsMatchingLastWord(args, HideNameData.getHideNameList());
        }
        return super.addTabCompletionOptions(sender, args);
    }
}