package stevekung.mods.indicatia.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.ClientCommandBase;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class CommandRealmsMessage extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "realmsmsg";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.realmsmsg.usage");
        }
        else
        {
            ITextComponent component = ClientCommandBase.getChatComponentFromNthArg(args, 0);
            String text = component.getUnformattedText();

            if (text.equals("reset"))
            {
                ExtendedConfig.realmsMessage = "";
                sender.sendMessage(JsonUtils.create(LangUtils.translate("message.realmsmsg_reset")));
                ExtendedConfig.save();
            }
            else
            {
                ExtendedConfig.realmsMessage = text;
                sender.sendMessage(JsonUtils.create(LangUtils.translate("message.realmsmsg_set", text)));
                ExtendedConfig.save();
            }
        }
    }
}