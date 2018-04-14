package stevekung.mods.indicatia.command;

import java.io.File;
import java.util.*;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.util.ClientCommandBase;
import stevekung.mods.stevekunglib.util.JsonUtils;
import stevekung.mods.stevekunglib.util.LangUtils;

public class CommandProfile extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "inprofile";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.inprofile.usage");
        }
        else
        {
            if ("add".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.inprofile.add.usage");
                }

                String name = args[1];
                boolean exist = false;

                if (name.equalsIgnoreCase("default"))
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.create_profile_default")).setStyle(JsonUtils.red()));
                    return;
                }

                for (File file : ExtendedConfig.indicatiaDir.listFiles())
                {
                    if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
                    {
                        exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
                    }
                }

                if (exist)
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.already_created", name)).setStyle(JsonUtils.red()));
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.profile_added", name)));
                    ExtendedConfig.save(name);
                }
            }
            else if ("load".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.inprofile.load.usage");
                }

                String name = args[1];

                for (File file : ExtendedConfig.indicatiaDir.listFiles())
                {
                    if (file.getName().contains(name) && file.getName().endsWith(".dat") && !file.exists())
                    {
                        sender.sendMessage(JsonUtils.create(LangUtils.translate("message.cant_load_profile")));
                        return;
                    }
                }

                ExtendedConfig.setCurrentProfile(name);
                ExtendedConfig.saveProfileFile(name);
                ExtendedConfig.load();
                sender.sendMessage(JsonUtils.create(LangUtils.translate("message.load_profile")));
                ExtendedConfig.save(name); // save current settings
            }
            else if ("save".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.inprofile.save.usage");
                }

                String name = args[1];
                boolean exist = false;

                for (File file : ExtendedConfig.indicatiaDir.listFiles())
                {
                    if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
                    {
                        exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
                    }
                }

                if (exist)
                {
                    ExtendedConfig.save(name);
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.save_profile", name)));
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.cant_save_profile", name)).setStyle(JsonUtils.red()));
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.inprofile.remove.usage");
                }

                String name = args[1];

                if (name.equals("default"))
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.cannot_remove_default")).setStyle(JsonUtils.red()));
                    return;
                }

                boolean exist = false;

                for (File file : ExtendedConfig.indicatiaDir.listFiles())
                {
                    if (name.equalsIgnoreCase(file.getName().replace(".dat", "")))
                    {
                        exist = file.getName().equalsIgnoreCase(name + ".dat") && file.exists();
                    }
                }

                if (exist)
                {
                    File toDel = new File(ExtendedConfig.indicatiaDir, name + ".dat");
                    toDel.delete();
                    ExtendedConfig.setCurrentProfile("default");
                    ExtendedConfig.load();
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.remove_profile", name)));
                }
                else
                {
                    sender.sendMessage(JsonUtils.create(LangUtils.translate("message.cant_remove_profile", name)).setStyle(JsonUtils.red()));
                }
            }
            else if ("list".equalsIgnoreCase(args[0]))
            {
                Collection<File> collection = new ArrayList<>(Arrays.asList(ExtendedConfig.indicatiaDir.listFiles()));

                if (collection.isEmpty())
                {
                    throw new CommandException("commands.inprofile.list.empty");
                }
                else
                {
                    int realSize = 0;

                    for (File file : collection)
                    {
                        if (file.getName().endsWith(".dat"))
                        {
                            ++realSize;
                        }
                    }

                    TextComponentTranslation translation = new TextComponentTranslation("commands.inprofile.list.count", realSize);
                    translation.getStyle().setColor(TextFormatting.DARK_GREEN);
                    sender.sendMessage(translation);

                    collection.forEach(file ->
                    {
                        String name = file.getName();
                        String realName = name.replace(".dat", "");
                        boolean current = realName.equals(ExtendedConfig.currentProfile);

                        if (name.endsWith(".dat"))
                        {
                            sender.sendMessage(new TextComponentTranslation("commands.inprofile.list.entry", realName, current ? "- " + TextFormatting.RED + LangUtils.translate("commands.inprofile.current_profile") : ""));
                        }
                    });
                }
            }
            else
            {
                throw new WrongUsageException("commands.inprofile.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "load", "save", "remove", "list");
        }
        if (args.length == 2)
        {
            if ("load".equalsIgnoreCase(args[0]) || "remove".equalsIgnoreCase(args[0]) || "save".equalsIgnoreCase(args[0]))
            {
                if (ExtendedConfig.indicatiaDir.exists())
                {
                    List<String> list = new LinkedList<>();

                    for (File file : ExtendedConfig.indicatiaDir.listFiles())
                    {
                        String name = file.getName();

                        if (("load".equalsIgnoreCase(args[0]) || "save".equalsIgnoreCase(args[0]) || !name.equals("default.dat")) && name.endsWith(".dat"))
                        {
                            list.add(name.replace(".dat", ""));
                        }
                    }
                    return CommandBase.getListOfStringsMatchingLastWord(args, list);
                }
            }
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}