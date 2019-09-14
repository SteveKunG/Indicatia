package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.screen.ExtendedConfigScreen;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

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
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().displayGuiScreen(new ExtendedConfigScreen()));
        return 1;
    }

    private static int startToggleSprint(CommandSource source)
    {
        ExtendedConfig.INSTANCE.toggleSprint = true;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sprint.enable"), false);
        ExtendedConfig.INSTANCE.save();
        return 1;
    }

    private static int stopToggleSprint(CommandSource source)
    {
        ExtendedConfig.INSTANCE.toggleSprint = false;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sprint.disable"), false);
        ExtendedConfig.INSTANCE.save();
        return 1;
    }

    private static int setToggleSprintMode(CommandSource source, String mode)
    {
        ExtendedConfig.INSTANCE.toggleSprintUseMode = mode;
        source.sendFeedback(JsonUtils.create(LangUtils.translate("commands.indicatia.toggle_sprint.set_mode") + " " + LangUtils.translate("commands.mode." + mode)), false);
        ExtendedConfig.INSTANCE.save();
        return 1;
    }

    private static int startToggleSneak(CommandSource source)
    {
        ExtendedConfig.INSTANCE.toggleSneak = true;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sneak.enable"), false);
        ExtendedConfig.INSTANCE.save();
        return 1;
    }

    private static int stopToggleSneak(CommandSource source)
    {
        ExtendedConfig.INSTANCE.toggleSneak = false;
        source.sendFeedback(LangUtils.translateComponent("commands.indicatia.toggle_sneak.disable"), false);
        ExtendedConfig.INSTANCE.save();
        return 1;
    }

    private static int setToggleSneakMode(CommandSource source, String mode)
    {
        ExtendedConfig.INSTANCE.toggleSneakUseMode = mode;
        source.sendFeedback(JsonUtils.create(LangUtils.translate("commands.indicatia.toggle_sneak.set_mode") + " " + LangUtils.translate("commands.mode." + mode)), false);
        ExtendedConfig.INSTANCE.save();
        return 1;
    }
}