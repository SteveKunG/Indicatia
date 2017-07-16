package stevekung.mods.indicatia.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.JsonUtil;

public class CommandAutoRealms extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "autorealms";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtil json = new JsonUtil();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.autorealms.usage");
        }
        else
        {
            ITextComponent component = ClientCommandBase.getChatComponentFromNthArg(args, 0);
            String text = component.getUnformattedText();

            if (text.equals("reset"))
            {
                ExtendedConfig.REALMS_MESSAGE = "";
                sender.sendMessage(json.text("Reset realm message"));
                ExtendedConfig.save();
            }
            else
            {
                ExtendedConfig.REALMS_MESSAGE = text;
                sender.sendMessage(json.text("Set realm message to " + "\"" + text + "\""));
                ExtendedConfig.save();
            }
        }
    }
}