package com.stevekung.indicatia.command;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.command.arguments.ProfileNameArgumentType;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekungslib.utils.LangUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ProfileCommand
{
    public ProfileCommand(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(ClientCommandManager.literal("inprofile")
                .then(ClientCommandManager.literal("add").then(ClientCommandManager.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.ADD)).executes(requirement -> ProfileCommand.addProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommandManager.literal("load").then(ClientCommandManager.argument("profile_name", ProfileNameArgumentType.create()).executes(requirement -> ProfileCommand.loadProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommandManager.literal("save").then(ClientCommandManager.argument("profile_name", ProfileNameArgumentType.create()).executes(requirement -> ProfileCommand.saveProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommandManager.literal("remove").then(ClientCommandManager.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.REMOVE)).executes(requirement -> ProfileCommand.removeProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommandManager.literal("list").executes(requirement -> ProfileCommand.getProfileList(requirement.getSource()))));
    }

    private static int addProfile(FabricClientCommandSource source, String name)
    {
        boolean exist = false;

        if (name.equalsIgnoreCase("default"))
        {
            source.sendError(LangUtils.translate("commands.inprofile.cannot_create_default"));
            return 0;
        }

        for (File file : IndicatiaSettings.USER_DIR.listFiles())
        {
            if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
            {
                exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
            }
        }

        if (exist)
        {
            source.sendError(LangUtils.translate("commands.inprofile.profile_already_created", name));
            return 0;
        }
        else
        {
            source.sendFeedback(LangUtils.translate("commands.inprofile.created", name));
            IndicatiaSettings.INSTANCE.save(name);
            return 1;
        }
    }

    private static int loadProfile(FabricClientCommandSource source, String name)
    {
        for (File file : IndicatiaSettings.USER_DIR.listFiles())
        {
            if (!file.getName().equals(name + ".dat"))
            {
                source.sendError(LangUtils.translate("commands.inprofile.cannot_load"));
                return 0;
            }
        }
        IndicatiaSettings.setCurrentProfile(name);
        IndicatiaSettings.saveProfileFile(name);
        IndicatiaSettings.INSTANCE.load();
        source.sendFeedback(LangUtils.translate("commands.inprofile.load", name));
        IndicatiaSettings.INSTANCE.save(name); // save current settings
        return 1;
    }

    private static int saveProfile(FabricClientCommandSource source, String name)
    {
        boolean exist = false;

        for (File file : IndicatiaSettings.USER_DIR.listFiles())
        {
            if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
            {
                exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
            }
        }

        if (exist)
        {
            IndicatiaSettings.INSTANCE.save(name);
            source.sendFeedback(LangUtils.translate("commands.inprofile.save", name));
            return 1;
        }
        else
        {
            source.sendError(LangUtils.translate("commands.inprofile.cannot_save", name));
            return 0;
        }
    }

    private static int removeProfile(FabricClientCommandSource source, String name)
    {
        if (name.equals("default"))
        {
            source.sendError(LangUtils.translate("commands.inprofile.cannot_remove_default"));
            return 0;
        }

        boolean exist = false;

        for (File file : IndicatiaSettings.USER_DIR.listFiles())
        {
            if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
            {
                exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
            }
        }

        if (exist)
        {
            File toDel = new File(IndicatiaSettings.USER_DIR, name + ".dat");
            toDel.delete();
            IndicatiaSettings.setCurrentProfile("default");
            IndicatiaSettings.INSTANCE.load();
            source.sendFeedback(LangUtils.translate("commands.inprofile.remove", name));
            return 1;
        }
        else
        {
            source.sendError(LangUtils.translate("commands.inprofile.cannot_remove", name));
            return 0;
        }
    }

    private static int getProfileList(FabricClientCommandSource source)
    {
        Collection<File> collection = Arrays.stream(IndicatiaSettings.USER_DIR.listFiles()).filter(file -> file.getName().endsWith(".dat")).collect(Collectors.toList());

        if (collection.isEmpty())
        {
            source.sendError(LangUtils.translate("commands.inprofile.list.empty"));
            return 0;
        }
        else
        {
            Component translation = LangUtils.translate("commands.inprofile.list.count", collection.size());
            translation.getStyle().withColor(ChatFormatting.DARK_GREEN);
            source.sendFeedback(translation);

            collection.forEach(file ->
            {
                String name = file.getName();
                String realName = name.replace(".dat", "");
                boolean current = realName.equals(IndicatiaSettings.CURRENT_PROFILE);
                source.sendFeedback(LangUtils.translate("commands.inprofile.list.entry", realName, current ? "- " + ChatFormatting.RED + LangUtils.translate("commands.inprofile.current_profile").getString() : ""));
            });
            return 1;
        }
    }
}