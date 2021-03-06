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
import com.stevekung.stevekungslib.utils.client.command.IClientSharedSuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class ProfileCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSharedSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("inprofile")
                .then(ClientCommands.literal("add")
                        .then(ClientCommands.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.ADD))
                                .executes(requirement -> ProfileCommand.addProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommands.literal("load")
                        .then(ClientCommands.argument("profile_name", ProfileNameArgumentType.create())
                                .executes(requirement -> ProfileCommand.loadProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommands.literal("save")
                        .then(ClientCommands.argument("profile_name", ProfileNameArgumentType.create())
                                .executes(requirement -> ProfileCommand.saveProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommands.literal("remove")
                        .then(ClientCommands.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.REMOVE))
                                .executes(requirement -> ProfileCommand.removeProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ClientCommands.literal("list")
                        .executes(requirement -> ProfileCommand.getProfileList(requirement.getSource()))));
    }

    private static int addProfile(IClientSharedSuggestionProvider source, String name)
    {
        File file = ProfileNameArgumentType.getProfileFile(name);

        if (name.equalsIgnoreCase("default"))
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_create_default"));
            return 1;
        }

        if (file != null && file.exists())
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.already_exist", name));
        }
        else
        {
            source.sendFeedback(LangUtils.translate("commands.inprofile.created", name));
            IndicatiaSettings.INSTANCE.save(name);
        }
        return 1;
    }

    private static int loadProfile(IClientSharedSuggestionProvider source, String name)
    {
        File file = ProfileNameArgumentType.getProfileFile(name);

        if (file != null)
        {
            if (file.exists())
            {
                IndicatiaSettings.setCurrentProfile(name);
                IndicatiaSettings.saveProfileFile(name);
                IndicatiaSettings.INSTANCE.load();
                source.sendFeedback(LangUtils.translate("commands.inprofile.load", name));
                IndicatiaSettings.INSTANCE.save(name); // save current settings
            }
            else
            {
                source.sendErrorMessage(LangUtils.translate("commands.inprofile.profile_not_exist", name));
            }
            return 1;
        }
        else
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.profile_not_exist", name));
        }
        return 1;
    }

    private static int saveProfile(IClientSharedSuggestionProvider source, String name)
    {
        File file = ProfileNameArgumentType.getProfileFile(name);

        if (file != null && file.exists())
        {
            IndicatiaSettings.INSTANCE.save(name);
            source.sendFeedback(LangUtils.translate("commands.inprofile.save", name));
        }
        else
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.profile_not_exist", name));
        }
        return 1;
    }

    private static int removeProfile(IClientSharedSuggestionProvider source, String name)
    {
        if (name.equals("default"))
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.cannot_remove_default"));
            return 1;
        }

        File file = ProfileNameArgumentType.getProfileFile(name);

        if (file != null && file.exists())
        {
            File toDel = new File(IndicatiaSettings.USER_DIR, name + ".dat");

            if (toDel.delete())
            {
                IndicatiaSettings.setCurrentProfile("default");
                IndicatiaSettings.INSTANCE.load();
                source.sendFeedback(LangUtils.translate("commands.inprofile.remove", name));
            }
            else
            {
                source.sendErrorMessage(LangUtils.translate("commands.inprofile.profile_not_exist", name));
            }
        }
        else
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.profile_not_exist", name));
        }
        return 1;
    }

    private static int getProfileList(IClientSharedSuggestionProvider source)
    {
        Collection<File> collection = Arrays.stream(IndicatiaSettings.USER_DIR.listFiles()).filter(file -> file.getName().endsWith(".dat")).collect(Collectors.toList());

        if (collection.isEmpty())
        {
            source.sendErrorMessage(LangUtils.translate("commands.inprofile.list.empty"));
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
                source.sendFeedback(LangUtils.translate("commands.inprofile.list.entry", realName, current ? "- " + ChatFormatting.GREEN + LangUtils.translate("commands.inprofile.current_profile").getString() : ""));
            });
        }
        return 1;
    }
}