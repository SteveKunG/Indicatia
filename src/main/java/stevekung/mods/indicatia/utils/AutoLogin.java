package stevekung.mods.indicatia.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AutoLogin
{
    private static final Map<String, AutoLoginData> autoLogin = new HashMap<>();

    public AutoLoginData getAutoLogin(String data)
    {
        return AutoLogin.autoLogin.get(data);
    }

    public void addAutoLogin(String serverIP, String command, String value, UUID uuid, String function)
    {
        AutoLoginData login = this.getAutoLogin(uuid.toString() + serverIP);

        if (login != null)
        {
            throw new IllegalArgumentException("An auto login data already set for Username: " + uuid.toString() + "!");
        }
        else
        {
            login = new AutoLoginData(serverIP, command, value, uuid, function);
            AutoLogin.autoLogin.put(uuid.toString() + serverIP, login);
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
        private UUID uuid;
        private String function;

        AutoLoginData(String serverIP, String command, String value, UUID uuid, String function)
        {
            this.serverIP = serverIP;
            this.command = command;
            this.value = value;
            this.uuid = uuid;
            this.function = function;
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

        public UUID getUUID()
        {
            return this.uuid;
        }

        public String getFunction()
        {
            return this.function;
        }
    }
}