package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class HideNameCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("inhidename").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(Commands.literal("add").then(Commands.argument("name", StringArgumentType.greedyString()).executes(requirement -> HideNameCommand.addHideName(requirement.getSource(), StringArgumentType.getString(requirement, "name")))))
                .then(Commands.literal("remove").then(Commands.argument("name", StringArgumentType.greedyString()).executes(requirement -> HideNameCommand.removeHideName(requirement.getSource(), StringArgumentType.getString(requirement, "name"))))));
    }

    private static int addHideName(CommandSource source, String name)
    {
        if (!HideNameData.getHideNameList().contains(name))
        {
            HideNameData.getHideNameList().add(name);
            ExtendedConfig.save();
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.hide_name.already_added"));
        }
        return 0;
    }

    private static int removeHideName(CommandSource source, String name)
    {
        if (HideNameData.getHideNameList().contains(name))
        {
            HideNameData.getHideNameList().remove(name);
            ExtendedConfig.save();
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.hide_name.already_removed"));
        }
        return 0;
    }
}