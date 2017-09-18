package stevekung.mods.indicatia.command;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.AutoLogin.AutoLoginData;
import stevekung.mods.indicatia.util.Base64Util;
import stevekung.mods.indicatia.util.GameProfileUtil;
import stevekung.mods.indicatia.util.JsonUtil;

public class CommandAutoLogin extends ClientCommandBase
{
    @Override
    public String getCommandName()
    {
        return "autologin";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtil json = IndicatiaMod.json;
        Minecraft mc = IndicatiaMod.MC;
        ServerData data = mc.getCurrentServerData();
        UUID uuid = GameProfileUtil.getUUID();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.autologin.usage");
        }
        else
        {
            if ("add".equalsIgnoreCase(args[0]))
            {
                if (args.length <= 2)
                {
                    throw new WrongUsageException("commands.autologin.usage");
                }
                if (!mc.isSingleplayer())
                {
                    if (data != null)
                    {
                        if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.serverIP) != null)
                        {
                            sender.addChatMessage(json.text("An auto login data already set for Username: " + uuid.toString() + "!").setStyle(json.red()));
                            return;
                        }
                        ITextComponent component = ClientCommandBase.getChatComponentFromNthArg(args, 2);
                        String value = component.createCopy().getUnformattedText();
                        ExtendedConfig.loginData.addAutoLogin(data.serverIP, "/" + args[1] + " ", Base64Util.encode(value), uuid);
                        sender.addChatMessage(json.text("Set auto login data for Server: " + data.serverIP));
                        ExtendedConfig.save();
                    }
                }
                else if (mc.isSingleplayer())
                {
                    sender.addChatMessage(json.text("Cannot add auto login data in singleplayer!").setStyle(json.red()));
                    return;
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                if (!mc.isSingleplayer())
                {
                    if (data != null)
                    {
                        if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.serverIP) != null)
                        {
                            ExtendedConfig.loginData.removeAutoLogin(uuid + data.serverIP);
                            sender.addChatMessage(json.text("Remove auto login data from Username: " + uuid.toString()));
                        }
                        else
                        {
                            sender.addChatMessage(json.text("No auto login data was set for Username: " + uuid.toString() + "!").setStyle(json.red()));
                        }
                    }
                }
                else if (mc.isSingleplayer())
                {
                    sender.addChatMessage(json.text("Cannot remove auto login data in singleplayer!").setStyle(json.red()));
                    return;
                }
            }
            else if ("list".equalsIgnoreCase(args[0]))
            {
                Collection<AutoLoginData> collection = ExtendedConfig.loginData.getAutoLoginList();

                if (collection.isEmpty())
                {
                    throw new CommandException("commands.autologin.list.empty");
                }
                else
                {
                    TextComponentTranslation component = new TextComponentTranslation("commands.autologin.list.count", collection.size());
                    component.getStyle().setColor(TextFormatting.DARK_GREEN);
                    sender.addChatMessage(component);

                    for (AutoLoginData loginData : collection)
                    {
                        sender.addChatMessage(new TextComponentTranslation("commands.autologin.list.entry", loginData.getServerIP(), loginData.getUUID()));
                    }
                }
            }
            else
            {
                throw new WrongUsageException("commands.autologin.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "list");
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}