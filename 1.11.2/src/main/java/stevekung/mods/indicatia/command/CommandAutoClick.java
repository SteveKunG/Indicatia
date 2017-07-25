package stevekung.mods.indicatia.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import stevekung.mods.indicatia.handler.CommonHandler;
import stevekung.mods.indicatia.util.JsonUtil;

public class CommandAutoClick extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "autoclick";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtil json = new JsonUtil();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.autoclick.usage");
        }
        else
        {
            if ("stop".equalsIgnoreCase(args[0]))
            {
                CommonHandler.autoClick = false;
                CommonHandler.autoClickTicks = 0;
                sender.sendMessage(json.text("Disabled Auto Click"));
            }
            else if ("start".equalsIgnoreCase(args[0]))
            {
                CommonHandler.autoClick = true;
                sender.sendMessage(json.text("Enabled Auto Click"));
            }
            else if ("mode".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.autoclick.mode.usage");
                }
                if ("left".equalsIgnoreCase(args[1]))
                {
                    CommonHandler.autoClickMode = "left";
                    sender.sendMessage(json.text("Changed Auto Click mode to Left"));
                }
                else if ("right".equalsIgnoreCase(args[1]))
                {
                    CommonHandler.autoClickMode = "right";
                    sender.sendMessage(json.text("Changed Auto Click mode to Right"));
                }
            }
            else
            {
                throw new WrongUsageException("commands.autoclick.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "start", "stop", "mode");
        }
        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "left", "right");
            }
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}