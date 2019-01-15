package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.command.arguments.ProfileNameArgumentType;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ProfileCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("inprofile").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(Commands.literal("add").then(Commands.argument("profile_name", StringArgumentType.word()).executes(requirement -> ProfileCommand.addProfile(requirement.getSource(), StringArgumentType.getString(requirement, "profile_name")))))
                .then(Commands.literal("load").then(Commands.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.NONE)).executes(requirement -> ProfileCommand.loadProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(Commands.literal("save").then(Commands.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.NONE)).executes(requirement -> ProfileCommand.saveProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(Commands.literal("remove").then(Commands.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.REMOVE)).executes(requirement -> ProfileCommand.removeProfile(requirement.getSource(), ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(Commands.literal("list").executes(requirement -> ProfileCommand.getProfileList(requirement.getSource()))));
    }

    private static int addProfile(CommandSource source, String name)
    {
        boolean exist = false;

        if (name.equalsIgnoreCase("default"))
        {
            throw new CommandException(LangUtils.translateComponent("commands.inprofile.cannot_create_default").setStyle(JsonUtils.red()));
        }

        for (File file : ExtendedConfig.userDir.listFiles())
        {
            if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
            {
                exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
            }
        }

        if (exist)
        {
            throw new CommandException(LangUtils.translateComponent("commands.inprofile.profile_already_created", name).setStyle(JsonUtils.red()));
        }
        else
        {
            source.sendFeedback(LangUtils.translateComponent("commands.inprofile.created", name), false);
            ExtendedConfig.save(name);
        }
        return 0;
    }

    private static int loadProfile(CommandSource source, String name)
    {
        for (File file : ExtendedConfig.userDir.listFiles())
        {
            if (!file.getName().contains(name) && file.getName().endsWith(".dat") && !file.exists())
            {
                throw new CommandException(LangUtils.translateComponent("commands.inprofile.cannot_load").setStyle(JsonUtils.red()));
            }
        }

        ExtendedConfig.setCurrentProfile(name);
        ExtendedConfig.saveProfileFile(name);
        ExtendedConfig.load();
        source.sendFeedback(LangUtils.translateComponent("commands.inprofile.load", name), false);
        ExtendedConfig.save(name); // save current settings
        return 0;
    }

    private static int saveProfile(CommandSource source, String name)
    {
        boolean exist = false;

        for (File file : ExtendedConfig.userDir.listFiles())
        {
            if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
            {
                exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
            }
        }

        if (exist)
        {
            ExtendedConfig.save(name);
            source.sendFeedback(LangUtils.translateComponent("commands.inprofile.save", name), false);
        }
        else
        {
            throw new CommandException(LangUtils.translateComponent("commands.inprofile.cannot_save", name).setStyle(JsonUtils.red()));
        }
        return 0;
    }

    private static int removeProfile(CommandSource source, String name)
    {
        if (name.equals("default"))
        {
            throw new CommandException(LangUtils.translateComponent("commands.inprofile.cannot_remove_default", name).setStyle(JsonUtils.red()));
        }

        boolean exist = false;

        for (File file : ExtendedConfig.userDir.listFiles())
        {
            if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
            {
                exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
            }
        }

        if (exist)
        {
            File toDel = new File(ExtendedConfig.userDir, name + ".dat");
            toDel.delete();
            ExtendedConfig.setCurrentProfile("default");
            ExtendedConfig.load();
            source.sendFeedback(LangUtils.translateComponent("commands.inprofile.remove", name), false);
        }
        else
        {
            throw new CommandException(LangUtils.translateComponent("commands.inprofile.cannot_remove", name).setStyle(JsonUtils.red()));
        }
        return 0;
    }

    private static int getProfileList(CommandSource source)
    {
        Collection<File> collection = new ArrayList<>(Arrays.asList(ExtendedConfig.userDir.listFiles()));

        if (collection.isEmpty())
        {
            throw new CommandException(LangUtils.translateComponent("commands.inprofile.list.empty"));
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
                boolean current = realName.equals(ExtendedConfig.currentProfile);
                source.sendFeedback(LangUtils.translateComponent("commands.inprofile.list.entry", realName, current ? "- " + TextFormatting.RED + LangUtils.translate("commands.inprofile.current_profile") : ""), false);
            });
        }
        return 0;
    }
}