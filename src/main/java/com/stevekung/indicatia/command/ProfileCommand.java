package com.stevekung.indicatia.command;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.command.arguments.ProfileNameArgumentType;
import com.stevekung.indicatia.config.IndicatiaSettings;
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
                .then(ClientCommands.literal("add").then(ClientCommands.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.ADD)).executes(requirement -> ProfileCommand.addProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
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

        for (File file : IndicatiaSettings.USER_DIR.listFiles())
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
            IndicatiaSettings.INSTANCE.save(name);
            return 1;
        }
    }

    private static int loadProfile(IClientSuggestionProvider source, String name)
    {
        for (File file : IndicatiaSettings.USER_DIR.listFiles())
        {
            if (!file.getName().equals(name + ".dat"))
            {
                source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_load"));
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

    private static int saveProfile(IClientSuggestionProvider source, String name)
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
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_save", name));
            return 0;
        }
    }

    private static int removeProfile(IClientSuggestionProvider source, String name)
    {
        if (name.equals("default"))
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_remove_default"));
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
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_remove", name));
            return 0;
        }
    }

    private static int getProfileList(IClientSuggestionProvider source)
    {
        Collection<File> collection = Arrays.stream(IndicatiaSettings.USER_DIR.listFiles()).filter(file -> file.getName().endsWith(".dat")).collect(Collectors.toList());

        if (collection.isEmpty())
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.list.empty"));
            return 0;
        }
        else
        {
            ITextComponent translation = LangUtils.translate("commands.inprofile.list.count", collection.size());
            translation.getStyle().setFormatting(TextFormatting.DARK_GREEN);
            source.sendFeedback(translation);

            collection.forEach(file ->
            {
                String name = file.getName();
                String realName = name.replace(".dat", "");
                boolean current = realName.equals(IndicatiaSettings.CURRENT_PROFILE);
                source.sendFeedback(LangUtils.translate("commands.inprofile.list.entry", realName, current ? "- " + TextFormatting.RED + LangUtils.translate("commands.inprofile.current_profile").getString() : ""));
            });
            return 1;
        }
    }
}