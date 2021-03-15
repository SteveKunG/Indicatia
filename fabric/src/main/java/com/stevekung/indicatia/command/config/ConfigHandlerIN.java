package com.stevekung.indicatia.command.config;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

import com.stevekung.indicatia.core.IndicatiaMod;
import com.stevekung.stevekungslib.utils.ConfigHandlerBase;
import com.stevekung.stevekungslib.utils.TextComponentUtils;

public class ConfigHandlerIN extends ConfigHandlerBase
{
    private IndicatiaConfig config;

    public ConfigHandlerIN()
    {
        super(IndicatiaMod.MOD_ID);
    }

    public IndicatiaConfig getConfig()
    {
        if (this.config == null)
        {
            try
            {
                this.loadConfig();
            }
            catch (IOException e)
            {
                IndicatiaMod.LOGGER.error("Failed to load config, using default.", e);
                return new IndicatiaConfig();
            }
        }
        return this.config;
    }

    @Override
    public void loadConfig() throws IOException
    {
        this.configFile.getParentFile().mkdirs();

        if (!this.configFile.exists())
        {
            IndicatiaMod.LOGGER.error("Unable to find config file, creating new one.");
            this.config = new IndicatiaConfig();
            this.saveConfig();
        }
        else
        {
            this.config = GSON.fromJson(ConfigHandlerBase.readFile(this.configFile.toPath().toString(), Charset.defaultCharset()), IndicatiaConfig.class);
        }
    }

    @Override
    public void saveConfig() throws IOException
    {
        this.configFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(this.configFile);
        TextComponentUtils.toJson(this.config, writer);
        writer.close();
    }
}