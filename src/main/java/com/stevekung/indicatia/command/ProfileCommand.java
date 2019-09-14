package com.stevekung.indicatia.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.stevekung.indicatia.command.arguments.ProfileNameArgumentType;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ProfileCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("inprofile").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(Commands.literal("add").then(Commands.argument("profile_name", StringArgumentType.word()).executes(requirement -> ProfileCommand.addProfile(requirement.getSource(), StringArgumentType.getString(requirement, "profile_name")))))
                .then(Commands.literal("load").then(Commands.argument("profile_name", ProfileNameArgumentType.create()).executes(requirement -> ProfileCommand.loadProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(Commands.literal("save").then(Commands.argument("profile_name", ProfileNameArgumentType.create()).executes(requirement -> ProfileCommand.saveProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(Commands.literal("remove").then(Commands.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.REMOVE)).executes(requirement -> ProfileCommand.removeProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(Commands.literal("list").executes(requirement -> ProfileCommand.getProfileList(requirement.getSource()))));
    }

    private static int addProfile(CommandSource source, String name)
    {
        boolean exist = false;

        if (name.equalsIgnoreCase("default"))
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.inprofile.cannot_create_default"));
            return 0;
        }

        for (File file : ExtendedConfig.USER_DIR.listFiles())
        {
            if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
            {
                exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
            }
        }

        if (exist)
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.inprofile.profile_already_created", name));
            return 0;
        }
        else
        {
            source.sendFeedback(LangUtils.translateComponent("commands.inprofile.created", name), false);
            ExtendedConfig.INSTANCE.save(name);
            return 1;
        }
    }

    private static int loadProfile(CommandSource source, String name)
    {
        for (File file : ExtendedConfig.USER_DIR.listFiles())
        {
            if (!file.getName().contains(name) && file.getName().endsWith(".dat") && !file.exists())
            {
                source.sendErrorMessage(LangUtils.translateComponent("commands.inprofile.cannot_load"));
                return 0;
            }
        }
        ExtendedConfig.setCurrentProfile(name);
        ExtendedConfig.saveProfileFile(name);
        ExtendedConfig.INSTANCE.load();
        source.sendFeedback(LangUtils.translateComponent("commands.inprofile.load", name), false);
        ExtendedConfig.INSTANCE.save(name); // save current settings
        return 1;
    }

    private static int saveProfile(CommandSource source, String name)
    {
        boolean exist = false;

        for (File file : ExtendedConfig.USER_DIR.listFiles())
        {
            if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
            {
                exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
            }
        }

        if (exist)
        {
            ExtendedConfig.INSTANCE.save(name);
            source.sendFeedback(LangUtils.translateComponent("commands.inprofile.save", name), false);
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.inprofile.cannot_save", name));
            return 0;
        }
    }

    private static int removeProfile(CommandSource source, String name)
    {
        if (name.equals("default"))
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.inprofile.cannot_remove_default", name));
            return 0;
        }

        boolean exist = false;

        for (File file : ExtendedConfig.USER_DIR.listFiles())
        {
            if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
            {
                exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
            }
        }

        if (exist)
        {
            File toDel = new File(ExtendedConfig.USER_DIR, name + ".dat");
            toDel.delete();
            ExtendedConfig.setCurrentProfile("default");
            ExtendedConfig.INSTANCE.load();
            source.sendFeedback(LangUtils.translateComponent("commands.inprofile.remove", name), false);
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.inprofile.cannot_remove", name));
            return 0;
        }
    }

    private static int getProfileList(CommandSource source)
    {
        Collection<File> collection = new ArrayList<>(Arrays.asList(ExtendedConfig.USER_DIR.listFiles()));

        if (collection.isEmpty())
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.inprofile.list.empty"));
            return 0;
        }
        else
        {
            int size = 0;

            for (File file : collection)
            {
                if (file.getName().endsWith(".dat"))
                {
                    ++size;
                }
            }

            ITextComponent translation = LangUtils.translateComponent("commands.inprofile.list.count", size);
            translation.getStyle().setColor(TextFormatting.DARK_GREEN);
            source.sendFeedback(translation, false);

            collection.stream().filter(file -> file.getName().endsWith(".dat")).forEach(file ->
            {
                String name = file.getName();
                String realName = name.replace(".dat", "");
                boolean current = realName.equals(ExtendedConfig.CURRENT_PROFILE);
                source.sendFeedback(LangUtils.translateComponent("commands.inprofile.list.entry", realName, current ? "- " + TextFormatting.RED + LangUtils.translate("commands.inprofile.current_profile") : ""), false);
            });
            return 1;
        }
    }
}