package stevekung.mods.indicatia.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.profile.ProfileConfigData;
import stevekung.mods.indicatia.profile.ProfileData;
import stevekung.mods.indicatia.profile.ProfileData.ProfileSettingData;
import stevekung.mods.indicatia.profile.RenderProfileConfig;
import stevekung.mods.indicatia.util.JsonUtil;

public class CommandProfile extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "profileiu";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtil json = IndicatiaMod.json;
        ProfileConfigData configData = new ProfileConfigData();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.profileiu.usage");
        }
        else
        {
            if ("add".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.profileiu.add.usage");
                }
                if (RenderProfileConfig.profileData.getProfile(args[1]) != null)
                {
                    sender.sendMessage(json.text("Profile data was already set for name: " + args[1] + "!").setStyle(json.red()));
                    return;
                }
                RenderProfileConfig.profileData.addProfileData(args[1], ConfigManager.enableFPS, ConfigManager.enableXYZ, ConfigManager.enableBiome, ConfigManager.enablePing, ConfigManager.enableServerIP,
                        ConfigManager.enableRenderEquippedItem, ConfigManager.enablePotionStatusHUD, ConfigManager.enableKeystroke, ConfigManager.enableCPS, ConfigManager.enableRCPS, ConfigManager.enableSlimeChunkFinder,
                        ConfigManager.enableCurrentRealTime, ConfigManager.enableCurrentGameTime, ConfigManager.enableGameWeather, ConfigManager.enableMoonPhase, ConfigManager.keystrokePosition, ConfigManager.equipmentOrdering,
                        ConfigManager.equipmentDirection, ConfigManager.equipmentStatus, ConfigManager.equipmentPosition, ConfigManager.potionStatusHUDStyle, ConfigManager.potionStatusHUDPosition, ExtendedConfig.ARMOR_STATUS_OFFSET,
                        ExtendedConfig.POTION_STATUS_OFFSET, ExtendedConfig.KEYSTROKE_Y_OFFSET, ExtendedConfig.CPS_X_OFFSET, ExtendedConfig.CPS_Y_OFFSET);
                sender.sendMessage(json.text("Add profile data name: " + args[1]));
                RenderProfileConfig.save();
            }
            else if ("load".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.profileiu.load.usage");
                }
                if (RenderProfileConfig.profileData.getProfileList().isEmpty())
                {
                    sender.sendMessage(json.text("Cannot load profile data, empty profile data file"));
                    return;
                }

                for (ProfileData.ProfileSettingData data : RenderProfileConfig.profileData.getProfileList())
                {
                    if (RenderProfileConfig.profileData.getProfile(args[1]) != null)
                    {
                        if (args[1].equals(data.getProfileName()))
                        {
                            configData.load(data);
                            //ConfigManager.getConfig().save();
                            ExtendedConfig.save();
                            RenderProfileConfig.save();
                            sender.sendMessage(json.text("Load profile data for name: " + args[1]));
                        }
                    }
                    else
                    {
                        sender.sendMessage(json.text("Cannot load profile data from: " + args[1]).setStyle(json.red()));
                        return;
                    }
                }
            }
            else if ("save".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.profileiu.save.usage");
                }
                for (ProfileData.ProfileSettingData data : RenderProfileConfig.profileData.getProfileList())
                {
                    if (RenderProfileConfig.profileData.getProfile(args[1]) == null)
                    {
                        sender.sendMessage(json.text("Cannot save profile data to: " + args[1]).setStyle(json.red()));
                        return;
                    }
                    if (args[1].equals(data.getProfileName()))
                    {
                        RenderProfileConfig.profileData.saveProfileData(args[1], ConfigManager.enableFPS, ConfigManager.enableXYZ, ConfigManager.enableBiome, ConfigManager.enablePing, ConfigManager.enableServerIP,
                                ConfigManager.enableRenderEquippedItem, ConfigManager.enablePotionStatusHUD, ConfigManager.enableKeystroke, ConfigManager.enableCPS, ConfigManager.enableRCPS, ConfigManager.enableSlimeChunkFinder,
                                ConfigManager.enableCurrentRealTime, ConfigManager.enableCurrentGameTime, ConfigManager.enableGameWeather, ConfigManager.enableMoonPhase, ConfigManager.keystrokePosition, ConfigManager.equipmentOrdering,
                                ConfigManager.equipmentDirection, ConfigManager.equipmentStatus, ConfigManager.equipmentPosition, ConfigManager.potionStatusHUDStyle, ConfigManager.potionStatusHUDPosition, ExtendedConfig.ARMOR_STATUS_OFFSET,
                                ExtendedConfig.POTION_STATUS_OFFSET, ExtendedConfig.KEYSTROKE_Y_OFFSET, ExtendedConfig.CPS_X_OFFSET, ExtendedConfig.CPS_Y_OFFSET);
                        RenderProfileConfig.save();
                        sender.sendMessage(json.text("Save profile data for name: " + args[1]));
                    }
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.profileiu.remove.usage");
                }
                if (RenderProfileConfig.profileData.getProfile(args[1]) != null)
                {
                    RenderProfileConfig.profileData.removeProfile(args[1]);
                    sender.sendMessage(json.text("Remove profile data for name: " + args[1]));
                }
                else
                {
                    sender.sendMessage(json.text("Cannot remove or find profile data from: " + args[1]).setStyle(json.red()));
                }
            }
            else if ("list".equalsIgnoreCase(args[0]))
            {
                Collection<ProfileSettingData> collection = RenderProfileConfig.profileData.getProfileList();

                if (collection.isEmpty())
                {
                    throw new CommandException("commands.profileiu.list.empty");
                }
                else
                {
                    TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.profileiu.list.count", new Object[] {Integer.valueOf(collection.size())});
                    textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
                    sender.sendMessage(textcomponenttranslation);

                    for (ProfileData.ProfileSettingData data : collection)
                    {
                        sender.sendMessage(new TextComponentTranslation("commands.profileiu.list.entry", new Object[] {data.getProfileName()}));
                    }
                }
            }
            else
            {
                throw new WrongUsageException("commands.profileiu.usage");
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
                Collection<ProfileSettingData> collection = RenderProfileConfig.profileData.getProfileList();
                List<String> list = new ArrayList<>();

                for (ProfileData.ProfileSettingData data : collection)
                {
                    list.add(data.getProfileName());
                }
                return CommandBase.getListOfStringsMatchingLastWord(args, list);
            }
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}