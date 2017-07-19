package stevekung.mods.indicatia.utils;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

public class AutoLogin
{
    private static final Map<String, AutoLoginData> autoLogin = Maps.newHashMap();

    public AutoLoginData getAutoLogin(String data)
    {
        return AutoLogin.autoLogin.get(data);
    }

    public AutoLoginData addAutoLogin(String serverIP, String command, String value, String uuid)
    {
        AutoLoginData login = this.getAutoLogin(uuid.toString() + serverIP);

        if (login != null)
        {
            throw new IllegalArgumentException("An auto login data already set for Username: " + uuid.toString() + "!");
        }
        else
        {
            login = new AutoLoginData(serverIP, command, value, uuid);
            AutoLogin.autoLogin.put(uuid.toString() + serverIP, login);
            return login;
        }
    }

    public void removeAutoLogin(String data)
    {
        AutoLogin.autoLogin.remove(data);
    }

    public Collection<AutoLoginData> getAutoLoginList()
    {
        return AutoLogin.autoLogin.values();
    }

    public static class AutoLoginData
    {
        private String serverIP;
        private String command;
        private String value;
        private String uuid;

        public AutoLoginData(String serverIP, String command, String value, String uuid)
        {
            this.serverIP = serverIP;
            this.command = command;
            this.value = value;
            this.uuid = uuid;
        }

        public String getServerIP()
        {
            return this.serverIP;
        }

        public String getCommand()
        {
            return this.command;
        }

        public String getValue()
        {
            return this.value;
        }

        public String getUUID()
        {
            return this.uuid;
        }
    }
}