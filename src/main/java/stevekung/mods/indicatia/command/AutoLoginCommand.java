package stevekung.mods.indicatia.command;

import java.util.Collection;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.Feedback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.GuiAutoLoginFunction;
import stevekung.mods.indicatia.utils.AutoLogin;
import stevekung.mods.indicatia.utils.Base64Utils;
import stevekung.mods.stevekungslib.utils.GameProfileUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class AutoLoginCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("autologin").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(ArgumentBuilders.literal("add").then(ArgumentBuilders.argument("command", StringArgumentType.word()).then(ArgumentBuilders.argument("object", StringArgumentType.greedyString()).executes(requirement -> AutoLoginCommand.addLoginData(StringArgumentType.getString(requirement, "command"), StringArgumentType.getString(requirement, "object"))))))
                .then(ArgumentBuilders.literal("remove").executes(requirement -> AutoLoginCommand.removeLoginData()))
                .then(ArgumentBuilders.literal("list").executes(requirement -> AutoLoginCommand.getLoginDataList()))
                .then(ArgumentBuilders.literal("function").executes(requirement -> AutoLoginCommand.addAutoLoginFunction())));
    }

    private static int addLoginData(String command, String value)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        IntegratedServer data = mc.getServer();
        UUID uuid = GameProfileUtils.getUUID();

        if (mc.isInSingleplayer())
        {
            Feedback.sendError(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer"));
            return 0;
        }
        else if (mc.isConnectedToRealms())
        {
            Feedback.sendError(LangUtils.translateComponent("commands.auto_login.used_in_realms"));
            return 0;
        }

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.getServerIp()) != null)
            {
                Feedback.sendError(LangUtils.translateComponent("commands.auto_login.already_added"));
                return 0;
            }
            ExtendedConfig.loginData.addAutoLogin(data.getServerIp(), "/" + command + " ", Base64Utils.encode(value), uuid, "");
            Feedback.sendFeedback(LangUtils.translateComponent("commands.auto_login.set"));
            ExtendedConfig.instance.save();
            return 1;
        }
        return 1;
    }

    private static int removeLoginData()
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        IntegratedServer data = mc.getServer();
        UUID uuid = GameProfileUtils.getUUID();

        if (mc.isInSingleplayer())
        {
            Feedback.sendError(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer"));
            return 0;
        }
        else if (mc.isConnectedToRealms())
        {
            Feedback.sendError(LangUtils.translateComponent("commands.auto_login.used_in_realms"));
            return 0;
        }

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.getServerIp()) != null)
            {
                ExtendedConfig.loginData.removeAutoLogin(uuid + data.getServerIp());
                Feedback.sendFeedback(LangUtils.translateComponent("commands.auto_login.remove"));
                return 1;
            }
            else
            {
                Feedback.sendError(LangUtils.translateComponent("commands.auto_login.not_set"));
                return 0;
            }
        }
        return 1;
    }

    private static int getLoginDataList()
    {
        Collection<AutoLogin.AutoLoginData> collection = ExtendedConfig.loginData.getAutoLoginList();

        if (collection.isEmpty())
        {
            Feedback.sendError(LangUtils.translateComponent("commands.auto_login.list.empty"));
            return 0;
        }
        else
        {
            StringTextComponent component = new StringTextComponent(LangUtils.translate("commands.auto_login.list.count", collection.size()));
            component.getStyle().setColor(TextFormat.DARK_GREEN);
            Feedback.sendFeedback(component);
            collection.forEach(loginData -> Feedback.sendFeedback(new TranslatableTextComponent(LangUtils.translate("commands.auto_login.list.entry"), loginData.getServerIP(), loginData.getUUID())));
            return 1;
        }
    }

    private static int addAutoLoginFunction()
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.isInSingleplayer())
        {
            Feedback.sendError(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer"));
            return 0;
        }
        else if (mc.isConnectedToRealms())
        {
            Feedback.sendError(LangUtils.translateComponent("commands.auto_login.used_in_realms"));
            return 0;
        }
        mc.execute(() -> mc.openScreen(new GuiAutoLoginFunction()));
        return 1;
    }
}