package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.Feedback;
import net.minecraft.server.command.CommandSource;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class HideNameCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("inhidename").requires(requirement -> requirement.hasPermissionLevel(0))
                .then(ArgumentBuilders.literal("add").then(ArgumentBuilders.argument("name", StringArgumentType.greedyString()).executes(requirement -> HideNameCommand.addHideName(StringArgumentType.getString(requirement, "name")))))
                .then(ArgumentBuilders.literal("remove").then(ArgumentBuilders.argument("name", StringArgumentType.greedyString()).executes(requirement -> HideNameCommand.removeHideName(StringArgumentType.getString(requirement, "name"))))));
    }

    private static int addHideName(String name)
    {
        if (!HideNameData.getHideNameList().contains(name))
        {
            HideNameData.getHideNameList().add(name);
            ExtendedConfig.save();
        }
        else
        {
            Feedback.sendError(LangUtils.translateComponent("commands.hide_name.already_added"));
        }
        return 1;
    }

    private static int removeHideName(String name)
    {
        if (HideNameData.getHideNameList().contains(name))
        {
            HideNameData.getHideNameList().remove(name);
            ExtendedConfig.save();
        }
        else
        {
            Feedback.sendError(LangUtils.translateComponent("commands.hide_name.already_removed"));
        }
        return 1;
    }
}