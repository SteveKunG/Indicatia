package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class HideNameCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("inhidename")
                .then(ArgumentBuilders.literal("add").then(ArgumentBuilders.argument("name", StringArgumentType.greedyString()).executes(requirement -> HideNameCommand.addHideName(StringArgumentType.getString(requirement, "name"), requirement.getSource()))))
                .then(ArgumentBuilders.literal("remove").then(ArgumentBuilders.argument("name", StringArgumentType.greedyString()).executes(requirement -> HideNameCommand.removeHideName(StringArgumentType.getString(requirement, "name"), requirement.getSource())))));
    }

    private static int addHideName(String name, CottonClientCommandSource source)
    {
        if (!HideNameData.getHideNameList().contains(name))
        {
            HideNameData.getHideNameList().add(name);
            ExtendedConfig.instance.save();
            return 1;
        }
        else
        {
            source.sendError(LangUtils.translateComponent("commands.hide_name.already_added"));
            return 1;
        }
    }

    private static int removeHideName(String name, CottonClientCommandSource source)
    {
        if (HideNameData.getHideNameList().contains(name))
        {
            HideNameData.getHideNameList().remove(name);
            ExtendedConfig.instance.save();
            return 1;
        }
        else
        {
            source.sendError(LangUtils.translateComponent("commands.hide_name.already_removed"));
            return 1;
        }
    }
}