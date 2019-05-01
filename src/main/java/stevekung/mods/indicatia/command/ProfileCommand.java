package stevekung.mods.indicatia.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.Feedback;
import net.minecraft.server.command.CommandSource;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import stevekung.mods.indicatia.command.arguments.ProfileNameArgumentType;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class ProfileCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("inprofile").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(ArgumentBuilders.literal("add").then(ArgumentBuilders.argument("profile_name", StringArgumentType.word()).executes(requirement -> ProfileCommand.addProfile(StringArgumentType.getString(requirement, "profile_name")))))
                .then(ArgumentBuilders.literal("load").then(ArgumentBuilders.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.NONE)).executes(requirement -> ProfileCommand.loadProfile(ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ArgumentBuilders.literal("save").then(ArgumentBuilders.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.NONE)).executes(requirement -> ProfileCommand.saveProfile(ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ArgumentBuilders.literal("remove").then(ArgumentBuilders.argument("profile_name", ProfileNameArgumentType.create(ProfileNameArgumentType.Mode.REMOVE)).executes(requirement -> ProfileCommand.removeProfile(ProfileNameArgumentType.getProfile(requirement, "profile_name")))))
                .then(ArgumentBuilders.literal("list").executes(requirement -> ProfileCommand.getProfileList())));
    }

    private static int addProfile(String name)
    {
        boolean exist = false;

        if (name.equalsIgnoreCase("default"))
        {
            Feedback.sendError(LangUtils.translateComponent("commands.inprofile.cannot_create_default"));
            return 0;
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
            Feedback.sendError(LangUtils.translateComponent("commands.inprofile.profile_already_created", name));
            return 0;
        }
        else
        {
            Feedback.sendFeedback(LangUtils.translateComponent("commands.inprofile.created", name));
            ExtendedConfig.instance.save(name);
            return 1;
        }
    }

    private static int loadProfile(String name)
    {
        for (File file : ExtendedConfig.userDir.listFiles())
        {
            if (!file.getName().contains(name) && file.getName().endsWith(".dat") && !file.exists())
            {
                Feedback.sendError(LangUtils.translateComponent("commands.inprofile.cannot_load"));
                return 0;
            }
        }
        ExtendedConfig.setCurrentProfile(name);
        ExtendedConfig.saveProfileFile(name);
        ExtendedConfig.instance.load();
        Feedback.sendFeedback(LangUtils.translateComponent("commands.inprofile.load", name));
        ExtendedConfig.instance.save(name); // save current settings
        return 1;
    }

    private static int saveProfile(String name)
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
            ExtendedConfig.instance.save(name);
            Feedback.sendFeedback(LangUtils.translateComponent("commands.inprofile.save", name));
            return 1;
        }
        else
        {
            Feedback.sendError(LangUtils.translateComponent("commands.inprofile.cannot_save", name));
            return 0;
        }
    }

    private static int removeProfile(String name)
    {
        if (name.equals("default"))
        {
            Feedback.sendError(LangUtils.translateComponent("commands.inprofile.cannot_remove_default", name));
            return 0;
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
            ExtendedConfig.instance.load();
            Feedback.sendFeedback(LangUtils.translateComponent("commands.inprofile.remove", name));
            return 1;
        }
        else
        {
            Feedback.sendError(LangUtils.translateComponent("commands.inprofile.cannot_remove", name));
            return 0;
        }
    }

    private static int getProfileList()
    {
        Collection<File> collection = new ArrayList<>(Arrays.asList(ExtendedConfig.userDir.listFiles()));

        if (collection.isEmpty())
        {
            Feedback.sendError(LangUtils.translateComponent("commands.inprofile.list.empty"));
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

            TextComponent translation = LangUtils.translateComponent("commands.inprofile.list.count", size);
            translation.getStyle().setColor(TextFormat.DARK_GREEN);
            Feedback.sendFeedback(translation);

            collection.stream().filter(file -> file.getName().endsWith(".dat")).forEach(file ->
            {
                String name = file.getName();
                String realName = name.replace(".dat", "");
                boolean current = realName.equals(ExtendedConfig.currentProfile);
                Feedback.sendFeedback(LangUtils.translateComponent("commands.inprofile.list.entry", realName, current ? "- " + TextFormat.RED + LangUtils.translate("commands.inprofile.current_profile") : ""));
            });
            return 1;
        }
    }
}