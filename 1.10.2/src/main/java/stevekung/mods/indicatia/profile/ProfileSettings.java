package stevekung.mods.indicatia.profile;

import java.io.File;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.utils.ModLogger;

public class ProfileSettings
{
    public static final ProfileData profileData = new ProfileData();
    public static final File FILE = new File(IndicatiaMod.MC.mcDataDir, "indicatia_profile.dat");

    public static void load()
    {
        try
        {
            NBTTagCompound nbt = CompressedStreamTools.read(ProfileSettings.FILE);

            if (nbt == null)
            {
                return;
            }
            ProfileSettings.readProfileData(nbt.getTagList("ProfileData", 10));
            ModLogger.info("Loading profile data {}", ProfileSettings.FILE.getPath());
        }
        catch (Exception e) {}
    }

    public static void save()
    {
        try
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("ProfileData", ProfileSettings.writeProfileData());
            CompressedStreamTools.safeWrite(nbt, ProfileSettings.FILE);
        }
        catch (Exception e) {}
    }

    private static NBTTagList writeProfileData()
    {
        NBTTagList list = new NBTTagList();

        for (ProfileData.ProfileSettingData login : ProfileSettings.profileData.getProfileList())
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("Name", login.getProfileName());
            nbt.setBoolean("Ping", (boolean) login.getObjects()[0]);
            nbt.setBoolean("IP", (boolean) login.getObjects()[1]);
            nbt.setBoolean("FPS", (boolean) login.getObjects()[2]);
            nbt.setBoolean("XYZ", (boolean) login.getObjects()[3]);
            nbt.setBoolean("LookBlock", (boolean) login.getObjects()[4]);
            nbt.setBoolean("Direction", (boolean) login.getObjects()[5]);
            nbt.setBoolean("Biome", (boolean) login.getObjects()[6]);
            nbt.setBoolean("Armor", (boolean) login.getObjects()[7]);
            nbt.setBoolean("Potion", (boolean) login.getObjects()[8]);
            nbt.setBoolean("Keystroke", (boolean) login.getObjects()[9]);
            nbt.setBoolean("CPS", (boolean) login.getObjects()[10]);
            nbt.setBoolean("Held", (boolean) login.getObjects()[11]);
            nbt.setString("ArmorStat", (String) login.getObjects()[12]);
            nbt.setString("PotionStat", (String) login.getObjects()[13]);
            nbt.setString("ArmorPos", (String) login.getObjects()[14]);
            nbt.setString("PotionPos", (String) login.getObjects()[15]);
            nbt.setString("KeystokePos", (String) login.getObjects()[16]);
            nbt.setInteger("ArmorOffset", (int) login.getObjects()[17]);
            nbt.setInteger("PotionOffset", (int) login.getObjects()[18]);
            nbt.setInteger("KeystrokeY", (int) login.getObjects()[19]);
            nbt.setBoolean("Time", (boolean) login.getObjects()[20]);
            nbt.setBoolean("GameTime", (boolean) login.getObjects()[21]);
            nbt.setBoolean("MoonPhase", (boolean) login.getObjects()[22]);
            nbt.setBoolean("WeatherStat", (boolean) login.getObjects()[23]);
            nbt.setBoolean("SlimeChunk", (boolean) login.getObjects()[24]);
            list.appendTag(nbt);
        }
        return list;
    }

    private static void readProfileData(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            ProfileSettings.profileData.addProfileData(nbt.getString("Name"), nbt.getBoolean("Ping"), nbt.getBoolean("IP"), nbt.getBoolean("FPS"), nbt.getBoolean("XYZ"), nbt.getBoolean("LookBlock"),
                    nbt.getBoolean("Direction"), nbt.getBoolean("Biome"), nbt.getBoolean("Armor"), nbt.getBoolean("Potion"), nbt.getBoolean("Keystroke"), nbt.getBoolean("CPS"), nbt.getBoolean("Held"),
                    nbt.getString("ArmorStat"), nbt.getString("PotionStat"), nbt.getString("ArmorPos"), nbt.getString("PotionPos"), nbt.getString("KeystokePos"), nbt.getInteger("ArmorOffset"),
                    nbt.getInteger("PotionOffset"), nbt.getInteger("KeystrokeY"), nbt.getBoolean("Time"), nbt.getBoolean("GameTime"), nbt.getBoolean("MoonPhase"), nbt.getBoolean("WeatherStat"), nbt.getBoolean("SlimeChunk"));
        }
    }
}