package com.stevekung.indicatia.fabric.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import com.stevekung.stevekunglib.utils.LangUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.world.item.FishingRodItem;

public class AutoFishCommand
{
    public AutoFishCommand(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(ClientCommandManager.literal("autofish").executes(requirement -> AutoFishCommand.doAutofish(requirement.getSource())));
    }

    private static int doAutofish(FabricClientCommandSource source)
    {
        if (InfoUtils.INSTANCE.isHypixel())
        {
            throw new CommandRuntimeException(LangUtils.translate("commands.not_allowed_hypixel"));
        }

        if (!HUDHelper.START_AUTO_FISH)
        {
            var player = Minecraft.getInstance().player;
            var mainHand = player.getMainHandItem().getItem() instanceof FishingRodItem;
            var offHand = player.getOffhandItem().getItem() instanceof FishingRodItem;

            if (player.getMainHandItem().getItem() instanceof FishingRodItem)
            {
                offHand = false;
            }

            if (mainHand || offHand)
            {
                HUDHelper.START_AUTO_FISH = true;
                source.sendFeedback(LangUtils.translate("commands.auto_fish.enable"));
                return 1;
            }
            else
            {
                source.sendError(LangUtils.translate("commands.auto_fish.not_equipped_fishing_rod"));
                return 0;
            }
        }
        else
        {
            HUDHelper.START_AUTO_FISH = false;
            source.sendFeedback(LangUtils.translate("commands.auto_fish.disable"));
            return 1;
        }
    }
}