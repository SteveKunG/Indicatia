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
import stevekung.mods.indicatia.gui.GuiAutoLoginFunction;
import stevekung.mods.indicatia.gui.GuiAutoLoginFunctionHelp;
import stevekung.mods.indicatia.utils.AutoLogin.AutoLoginData;
import stevekung.mods.indicatia.utils.Base64Utils;
import stevekung.mods.stevekunglib.utils.ClientCommandBase;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class CommandAutoLogin extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "autologin";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        Minecraft mc = Minecraft.getMinecraft();
        ServerData data = mc.getCurrentServerData();
        UUID uuid = GameProfileUtils.getUUID();

        if (mc.isSingleplayer())
        {
            sender.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_login_singleplayer")).setStyle(JsonUtils.red()));
            return;
        }
        else if (mc.isConnectedToRealms())
        {
            sender.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_login_realms")).setStyle(JsonUtils.red()));
            return;
        }

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
                if (data != null)
                {
                    if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.serverIP) != null)
                    {
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_login_already_add")).setStyle(JsonUtils.red()));
                        return;
                    }
                    ITextComponent component = ClientCommandBase.getChatComponentFromNthArg(args, 2);
                    String value = component.createCopy().getUnformattedText();
                    ExtendedConfig.loginData.addAutoLogin(data.serverIP, "/" + args[1] + " ", Base64Utils.encode(value), uuid, "");
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_login_set")));
                    ExtendedConfig.save();
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                if (data != null)
                {
                    if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.serverIP) != null)
                    {
                        ExtendedConfig.loginData.removeAutoLogin(uuid + data.serverIP);
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_login_remove")));
                    }
                    else
                    {
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.auto_login_not_set")).setStyle(JsonUtils.red()));
                    }
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
                    sender.sendMessage(component);

                    collection.forEach(loginData ->
                    {
                        sender.sendMessage(new TextComponentTranslation("commands.autologin.list.entry", loginData.getServerIP(), loginData.getUUID()));
                    });
                }
            }
            else if ("function".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    GuiAutoLoginFunction gui = new GuiAutoLoginFunction();
                    gui.display();
                }
                if (args.length == 2)
                {
                    if ("help".equalsIgnoreCase(args[1]))
                    {
                        GuiAutoLoginFunctionHelp gui = new GuiAutoLoginFunctionHelp(false);
                        gui.display();
                    }
                    else
                    {
                        throw new WrongUsageException("commands.autologin.function.usage");
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
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (Minecraft.getMinecraft().isSingleplayer() || Minecraft.getMinecraft().isConnectedToRealms())
        {
            return super.getTabCompletions(server, sender, args, pos);
        }
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "list", "function");
        }
        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("function"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "help");
            }
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}