package stevekung.mods.indicatia.util;

import stevekung.mods.indicatia.core.IndicatiaMod;

public class ThreadCheckMojangStatus extends Thread
{
    private final boolean startup;

    public ThreadCheckMojangStatus(boolean startup)
    {
        super("Mojang Status Check Thread");
        this.startup = startup;
    }

    @Override
    public void run()
    {
        for (MojangStatusChecker checker : MojangStatusChecker.valuesCached())
        {
            MojangServerStatus status = checker.getServiceStatus();
            JsonUtil json = new JsonUtil();

            if (this.startup)
            {
                ModLogger.info(checker.getName() + ": " + status.getStatus());
            }
            else
            {
                IndicatiaMod.MC.thePlayer.addChatMessage(json.text(checker.getName() + ": ").appendSibling(json.text(status.getStatus()).setStyle(json.colorFromConfig(status.getColor()))));
            }
        }
    }
}