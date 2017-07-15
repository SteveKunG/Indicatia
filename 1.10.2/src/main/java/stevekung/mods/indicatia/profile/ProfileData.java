package stevekung.mods.indicatia.profile;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

public class ProfileData
{
    private Map<String, ProfileSettingData> profileData = Maps.newHashMap();

    @Nullable
    public ProfileSettingData getProfile(String name)
    {
        return this.profileData.get(name);
    }

    public ProfileSettingData addProfileData(String name, Object... objects)
    {
        ProfileSettingData profile = this.getProfile(name);

        if (profile != null)
        {
            throw new IllegalArgumentException("Already have profile data was set for name: " + name + "!");
        }
        else
        {
            profile = new ProfileSettingData(name, objects);
            this.profileData.put(name, profile);
            return profile;
        }
    }

    public ProfileSettingData saveProfileData(String name, Object... objects)
    {
        ProfileSettingData profile = new ProfileSettingData(name, objects);
        this.profileData.put(name, profile);
        return profile;
    }

    public void removeProfile(String name)
    {
        this.profileData.remove(name);
    }

    public Collection<ProfileSettingData> getProfileList()
    {
        return this.profileData.values();
    }

    public static class ProfileSettingData
    {
        private String name;
        private Object[] objects;

        public ProfileSettingData(String name, Object... objects)
        {
            this.name = name;
            this.objects = objects;
        }

        public String getProfileName()
        {
            return this.name;
        }

        public Object[] getObjects()
        {
            return this.objects;
        }
    }
}