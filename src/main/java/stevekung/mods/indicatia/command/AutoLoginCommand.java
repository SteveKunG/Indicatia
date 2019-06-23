package stevekung.mods.indicatia.command;

import java.util.Collection;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.screen.AutoLoginFunctionScreen;
import stevekung.mods.indicatia.utils.AutoLogin;
import stevekung.mods.indicatia.utils.Base64Utils;
import stevekung.mods.stevekungslib.utils.GameProfileUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class AutoLoginCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("autologin").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(Commands.literal("add").then(Commands.argument("command", StringArgumentType.word()).then(Commands.argument("object", StringArgumentType.greedyString()).executes(requirement -> AutoLoginCommand.addLoginData(requirement.getSource(), StringArgumentType.getString(requirement, "command"), StringArgumentType.getString(requirement, "object"))))))
                .then(Commands.literal("remove").executes(requirement -> AutoLoginCommand.removeLoginData(requirement.getSource())))
                .then(Commands.literal("list").executes(requirement -> AutoLoginCommand.getLoginDataList(requirement.getSource())))
                .then(Commands.literal("function").executes(requirement -> AutoLoginCommand.addAutoLoginFunction(requirement.getSource()))));
    }

    private static int addLoginData(CommandSource source, String command, String value)
    {
        Minecraft mc = Minecraft.getInstance();
        ServerData data = mc.getCurrentServerData();
        UUID uuid = GameProfileUtils.getUUID();

        if (mc.isSingleplayer())
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer"));
            return 0;
        }
        else if (mc.isConnectedToRealms())
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.auto_login.used_in_realms"));
            return 0;
        }

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.serverIP) != null)
            {
                source.sendErrorMessage(LangUtils.translateComponent("commands.auto_login.already_added"));
                return 0;
            }
            ExtendedConfig.loginData.addAutoLogin(data.serverIP, "/" + command + " ", Base64Utils.encode(value), uuid, "");
            source.sendFeedback(LangUtils.translateComponent("commands.auto_login.set"), false);
            ExtendedConfig.instance.save();
            return 1;
        }
        return 0;
    }

    private static int removeLoginData(CommandSource source)
    {
        Minecraft mc = Minecraft.getInstance();
        ServerData data = mc.getCurrentServerData();
        UUID uuid = GameProfileUtils.getUUID();

        if (mc.isSingleplayer())
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer"));
            return 0;
        }
        else if (mc.isConnectedToRealms())
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.auto_login.used_in_realms"));
            return 0;
        }

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.serverIP) != null)
            {
                ExtendedConfig.loginData.removeAutoLogin(uuid + data.serverIP);
                source.sendFeedback(LangUtils.translateComponent("commands.auto_login.remove"), false);
                return 1;
            }
            else
            {
                source.sendErrorMessage(LangUtils.translateComponent("commands.auto_login.not_set"));
                return 0;
            }
        }
        return 0;
    }

    private static int getLoginDataList(CommandSource source)
    {
        Collection<AutoLogin.AutoLoginData> collection = ExtendedConfig.loginData.getAutoLoginList();

        if (collection.isEmpty())
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.auto_login.list.empty"));
            return 0;
        }
        else
        {
            StringTextComponent component = new StringTextComponent(LangUtils.translate("commands.auto_login.list.count", collection.size()));
            component.getStyle().setColor(TextFormatting.DARK_GREEN);
            source.sendFeedback(component, false);
            collection.forEach(loginData -> source.sendFeedback(new TranslationTextComponent(LangUtils.translate("commands.auto_login.list.entry"), loginData.getServerIP(), loginData.getUUID()), false));
            return 1;
        }
    }

    private static int addAutoLoginFunction(CommandSource source)
    {
        Minecraft mc = Minecraft.getInstance();

        if (mc.isSingleplayer())
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer"));
            return 0;
        }
        else if (mc.isConnectedToRealms())
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.auto_login.used_in_realms"));
            return 0;
        }
        mc.execute(() -> mc.displayGuiScreen(new AutoLoginFunctionScreen()));
        return 1;
    }
}