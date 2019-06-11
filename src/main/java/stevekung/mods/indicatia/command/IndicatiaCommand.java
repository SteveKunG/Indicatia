package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.config.GuiExtendedConfig;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class IndicatiaCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("indicatia").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> IndicatiaCommand.openGui())
                .then(ArgumentBuilders.literal("toggle_sprint").then(ArgumentBuilders.literal("enable").executes(requirement -> IndicatiaCommand.startToggleSprint(requirement.getSource())))
                        .then(ArgumentBuilders.literal("disable").executes(requirement -> IndicatiaCommand.stopToggleSprint(requirement.getSource())))
                        .then(ArgumentBuilders.literal("mode").then(ArgumentBuilders.literal("key_binding").executes(requirement -> IndicatiaCommand.setToggleSprintMode(requirement.getSource(), "key_binding")))
                                .then(ArgumentBuilders.literal("command").executes(requirement -> IndicatiaCommand.setToggleSprintMode(requirement.getSource(), "command")))))
                .then(ArgumentBuilders.literal("toggle_sneak").then(ArgumentBuilders.literal("enable").executes(requirement -> IndicatiaCommand.startToggleSneak(requirement.getSource())))
                        .then(ArgumentBuilders.literal("disable").executes(requirement -> IndicatiaCommand.stopToggleSneak(requirement.getSource())))
                        .then(ArgumentBuilders.literal("mode").then(ArgumentBuilders.literal("key_binding").executes(requirement -> IndicatiaCommand.setToggleSneakMode(requirement.getSource(), "key_binding")))
                                .then(ArgumentBuilders.literal("command").executes(requirement -> IndicatiaCommand.setToggleSneakMode(requirement.getSource(), "command"))))));

        dispatcher.register(ArgumentBuilders.literal("in").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> IndicatiaCommand.openGui())
                .then(ArgumentBuilders.literal("toggle_sprint").then(ArgumentBuilders.literal("enable").executes(requirement -> IndicatiaCommand.startToggleSprint(requirement.getSource())))
                        .then(ArgumentBuilders.literal("disable").executes(requirement -> IndicatiaCommand.stopToggleSprint(requirement.getSource())))
                        .then(ArgumentBuilders.literal("mode").then(ArgumentBuilders.literal("key_binding").executes(requirement -> IndicatiaCommand.setToggleSprintMode(requirement.getSource(), "key_binding")))
                                .then(ArgumentBuilders.literal("command").executes(requirement -> IndicatiaCommand.setToggleSprintMode(requirement.getSource(), "command")))))
                .then(ArgumentBuilders.literal("toggle_sneak").then(ArgumentBuilders.literal("enable").executes(requirement -> IndicatiaCommand.startToggleSneak(requirement.getSource())))
                        .then(ArgumentBuilders.literal("disable").executes(requirement -> IndicatiaCommand.stopToggleSneak(requirement.getSource())))
                        .then(ArgumentBuilders.literal("mode").then(ArgumentBuilders.literal("key_binding").executes(requirement -> IndicatiaCommand.setToggleSneakMode(requirement.getSource(), "key_binding")))
                                .then(ArgumentBuilders.literal("command").executes(requirement -> IndicatiaCommand.setToggleSneakMode(requirement.getSource(), "command"))))));
    }

    private static int openGui()
    {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().openScreen(new GuiExtendedConfig()));
        return 1;
    }

    private static int startToggleSprint(CottonClientCommandSource source)
    {
        ExtendedConfig.instance.toggleSprint = true;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sprint.enable"));
        ExtendedConfig.instance.save();
        return 1;
    }

    private static int stopToggleSprint(CottonClientCommandSource source)
    {
        ExtendedConfig.instance.toggleSprint = false;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sprint.disable"));
        ExtendedConfig.instance.save();
        return 1;
    }

    private static int setToggleSprintMode(CottonClientCommandSource source, String mode)
    {
        ExtendedConfig.instance.toggleSprintUseMode = mode;
        source.sendFeedback(JsonUtils.create(LangUtils.translate("commands.indicatia.toggle_sprint.set_mode") + LangUtils.translate("commands.mode." + mode)));
        ExtendedConfig.instance.save();
        return 1;
    }

    private static int startToggleSneak(CottonClientCommandSource source)
    {
        ExtendedConfig.instance.toggleSneak = true;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sneak.enable"));
        ExtendedConfig.instance.save();
        return 1;
    }

    private static int stopToggleSneak(CottonClientCommandSource source)
    {
        ExtendedConfig.instance.toggleSneak = false;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sneak.disable"));
        ExtendedConfig.instance.save();
        return 1;
    }

    private static int setToggleSneakMode(CottonClientCommandSource source, String mode)
    {
        ExtendedConfig.instance.toggleSneakUseMode = mode;
        source.sendFeedback(JsonUtils.create(LangUtils.translate("commands.indicatia.toggle_sneak.set_mode") + LangUtils.translate("commands.mode." + mode)));
        ExtendedConfig.instance.save();
        return 1;
    }
}