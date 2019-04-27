package stevekung.mods.indicatia.command;

import java.util.Collection;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.Feedback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandException;
import net.minecraft.command.arguments.MessageArgumentType;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.GuiAutoLoginFunction;
import stevekung.mods.indicatia.utils.AutoLogin;
import stevekung.mods.indicatia.utils.Base64Utils;
import stevekung.mods.stevekungslib.utils.GameProfileUtils;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class AutoLoginCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("autologin").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(ArgumentBuilders.literal("add").then(ArgumentBuilders.argument("command", StringArgumentType.word()).then(ArgumentBuilders.argument("object", MessageArgumentType.create()).executes(requirement -> AutoLoginCommand.addLoginData(StringArgumentType.getString(requirement, "command"), ClientCommandUtils.getMessageArgument(requirement, "object"))))))
                .then(ArgumentBuilders.literal("remove").executes(requirement -> AutoLoginCommand.removeLoginData()))
                .then(ArgumentBuilders.literal("list").executes(requirement -> AutoLoginCommand.getLoginDataList()))
                .then(ArgumentBuilders.literal("function").executes(requirement -> AutoLoginCommand.addAutoLoginFunction())));
    }

    private static int addLoginData(String command, TextComponent component)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        IntegratedServer data = mc.getServer();
        UUID uuid = GameProfileUtils.getUUID();

        if (mc.isInSingleplayer())
        {
            throw new CommandException(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer").setStyle(JsonUtils.red()));
        }
        else if (mc.isConnectedToRealms())
        {
            throw new CommandException(LangUtils.translateComponent("commands.auto_login.used_in_realms").setStyle(JsonUtils.red()));
        }

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.getServerIp()) != null)
            {
                throw new CommandException(LangUtils.translateComponent("commands.auto_login.already_added").setStyle(JsonUtils.red()));
            }
            String value = component.copy().getText();
            ExtendedConfig.loginData.addAutoLogin(data.getServerIp(), "/" + command + " ", Base64Utils.encode(value), uuid, "");
            Feedback.sendFeedback(LangUtils.translateComponent("commands.auto_login.set"));
            ExtendedConfig.save();
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
            throw new CommandException(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer").setStyle(JsonUtils.red()));
        }
        else if (mc.isConnectedToRealms())
        {
            throw new CommandException(LangUtils.translateComponent("commands.auto_login.used_in_realms").setStyle(JsonUtils.red()));
        }

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.getServerIp()) != null)
            {
                ExtendedConfig.loginData.removeAutoLogin(uuid + data.getServerIp());
                Feedback.sendFeedback(LangUtils.translateComponent("commands.auto_login.remove"));
            }
            else
            {
                Feedback.sendError(LangUtils.translateComponent("commands.auto_login.not_set"));
            }
        }
        return 1;
    }

    private static int getLoginDataList()
    {
        Collection<AutoLogin.AutoLoginData> collection = ExtendedConfig.loginData.getAutoLoginList();

        if (collection.isEmpty())
        {
            throw new CommandException(LangUtils.translateComponent("commands.auto_login.list.empty").setStyle(JsonUtils.red()));
        }
        else
        {
            StringTextComponent component = new StringTextComponent(LangUtils.translate("commands.auto_login.list.count", collection.size()));
            component.getStyle().setColor(TextFormat.DARK_GREEN);
            Feedback.sendFeedback(component);
            collection.forEach(loginData -> Feedback.sendFeedback(new TranslatableTextComponent(LangUtils.translate("commands.auto_login.list.entry"), loginData.getServerIP(), loginData.getUUID())));
        }
        return 1;
    }

    private static int addAutoLoginFunction()
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.isInSingleplayer())
        {
            throw new CommandException(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer").setStyle(JsonUtils.red()));
        }
        else if (mc.isConnectedToRealms())
        {
            throw new CommandException(LangUtils.translateComponent("commands.auto_login.used_in_realms").setStyle(JsonUtils.red()));
        }
        mc.execute(() -> mc.openScreen(new GuiAutoLoginFunction()));
        return 1;
    }
}