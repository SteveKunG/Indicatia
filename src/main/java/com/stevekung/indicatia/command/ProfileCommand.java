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
import com.stevekung.stevekungslib.utils.client.command.ClientCommands;
import com.stevekung.stevekungslib.utils.client.command.IClientCommand;
import com.stevekung.stevekungslib.utils.client.command.IClientSuggestionProvider;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ProfileCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("inprofile")
                .then(ClientCommands.literal("add").then(ClientCommands.argument("profile_name", StringArgumentType.word()).executes(requirement -> ProfileCommand.addProfile(requirement.getSource(), StringArgumentType.getString(requirement, "profile_name")))))
                .then(ClientCommands.literal("load").then(ClientCommands.argument("profile_name", ProfileNameArgumentType.create()).executes(requirement -> ProfileCommand.loadProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommands.literal("save").then(ClientCommands.argument("profile_name", ProfileNameArgumentType.create()).executes(requirement -> ProfileCommand.saveProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommands.literal("remove").then(ClientCommands.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.REMOVE)).executes(requirement -> ProfileCommand.removeProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommands.literal("list").executes(requirement -> ProfileCommand.getProfileList(requirement.getSource()))));
    }

    private static int addProfile(IClientSuggestionProvider source, String name)
    {
        boolean exist = false;

        if (name.equalsIgnoreCase("default"))
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_create_default"));
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
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.profile_already_created", name));
            return 0;
        }
        else
        {
            source.sendFeedback(LangUtils.translate("commands.inprofile.created", name));
            ExtendedConfig.INSTANCE.save(name);
            return 1;
        }
    }

    private static int loadProfile(IClientSuggestionProvider source, String name)
    {
        for (File file : ExtendedConfig.USER_DIR.listFiles())
        {
            if (!file.getName().contains(name) && file.getName().endsWith(".dat") && !file.exists())
            {
                source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_load"));
                return 0;
            }
        }
        ExtendedConfig.setCurrentProfile(name);
        ExtendedConfig.saveProfileFile(name);
        ExtendedConfig.INSTANCE.load();
        source.sendFeedback(LangUtils.translate("commands.inprofile.load", name));
        ExtendedConfig.INSTANCE.save(name); // save current settings
        return 1;
    }

    private static int saveProfile(IClientSuggestionProvider source, String name)
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
            source.sendFeedback(LangUtils.translate("commands.inprofile.save", name));
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_save", name));
            return 0;
        }
    }

    private static int removeProfile(IClientSuggestionProvider source, String name)
    {
        if (name.equals("default"))
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_remove_default", name));
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
            source.sendFeedback(LangUtils.translate("commands.inprofile.remove", name));
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_remove", name));
            return 0;
        }
    }

    private static int getProfileList(IClientSuggestionProvider source)
    {
        Collection<File> collection = new ArrayList<>(Arrays.asList(ExtendedConfig.USER_DIR.listFiles()));

        if (collection.isEmpty())
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.list.empty"));
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

            ITextComponent translation = LangUtils.translate("commands.inprofile.list.count", size);
            translation.getStyle().setFormatting(TextFormatting.DARK_GREEN);
            source.sendFeedback(translation);

            collection.stream().filter(file -> file.getName().endsWith(".dat")).forEach(file ->
            {
                String name = file.getName();
                String realName = name.replace(".dat", "");
                boolean current = realName.equals(ExtendedConfig.CURRENT_PROFILE);
                source.sendFeedback(LangUtils.translate("commands.inprofile.list.entry", realName, current ? "- " + TextFormatting.RED + LangUtils.translate("commands.inprofile.current_profile").getString() : ""));
            });
            return 1;
        }
    }
}