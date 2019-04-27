package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.config.GuiExtendedConfig;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class IndicatiaCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("indicatia").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> IndicatiaCommand.openGui())
                .then(Commands.literal("toggle_sprint").then(Commands.literal("enable").executes(requirement -> IndicatiaCommand.startToggleSprint(requirement.getSource())))
                        .then(Commands.literal("disable").executes(requirement -> IndicatiaCommand.stopToggleSprint(requirement.getSource())))
                        .then(Commands.literal("mode").then(Commands.literal("key_binding").executes(requirement -> IndicatiaCommand.setToggleSprintMode(requirement.getSource(), "key_binding")))
                                .then(Commands.literal("command").executes(requirement -> IndicatiaCommand.setToggleSprintMode(requirement.getSource(), "command")))))
                .then(Commands.literal("toggle_sneak").then(Commands.literal("enable").executes(requirement -> IndicatiaCommand.startToggleSneak(requirement.getSource())))
                        .then(Commands.literal("disable").executes(requirement -> IndicatiaCommand.stopToggleSneak(requirement.getSource())))
                        .then(Commands.literal("mode").then(Commands.literal("key_binding").executes(requirement -> IndicatiaCommand.setToggleSneakMode(requirement.getSource(), "key_binding")))
                                .then(Commands.literal("command").executes(requirement -> IndicatiaCommand.setToggleSneakMode(requirement.getSource(), "command"))))));

        dispatcher.register(Commands.literal("in").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> IndicatiaCommand.openGui())
                .then(Commands.literal("toggle_sprint").then(Commands.literal("enable").executes(requirement -> IndicatiaCommand.startToggleSprint(requirement.getSource())))
                        .then(Commands.literal("disable").executes(requirement -> IndicatiaCommand.stopToggleSprint(requirement.getSource())))
                        .then(Commands.literal("mode").then(Commands.literal("key_binding").executes(requirement -> IndicatiaCommand.setToggleSprintMode(requirement.getSource(), "key_binding")))
                                .then(Commands.literal("command").executes(requirement -> IndicatiaCommand.setToggleSprintMode(requirement.getSource(), "command")))))
                .then(Commands.literal("toggle_sneak").then(Commands.literal("enable").executes(requirement -> IndicatiaCommand.startToggleSneak(requirement.getSource())))
                        .then(Commands.literal("disable").executes(requirement -> IndicatiaCommand.stopToggleSneak(requirement.getSource())))
                        .then(Commands.literal("mode").then(Commands.literal("key_binding").executes(requirement -> IndicatiaCommand.setToggleSneakMode(requirement.getSource(), "key_binding")))
                                .then(Commands.literal("command").executes(requirement -> IndicatiaCommand.setToggleSneakMode(requirement.getSource(), "command"))))));
    }

    private static int openGui()
    {
        Minecraft.getInstance().addScheduledTask(() -> Minecraft.getInstance().displayGuiScreen(new GuiExtendedConfig()));
        return 1;
    }

    private static int startToggleSprint(CommandSource source)
    {
        ExtendedConfig.toggleSprint = true;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sprint.enable"), false);
        ExtendedConfig.save();
        return 1;
    }

    private static int stopToggleSprint(CommandSource source)
    {
        ExtendedConfig.toggleSprint = false;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sprint.disable"), false);
        ExtendedConfig.save();
        return 1;
    }

    private static int setToggleSprintMode(CommandSource source, String mode)
    {
        ExtendedConfig.toggleSprintUseMode = mode;
        source.sendFeedback(JsonUtils.create(LangUtils.translate("commands.indicatia.toggle_sprint.set_mode") + " " + LangUtils.translate("commands.mode." + mode)), false);
        ExtendedConfig.save();
        return 1;
    }

    private static int startToggleSneak(CommandSource source)
    {
        ExtendedConfig.toggleSneak = true;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sneak.enable"), false);
        ExtendedConfig.save();
        return 1;
    }

    private static int stopToggleSneak(CommandSource source)
    {
        ExtendedConfig.toggleSneak = false;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sneak.disable"), false);
        ExtendedConfig.save();
        return 1;
    }

    private static int setToggleSneakMode(CommandSource source, String mode)
    {
        ExtendedConfig.toggleSneakUseMode = mode;
        source.sendFeedback(JsonUtils.create(LangUtils.translate("commands.indicatia.toggle_sneak.set_mode") + " " + LangUtils.translate("commands.mode." + mode)), false);
        ExtendedConfig.save();
        return 1;
    }
}