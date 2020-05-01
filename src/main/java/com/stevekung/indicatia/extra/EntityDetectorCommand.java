package com.stevekung.indicatia.extra;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.stevekung.stevekungslib.utils.GameProfileUtils;
import com.stevekung.stevekungslib.utils.JsonUtils;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.command.CommandException;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.CommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class EntityDetectorCommand implements ClientCommandPlugin
{
    public static final SuggestionProvider<CottonClientCommandSource> SUMMONABLE_ENTITIES;

    static
    {
        SUMMONABLE_ENTITIES = SuggestionProviders.register(new Identifier("client_summonable_entities"), (commandContext_1, suggestionsBuilder_1) -> CommandSource.suggestFromIdentifier(Registry.ENTITY_TYPE.stream().filter(EntityType::isSummonable), suggestionsBuilder_1, EntityType::getId, entityType_1 -> new TranslatableText(Util.createTranslationKey("entity", EntityType.getId(entityType_1)), new Object[0])));
    }

    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("entitydetect")
                .then(ArgumentBuilders.literal("entity").then(ArgumentBuilders.argument("target", ClientEntitySummonArgumentType.create()).suggests(EntityDetectorCommand.SUMMONABLE_ENTITIES).executes(requirement -> EntityDetectorCommand.getTargetedEntity(requirement.getSource(), ClientEntitySummonArgumentType.getEntitySummon(requirement, "target").toString()))))
                .then(ArgumentBuilders.literal("player").executes(requirement -> EntityDetectorCommand.getTargetedByType(requirement.getSource(), "player")).then(ArgumentBuilders.argument("target", EntityArgumentType.player()).executes(requirement -> EntityDetectorCommand.getTargetedPlayer(requirement.getSource(), ClientEntitySummonArgumentType.getEntity(requirement, "target").getDisplayName().asString()))))
                .then(ArgumentBuilders.literal("all").executes(requirement -> EntityDetectorCommand.getTargetedByType(requirement.getSource(), "all")))
                .then(ArgumentBuilders.literal("mob").executes(requirement -> EntityDetectorCommand.getTargetedByType(requirement.getSource(), "mob")))
                .then(ArgumentBuilders.literal("creature").executes(requirement -> EntityDetectorCommand.getTargetedByType(requirement.getSource(), "creature")))
                .then(ArgumentBuilders.literal("non_entity").executes(requirement -> EntityDetectorCommand.getTargetedByType(requirement.getSource(), "non_entity")))
                .then(ArgumentBuilders.literal("reset").executes(requirement -> EntityDetectorCommand.resetDetection(requirement.getSource()))));
    }

    private static int getTargetedEntity(CottonClientCommandSource source, String name)
    {
        source.sendFeedback(JsonUtils.create("Detecting entity: " + name));
        ExtraExtendedConfig.entityDetectTarget = name;
        ExtraExtendedConfig.save();
        return 1;
    }

    private static int getTargetedPlayer(CottonClientCommandSource source, String name)
    {
        if (GameProfileUtils.getUsername().equalsIgnoreCase(name))
        {
            throw new CommandException(JsonUtils.create("Cannot set entity detector type to yourself!").setStyle(JsonUtils.RED));
        }
        else
        {
            source.sendFeedback(JsonUtils.create("Detecting player name: " + name));
            ExtraExtendedConfig.entityDetectTarget = name;
            ExtraExtendedConfig.save();
        }
        return 1;
    }

    private static int getTargetedByType(CottonClientCommandSource source, String type)
    {
        source.sendFeedback(JsonUtils.create(type.equalsIgnoreCase("all") ? "Set detect to all" : "Set detecting only: " + type));
        ExtraExtendedConfig.entityDetectTarget = type;
        ExtraExtendedConfig.save();
        return 1;
    }

    private static int resetDetection(CottonClientCommandSource source)
    {
        source.sendFeedback(JsonUtils.create("Reset entity detect"));
        ExtraExtendedConfig.entityDetectTarget = "";
        ExtraExtendedConfig.save();
        return 1;
    }
}