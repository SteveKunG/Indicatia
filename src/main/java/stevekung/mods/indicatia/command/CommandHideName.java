package stevekung.mods.indicatia.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;
import stevekung.mods.stevekunglib.utils.client.ClientCommandBase;

public class CommandHideName extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "inhidename";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
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

                String name = args[1];

                if (!HideNameData.getHideNameList().contains(name))
                {
                    HideNameData.getHideNameList().add(name);
                    ExtendedConfig.instance.save();
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.hidename_already_add")).setStyle(JsonUtils.red()));
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.hidename.usage");
                }

                String name = args[1];

                if (HideNameData.getHideNameList().contains(name))
                {
                    HideNameData.getHideNameList().remove(name);
                    ExtendedConfig.instance.save();
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.hidename_remove")).setStyle(JsonUtils.red()));
                }
            }
            else
            {
                throw new WrongUsageException("commands.hidename.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove");
        }
        if (args.length == 2 && !args[0].equals("add"))
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, HideNameData.getHideNameList());
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}