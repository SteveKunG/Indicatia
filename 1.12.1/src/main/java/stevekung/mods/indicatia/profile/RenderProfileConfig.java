package stevekung.mods.indicatia.profile;

import java.io.File;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.ModLogger;

public class RenderProfileConfig
{
    public static ProfileData profileData = new ProfileData();
    private static final File FILE = new File(IndicatiaMod.MC.mcDataDir, "indicatia_profile.dat");

    public static void load()
    {
        try
        {

            NBTTagCompound nbt = CompressedStreamTools.read(RenderProfileConfig.FILE);

            if (nbt == null)
            {
                return;
            }
            RenderProfileConfig.readProfileData(nbt.getTagList("ProfileData", 10));
            ModLogger.info("Loading profile data {}", RenderProfileConfig.FILE.getPath());
        }
        catch (Exception e) {}
    }

    public static void save()
    {
        try
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("ProfileData", RenderProfileConfig.writeProfileData());
            CompressedStreamTools.safeWrite(nbt, RenderProfileConfig.FILE);
        }
        catch (Exception e) {}
    }

    private static NBTTagList writeProfileData()
    {
        NBTTagList list = new NBTTagList();

        for (ProfileData.ProfileSettingData login : RenderProfileConfig.profileData.getProfileList())
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("Name", login.getProfileName());
            nbt.setBoolean("FPS", (boolean) login.getObjects()[0]);
            nbt.setBoolean("XYZ", (boolean) login.getObjects()[1]);
            nbt.setBoolean("Biome", (boolean) login.getObjects()[2]);
            nbt.setBoolean("Ping", (boolean) login.getObjects()[3]);
            nbt.setBoolean("IP", (boolean) login.getObjects()[4]);
            nbt.setBoolean("Equipments", (boolean) login.getObjects()[5]);
            nbt.setBoolean("PotionHUD", (boolean) login.getObjects()[6]);
            nbt.setBoolean("Keystroke", (boolean) login.getObjects()[7]);
            nbt.setBoolean("CPS", (boolean) login.getObjects()[8]);
            nbt.setBoolean("RCPS", (boolean) login.getObjects()[9]);
            nbt.setBoolean("SlimeChunk", (boolean) login.getObjects()[10]);
            nbt.setBoolean("RealTime", (boolean) login.getObjects()[11]);
            nbt.setBoolean("GameTime", (boolean) login.getObjects()[12]);
            nbt.setBoolean("Weather", (boolean) login.getObjects()[13]);
            nbt.setBoolean("MoonPhase", (boolean) login.getObjects()[14]);
            nbt.setString("KeystokePos", (String) login.getObjects()[15]);
            nbt.setString("EquipOrder", (String) login.getObjects()[16]);
            nbt.setString("EquipDirection", (String) login.getObjects()[17]);
            nbt.setString("EquipStatus", (String) login.getObjects()[18]);
            nbt.setString("EquipPos", (String) login.getObjects()[19]);
            nbt.setString("PotionStyle", (String) login.getObjects()[20]);
            nbt.setString("PotionPos", (String) login.getObjects()[21]);
            nbt.setInteger("ArmorOffset", (int) login.getObjects()[22]);
            nbt.setInteger("PotionOffset", (int) login.getObjects()[23]);
            nbt.setInteger("KeystrokeY", (int) login.getObjects()[24]);
            nbt.setInteger("CPSX", (int) login.getObjects()[25]);
            nbt.setInteger("CPSY", (int) login.getObjects()[26]);
            list.appendTag(nbt);
        }
        return list;
    }

    private static void readProfileData(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            RenderProfileConfig.profileData.addProfileData(nbt.getString("Name"), nbt.getBoolean("FPS"), nbt.getBoolean("XYZ"), nbt.getBoolean("Biome"), nbt.getBoolean("Ping"), nbt.getBoolean("IP"),
                    nbt.getBoolean("Equipments"), nbt.getBoolean("PotionHUD"), nbt.getBoolean("Keystroke"), nbt.getBoolean("CPS"), nbt.getBoolean("RCPS"), nbt.getBoolean("SlimeChunk"), nbt.getBoolean("RealTime"),
                    nbt.getBoolean("GameTime"), nbt.getBoolean("Weather"), nbt.getBoolean("MoonPhase"), nbt.getString("KeystokePos"), nbt.getString("EquipOrder"), nbt.getString("EquipDirection"), nbt.getString("EquipStatus"),
                    nbt.getString("EquipPos"), nbt.getString("PotionStyle"), nbt.getString("PotionPos"), nbt.getInteger("ArmorOffset"), nbt.getInteger("PotionOffset"), nbt.getInteger("KeystrokeY"), nbt.getInteger("CPSX"), nbt.getInteger("CPSY"));
        }
    }
}