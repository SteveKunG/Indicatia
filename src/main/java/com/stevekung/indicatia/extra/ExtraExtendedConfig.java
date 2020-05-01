package com.stevekung.indicatia.extra;

import java.io.File;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.core.IndicatiaMod;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

public class ExtraExtendedConfig
{
    private static final File extraDir = new File(ExtendedConfig.userDir, "extra");
    private static final File file = new File(ExtraExtendedConfig.extraDir, "indicatia_extra.dat");
    static String entityDetectTarget = "reset";

    public static void load()
    {
        if (ExtraExtendedConfig.extraDir.exists())
        {
            ExtraExtendedConfig.extraDir.mkdir();
        }
        try
        {
            CompoundTag nbt = NbtIo.read(ExtraExtendedConfig.file);

            if (nbt == null)
            {
                return;
            }

            ExtraExtendedConfig.entityDetectTarget = ExtraExtendedConfig.getString(nbt, "EntityDetectTarget", ExtraExtendedConfig.entityDetectTarget);
            IndicatiaMod.LOGGER.info("Loading extended config {}", ExtraExtendedConfig.file.getPath());
        }
        catch (Exception e) {}
    }

    public static void save()
    {
        try
        {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("EntityDetectTarget", ExtraExtendedConfig.entityDetectTarget);
            NbtIo.safeWrite(nbt, ExtraExtendedConfig.file);
        }
        catch (Exception e) {}
    }

    private static String getString(CompoundTag nbt, String key, String defaultValue)
    {
        if (nbt.contains(key, 8))
        {
            return nbt.getString(key);
        }
        else
        {
            return defaultValue;
        }
    }
}