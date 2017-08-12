package stevekung.mods.indicatia.profile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ProfileData
{
    private static final Map<String, ProfileSettingData> profileData = new HashMap<>();

    @Nullable
    public ProfileSettingData getProfile(String name)
    {
        return ProfileData.profileData.get(name);
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
            ProfileData.profileData.put(name, profile);
            return profile;
        }
    }

    public ProfileSettingData saveProfileData(String name, Object... objects)
    {
        ProfileSettingData profile = new ProfileSettingData(name, objects);
        ProfileData.profileData.put(name, profile);
        return profile;
    }

    public void removeProfile(String name)
    {
        ProfileData.profileData.remove(name);
    }

    public Collection<ProfileSettingData> getProfileList()
    {
        return ProfileData.profileData.values();
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