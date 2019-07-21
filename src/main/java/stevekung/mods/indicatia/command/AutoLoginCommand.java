package stevekung.mods.indicatia.command;

import java.util.Collection;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.GuiAutoLoginFunction;
import stevekung.mods.indicatia.utils.AutoLogin;
import stevekung.mods.indicatia.utils.Base64Utils;
import stevekung.mods.stevekungslib.utils.GameProfileUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class AutoLoginCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("autologin")
                .then(ArgumentBuilders.literal("add").then(ArgumentBuilders.argument("command", StringArgumentType.word()).then(ArgumentBuilders.argument("object", StringArgumentType.greedyString()).executes(requirement -> AutoLoginCommand.addLoginData(StringArgumentType.getString(requirement, "command"), StringArgumentType.getString(requirement, "object"), requirement.getSource())))))
                .then(ArgumentBuilders.literal("remove").executes(requirement -> AutoLoginCommand.removeLoginData(requirement.getSource())))
                .then(ArgumentBuilders.literal("list").executes(requirement -> AutoLoginCommand.getLoginDataList(requirement.getSource())))
                .then(ArgumentBuilders.literal("function").executes(requirement -> AutoLoginCommand.addAutoLoginFunction(requirement.getSource()))));
    }

    private static int addLoginData(String command, String value, CottonClientCommandSource source)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        IntegratedServer data = mc.getServer();
        UUID uuid = GameProfileUtils.getUUID();

        if (mc.isInSingleplayer())
        {
            source.sendError(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer"));
            return 1;
        }
        else if (mc.isConnectedToRealms())
        {
            source.sendError(LangUtils.translateComponent("commands.auto_login.used_in_realms"));
            return 1;
        }

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.getServerIp()) != null)
            {
                source.sendError(LangUtils.translateComponent("commands.auto_login.already_added"));
                return 1;
            }
            ExtendedConfig.loginData.addAutoLogin(data.getServerIp(), "/" + command + " ", Base64Utils.encode(value), uuid, "");
            source.sendFeedback(LangUtils.translateComponent("commands.auto_login.set"));
            ExtendedConfig.instance.save();
            return 1;
        }
        return 0;
    }

    private static int removeLoginData(CottonClientCommandSource source)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        IntegratedServer data = mc.getServer();
        UUID uuid = GameProfileUtils.getUUID();

        if (mc.isInSingleplayer())
        {
            source.sendError(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer"));
            return 1;
        }
        else if (mc.isConnectedToRealms())
        {
            source.sendError(LangUtils.translateComponent("commands.auto_login.used_in_realms"));
            return 1;
        }

        if (data != null)
        {
            if (ExtendedConfig.loginData.getAutoLogin(uuid.toString() + data.getServerIp()) != null)
            {
                ExtendedConfig.loginData.removeAutoLogin(uuid + data.getServerIp());
                source.sendFeedback(LangUtils.translateComponent("commands.auto_login.remove"));
                return 1;
            }
            else
            {
                source.sendError(LangUtils.translateComponent("commands.auto_login.not_set"));
                return 1;
            }
        }
        return 0;
    }

    private static int getLoginDataList(CottonClientCommandSource source)
    {
        Collection<AutoLogin.AutoLoginData> collection = ExtendedConfig.loginData.getAutoLoginList();

        if (collection.isEmpty())
        {
            source.sendError(LangUtils.translateComponent("commands.auto_login.list.empty"));
            return 1;
        }
        else
        {
            LiteralText component = new LiteralText(LangUtils.translate("commands.auto_login.list.count", collection.size()));
            component.getStyle().setColor(Formatting.DARK_GREEN);
            source.sendFeedback(component);
            collection.forEach(loginData -> source.sendFeedback(new TranslatableText(LangUtils.translate("commands.auto_login.list.entry"), loginData.getServerIP(), loginData.getUUID())));
            return 1;
        }
    }

    private static int addAutoLoginFunction(CottonClientCommandSource source)
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.isInSingleplayer())
        {
            source.sendError(LangUtils.translateComponent("commands.auto_login.used_in_singleplayer"));
            return 1;
        }
        else if (mc.isConnectedToRealms())
        {
            source.sendError(LangUtils.translateComponent("commands.auto_login.used_in_realms"));
            return 1;
        }
        mc.execute(() -> mc.openScreen(new GuiAutoLoginFunction()));
        return 0;
    }
}