package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.event.IndicatiaEventHandler;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.command.ClientCommands;
import com.stevekung.stevekungslib.utils.client.command.IClientCommand;
import com.stevekung.stevekungslib.utils.client.command.IClientSuggestionProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandException;
import net.minecraft.item.FishingRodItem;

public class AutoFishCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("autofish").executes(requirement -> AutoFishCommand.doAutofish(requirement.getSource())));
    }

    private static int doAutofish(IClientSuggestionProvider source)
    {
        if (InfoUtils.INSTANCE.isHypixel())
        {
            throw new CommandException(LangUtils.translateComponent("commands.not_allowed_hypixel"));
        }

        if (!IndicatiaEventHandler.START_AUTO_FISH)
        {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            boolean mainHand = player.getHeldItemMainhand().getItem() instanceof FishingRodItem;
            boolean offHand = player.getHeldItemOffhand().getItem() instanceof FishingRodItem;

            if (player.getHeldItemMainhand().getItem() instanceof FishingRodItem)
            {
                offHand = false;
            }

            if (mainHand || offHand)
            {
                IndicatiaEventHandler.START_AUTO_FISH = true;
                source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.enable"));
                return 1;
            }
            else
            {
                source.sendErrorMessage(LangUtils.translateComponent("commands.auto_fish.not_equipped_fishing_rod"));
                return 0;
            }
        }
        else
        {
            IndicatiaEventHandler.START_AUTO_FISH = false;
            source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.disable"));
            return 1;
        }
    }
}