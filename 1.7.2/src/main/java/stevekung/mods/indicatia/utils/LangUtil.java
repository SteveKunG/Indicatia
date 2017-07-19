package stevekung.mods.indicatia.utils;

import net.minecraft.util.StringTranslate;

public class LangUtil
{
    public static String translate(String key)
    {
        return StringTranslate.instance.translateKey(key);
    }

    public static String translate(String key, Object... format)
    {
        return StringTranslate.instance.translateKeyFormat(key, format);
    }
}