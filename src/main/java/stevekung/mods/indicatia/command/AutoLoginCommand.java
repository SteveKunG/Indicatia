package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.GuiAutoLoginFunction;
import stevekung.mods.indicatia.utils.AutoLogin;
import stevekung.mods.indicatia.utils.Base64Utils;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.util.Collection;
import java.util.UUID;

public class AutoLoginCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("autologin").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(Commands.literal("add").then(Commands.argument("command", StringArgumentType.word()).then(Commands.argument("object", MessageArgument.message())).executes(requirement -> AutoLoginCommand.addLoginData(requirement.getSource(), StringArgumentType.getString(requirement, "command"), MessageArgument.getMessage(requirement, "object")))))
                .then(Commands.literal("remove").executes(requirement -> AutoLoginCommand.removeLoginData(requirement.getSource())))
                .then(Commands.literal("list").executes(requirement -> AutoLoginCommand.getLoginDataList(requirement.getSource())))
                .then(Commands.literal("function").executes(requirement -> AutoLoginCommand.addAutoLoginFunction())));
    }

    private static int addLoginData(CommandSource source, String command, ITextComponent component)
    {
        Minecraft mc = Minecraft.getInstance();
        ServerData data = mc.getCurrentServerData();
        UUID uuid = GameProfileUtils.getUUID();

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.serverIP) != null)
            {
                throw new CommandException(LangUtils.translateComponent("message.auto_login_already_add").setStyle(JsonUtils.red()));
            }
            String value = component.createCopy().getUnformattedComponentText();
            ExtendedConfig.loginData.addAutoLogin(data.serverIP, "/" + command + " ", Base64Utils.encode(value), uuid, "");
            source.sendFeedback(LangUtils.translateComponent("message.auto_login_set"), false);
            ExtendedConfig.save();
        }
        return 0;
    }

    private static int removeLoginData(CommandSource source)
    {
        Minecraft mc = Minecraft.getInstance();
        ServerData data = mc.getCurrentServerData();
        UUID uuid = GameProfileUtils.getUUID();

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.serverIP) != null)
            {
                ExtendedConfig.loginData.removeAutoLogin(uuid + data.serverIP);
                source.sendFeedback(LangUtils.translateComponent("message.auto_login_remove"), false);
            }
            else
            {
                source.sendFeedback(LangUtils.translateComponent("message.auto_login_not_set").setStyle(JsonUtils.red()), false);
            }
        }
        return 0;
    }

    private static int getLoginDataList(CommandSource source)
    {
        Collection<AutoLogin.AutoLoginData> collection = ExtendedConfig.loginData.getAutoLoginList();

        if (collection.isEmpty())
        {
            throw new CommandException(LangUtils.translateComponent("commands.autologin.list.empty"));
        }
        else
        {
            TextComponentTranslation component = new TextComponentTranslation(LangUtils.translate("commands.autologin.list.count"), collection.size());
            component.getStyle().setColor(TextFormatting.DARK_GREEN);
            source.sendFeedback(component, false);
            collection.forEach(loginData -> source.sendFeedback(new TextComponentTranslation(LangUtils.translate("commands.autologin.list.entry"), loginData.getServerIP(), loginData.getUUID()), false));
        }
        return 0;
    }

    private static int addAutoLoginFunction()
    {
        Minecraft.getInstance().addScheduledTask(() -> Minecraft.getInstance().displayGuiScreen(new GuiAutoLoginFunction()));
        return 0;
    }
}